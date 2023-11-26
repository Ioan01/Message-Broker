package com.ioan01.carrotqueue.request;

import com.ioan01.carrotqueue.response.ResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static com.ioan01.carrotqueue.request.Request.RequestType;

public class RequestParser implements IRequestParser {
    private static Logger logger = LoggerFactory.getLogger(RequestParser.class);

    private Socket clientSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Request request;

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

        if (type == (byte)0x00) {
            logger.info("Message is of type WRITE_QUEUE.");
            request.setType(RequestType.WRITE_QUEUE);

            request.setLength(dataInputStream.readByte());

            request.setQueueId(parseQueueId());
            request.setData(parseMessage(request.getLength()));

            logger.info("Writing to ID: \'" + request.getQueueId() + "\' data: \'" +  request.getData() + "\'");
        }
        else if (type == (byte)0x0F) {
            request.setType(RequestType.READ_QUEUE);
            logger.info("Message is of type READ_QUEUE.");

            request.setQueueId(parseQueueId());

            logger.info("Reading from ID \'" + request.getQueueId() + "\'");
        }
        else if (type == (byte)0xF0) {
            request.setType(RequestType.ADD_QUEUE);
            logger.info("Message is of type ADD_QUEUE.");

            request.setQueueId(parseQueueId());

            logger.info("Creating queue \'" + request.getQueueId() + "\'");
        }
        else if (type == (byte)0xFF) {
            request.setType(RequestType.REMOVE_QUEUE);
            logger.info("Message is of type REMOVE_QUEUE.");

            request.setQueueId(parseQueueId());

            logger.info("Removing queue with ID \'" + request.getQueueId() + "\'");
        }

        return request;
    }

    private String parseQueueId() throws IOException {
        byte[] data = new byte[256];
        int currentIndex = 0;

        do {
            dataInputStream.read(data, currentIndex, 1);
        } while(data[currentIndex++] != 0);

        logger.info("Queue ID: " + new String(data, StandardCharsets.UTF_8).substring(0, currentIndex-1));
        return new String(data, StandardCharsets.UTF_8);
    }

    private String parseMessage(int length) throws IOException {
        byte[] data = new byte[256];
        int currentIndex = 0;

        while (currentIndex < length) {
            dataInputStream.read(data, currentIndex++, 1);
        }

        logger.info("Data: " + new String(data, StandardCharsets.UTF_8).substring(0, currentIndex));
        return new String(data, StandardCharsets.UTF_8);
    }
}
