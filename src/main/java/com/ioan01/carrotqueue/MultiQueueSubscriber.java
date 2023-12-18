package com.ioan01.carrotqueue;

import com.ioan01.carrotqueue.client.Client;
import com.rabbitmq.client.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MultiQueueSubscriber {

    private static final String[] QUEUE_NAMES = {"A_INFO", "B_INFO", "C_INFO", "D_INFO", "E_INFO"};

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            QUEUE_NAMES[i] = "Q"+(i+1);
        }

        for (String queueName : QUEUE_NAMES) {
            Thread subscriberThread = new Thread(() -> subscribeToQueue(queueName));
            subscriberThread.start();
        }
    }

    private static void subscribeToQueue(String queueName) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // Replace with the RabbitMQ server host

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {


            // Set up the callback function
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Received message on " + queueName + ": " + message);

                // Call your function here
                processMessage(message,queueName);
            };

            // Start consuming messages from the queue
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});

            System.out.println("Thread listening for messages on queue '" + queueName + "'.");
            while (true) {
                // Keep the thread alive
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processMessage(String message,String queueId) throws IOException {
        // Implement your custom logic to process the message
        var nums = Integer.parseInt(message);


        var client = new Client("localhost", 4200);

        for (int i = 0; i < nums; i++) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(0x0F); // WRITE_QUEUE
            byteArrayOutputStream.write(queueId.getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(0x00);
            client.write(byteArrayOutputStream.toByteArray());
            client.stop();
        }
    }

}
