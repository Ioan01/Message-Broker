package com.ioan01.carrotqueue;

import com.ioan01.carrotqueue.client.Client;

import java.io.IOException;

public class ClientRunner {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 0420);

        byte[] message = new byte[] {
                0x00, // WRITE_QUEUE
                0x02, // MSG_LEN = 2
                0x61, 0x00, // Queue ID = 'a'
                0x61, 0x62 // Message: "ab"
        };

        client.write(message);

        client.stop();
    }
}
