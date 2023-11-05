package com.ioan01.carrotqueue;

import com.ioan01.carrotqueue.server.Server;

import java.io.IOException;

public class ServerRunner {
    public static void main(String[] args) {
        var server = new Server(0420);
        try {
            server.Run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}