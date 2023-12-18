package com.ioan01.carrotqueue.queue;

import com.ioan01.carrotqueue.exceptions.MessageBrokerException;
import com.ioan01.carrotqueue.request.Request;
import com.ioan01.carrotqueue.response.Response;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

public class QueueMaster implements IQueueMaster {
    private static Logger logger = LoggerFactory.getLogger(QueueMaster.class);

    private final ConcurrentHashMap<String, MessageQueue> Queues = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> Locks = new ConcurrentHashMap<>();

    private int writtenMessages = 0;

    private int readMessages = 0;

    ConnectionFactory factory = new ConnectionFactory();

    public QueueMaster() {
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
    }

    private void WriteInfoToRabbit() {
        String message = "NEW! We have " + Queues.size() + " queues\n";

        for (String key : Queues.keySet()) {
            message += key + "\n";
        }

        AddQueue("info");
        WriteMessage("info", message);
    }

    private void WriteMessage(String queueId, String message) {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {
            channel.confirmSelect();
            channel.basicPublish("", queueId, null, message.getBytes());
            if (channel.waitForConfirms(5000)) {
                logger.info("Message published successfully.");
            } else {
                throw new MessageBrokerException("NO ACK RECEIVED FROM RABBITMQ");
            }
        } catch (TimeoutException ex) {
            logger.error(ex.getMessage());
            throw new MessageBrokerException("RABBITMQ TIMEOUT ERROR");
        } catch (IOException | InterruptedException ex) {
            logger.error(ex.getMessage());
            throw new MessageBrokerException("UNKNOWN ERROR");
        }
    }

    private void AddQueue(String queueId) {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            Map<String, Object> config = new HashMap<>();
            config.put("x-message-ttl", 600000); // TTL in milliseconds

            channel.queueDeclare(queueId, false, false, false, config);
        } catch (TimeoutException ex) {
            logger.error(ex.getMessage());
            throw new MessageBrokerException("RABBITMQ TIMEOUT ERROR");
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new MessageBrokerException("UNKNOWN ERROR");
        }
    }

    private void RemoveQueue(String queueId) {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDelete(queueId);
        } catch (TimeoutException ex) {
            logger.error(ex.getMessage());
            throw new MessageBrokerException("RABBITMQ TIMEOUT ERROR");
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new MessageBrokerException("UNKNOWN ERROR");
        }
    }

    @Override
    public Response HandleMessage(Request message) {
        try {
            switch (message.getType()) {
                case WRITE_QUEUE -> {
                    var queueId = message.getQueueId();

                    var queue = Queues.get(queueId);
                    if (queue == null) {
                        return new Response(Response.ResponseType.ERROR, "NO SUCH QUEUE");
                    }

                    queue.offer(message.getData());

                    try {
                        WriteMessage(queueId, message.getData());
                    } catch (MessageBrokerException ex) {
                        return new Response(Response.ResponseType.ERROR, ex.getMessage());
                    }

                    return new Response(Response.ResponseType.SUCCESS, "MESSAGE ADDED TO QUEUE");
                }
                case READ_QUEUE -> {
                    var queueId = message.getQueueId();

                    var queue = Queues.get(queueId);
                    if (queue == null) {
                        return new Response(Response.ResponseType.ERROR, "NO SUCH QUEUE");
                    }

                    var msg = queue.poll();
                    return new Response(Response.ResponseType.SUCCESS, msg);
                }
                case ADD_QUEUE -> {
                    var queueId = message.getQueueId();

                    var padlock = new Object();
                    var queue = new MessageQueue(padlock);

                    if (Queues.putIfAbsent(queueId, queue) != null) {
                        return new Response(Response.ResponseType.ERROR, "QUEUE ALREADY EXISTS");
                    }

                    Locks.put(queueId, padlock);
                    AddQueue(queueId);
                    WriteInfoToRabbit();
                    return new Response(Response.ResponseType.SUCCESS, "QUEUE ADDED");
                }
                case REMOVE_QUEUE -> {
                    var queueId = message.getQueueId();

                    var padlock = Locks.get(queueId);
                    var queue = Queues.get(queueId);

                    if (queue == null) {
                        return new Response(Response.ResponseType.ERROR, "NO SUCH QUEUE");
                    }

                    synchronized (padlock) {

                        Locks.remove(queueId);
                        Queues.remove(queueId);
                        queue.setRemoved();

                        RemoveQueue(queueId);
                        WriteInfoToRabbit();

                        return new Response(Response.ResponseType.SUCCESS, "QUEUE DELETED");
                    }
                }
                default -> {
                    return new Response(Response.ResponseType.ERROR, "UNKNOWN OPERATION");
                }
            }
        } catch (Exception e) {
            return new Response(Response.ResponseType.ERROR, e.getMessage());
        }
    }
}
