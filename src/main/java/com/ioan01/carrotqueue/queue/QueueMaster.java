package com.ioan01.carrotqueue.queue;

import com.ioan01.carrotqueue.request.Request;
import com.ioan01.carrotqueue.response.Response;

import java.util.concurrent.ConcurrentHashMap;

public class QueueMaster implements IQueueMaster {
    private final ConcurrentHashMap<String, MessageQueue> Queues = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> Locks = new ConcurrentHashMap<>();

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
