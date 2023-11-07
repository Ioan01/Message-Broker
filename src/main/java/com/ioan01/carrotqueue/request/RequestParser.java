package com.ioan01.carrotqueue.request;

import com.ioan01.carrotqueue.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestParser implements IRequestParser {
    private Socket clientSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Request request;

    private static Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public RequestParser(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;

        // Used in order to read the incoming data from the Client
        dataInputStream = new DataInputStream(clientSocket.getInputStream());

        // Used in order to write data to the Client
        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        request = new Request();
    }

    @Override
    public Request parse() throws IOException {
        logger.info("Starting request parsing...");

        byte[] data = new byte[256];

        // Read first byte and check the message type
        byte type = dataInputStream.readByte();

        if (type == 0x00) {
            logger.info("Message is of type WRITE_QUEUE.");
            request.setType(RequestType.WRITE_QUEUE);

            request.setLength(dataInputStream.readByte());

            parseQueueId();
            parseMessage(request.getLength());

            request.setData(data);
        }
        else if (type == 0x0F) {
            request.setType(RequestType.READ_QUEUE);
            logger.info("Message is of type READ_QUEUE.");

            parseQueueId();
        }
        else if (type == 0xF0) {
            request.setType(RequestType.ADD_QUEUE);
            logger.info("Message is of type ADD_QUEUE.");

            parseQueueId();
        }
        else if (type == 0xFF) {
            request.setType(RequestType.REMOVE_QUEUE);
            logger.info("Message is of type REMOVE_QUEUE.");

            parseQueueId();
        }

        return request;
    }

    private void parseQueueId() throws IOException {
        byte[] data = new byte[256];
        int currentIndex = 0;

        do {
            dataInputStream.read(data, currentIndex, 1);
        } while(data[currentIndex++] != 0);

        request.setQueueId(data);
        logger.info("Queue ID: " + new String(data, StandardCharsets.UTF_8).substring(0, currentIndex-1));
    }

    private void parseMessage(int length) throws IOException {
        byte[] data = new byte[256];
        int currentIndex = 0;

        while (currentIndex < length) {
            dataInputStream.read(data, currentIndex++, 1);
        }

        request.setData(data);
        logger.info("Data: " + new String(data, StandardCharsets.UTF_8).substring(0, currentIndex));
    }
}
