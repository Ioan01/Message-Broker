package com.ioan01.carrotqueue;

import com.ioan01.carrotqueue.client.Client;

import java.io.IOException;

public class ClientRunner {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 0420);

        client.write(0xFF);

        client.stop();
    }
}
