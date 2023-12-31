package com.ioan01.carrotqueue;

import com.ioan01.carrotqueue.client.Client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ClientRunner {
    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < 5; i++) {
            int id = i;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        createQueue(new byte[]{(byte)'Q', (byte) (id + 48 + 1)});
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            thread.start();

            for (int j = 0; j < 5; j++) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            writeToQueue(new byte[]{(byte)'Q', (byte) (id + 48 + 1)}, (byte) 3, new byte[]{65, 66, 67});
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

    private static void createQueue(byte[] queueId) throws IOException, InterruptedException {
        var client = new Client("localhost", 4200);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(0xF0); // ADD_QUEUE
        byteArrayOutputStream.write(queueId);
        byteArrayOutputStream.write(0x00);

        client.write(byteArrayOutputStream.toByteArray());

        client.stop();
    }

    private static void writeToQueue(byte[] queueId, byte msgLen, byte[] msgContent) throws IOException, InterruptedException {
        var client = new Client("localhost", 4200);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(0x00); // WRITE_QUEUE
        byteArrayOutputStream.write(msgLen);
        byteArrayOutputStream.write(queueId);
        byteArrayOutputStream.write(0x00);
        byteArrayOutputStream.write(msgContent);

        client.write(byteArrayOutputStream.toByteArray());

        client.stop();
    }

    private static void readFromQueue(byte[] queueId) throws IOException {
        var client = new Client("localhost", 4200);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(0x0F); // WRITE_QUEUE
        byteArrayOutputStream.write(queueId);
        byteArrayOutputStream.write(0x00);

        client.write(byteArrayOutputStream.toByteArray());

        client.stop();
    }
}
