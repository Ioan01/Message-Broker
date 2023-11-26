package com.ioan01.carrotqueue;

import com.ioan01.carrotqueue.client.Client;

import java.io.IOException;

public class ClientRunner {
    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client("localhost", 0420);

        byte[] message = new byte[] {
                (byte)0xF0, // ADD_QUEUE
                0x61, 0x00, // Queue ID = 'a'
        };

        client.write(message);
        client.stop();

        for (int i = 0; i<100; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        createClient();
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
        }
    }

    private static void createClient() throws IOException, InterruptedException {
        var client = new Client("localhost", 0420);

        var message = new byte[]{
                0x00, // WRITE_QUEUE
                0x02, // MSG_LEN = 2
                0x61, 0x00, // Queue ID = 'a'
                0x61, 0x62 // Message: "ab"
        };

        client.write(message);

        client.stop();
    }
}
