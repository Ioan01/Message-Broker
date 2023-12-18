package com.ioan01.carrotqueue.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public Client(String address, int port) throws IOException {
        logger.info("Establishing connection with " + address + ":" + port + "...");
        socket = new Socket(address, port);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());
        logger.info("Connection has been established successfully.");
    }

    public void write(byte[] b) throws IOException {
        logger.info("Sending message to server...");
        dataOutputStream.write(b);
        logger.info("Message has been sent successfully.");

        logger.info("Waiting for response from server.");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; // You can adjust the buffer size as needed

        int bytesRead;
        if ((bytesRead = dataInputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        byte[] bArr = baos.toByteArray();

        if (bArr[0] == 0) {
            logger.info("Received response from server: " + baos.toString().substring(1));
        }
        else {
            logger.error("Received response from server: " + baos.toString().substring(1));
        }
    }

    public void stop() throws IOException {
        logger.info("Stopping client...");
        socket.close();
        logger.info("Client has been successfully stopped.");
    }
}
