package com.ioan01.carrotqueue;

import com.ioan01.carrotqueue.client.Client;

import java.io.IOException;

public class ClientRunner {
    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client("localhost", 0420);

        byte[] message = new byte[] {
                (byte)0xF0, // ADD_QUEUE
                0x63, 0x00, // Queue ID = 'a'
        };

        client.write(message);
        client.stop();

        for (int i = 0; i<100; i++) {
            int id = i;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        createQueue((byte)id);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            thread.start();

            for (int j = 0; j<10; j++) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            writeToQueue((byte)id);
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                Thread.sleep(10);
                thread.start();
            }
        }
    }

    private static void writeToQueue(byte queueIndex) throws IOException, InterruptedException {
        var client = new Client("localhost", 0420);

        var message = new byte[]{
                0x00, // WRITE_QUEUE
                0x02, // MSG_LEN = 2
                0x63, (byte) (33 + queueIndex), 0x00, // Queue ID = 'a'
                (byte) (33 + queueIndex), (byte) (33 + queueIndex) // Message: "ab"
        };

        client.write(message);

        client.stop();
    }

    private static void createQueue(byte queueId) throws IOException, InterruptedException {
        var client = new Client("localhost", 0420);

        var message = new byte[]{
                (byte)0xF0, // ADD_QUEUE
                0x63, (byte) (33 + queueId), 0x00, // Queue ID = 'a'
        };

        client.write(message);

        client.stop();
    }
}
