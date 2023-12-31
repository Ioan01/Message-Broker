package com.ioan01.carrotqueue.server;

import com.ioan01.carrotqueue.queue.QueueMaster;
import com.ioan01.carrotqueue.request.IRequestParser;
import com.ioan01.carrotqueue.request.Request;
import com.ioan01.carrotqueue.request.RequestParser;
import com.ioan01.carrotqueue.response.IResponseParser;
import com.ioan01.carrotqueue.response.Response;
import com.ioan01.carrotqueue.response.ResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server implements IServer {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    private int port;
    private ServerSocket serverSocket;
    private QueueMaster queueMaster;
    private ThreadPoolExecutor threadPoolExecutor;

    public Server(int port) {
        this.port = port;
        queueMaster = new QueueMaster();
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    /**
     * Starts the server and calls the Listen() method
     * @throws IOException
     */
    @Override
    public void Run() throws IOException {
        logger.info("Starting server...");
        serverSocket = new ServerSocket(port);
        logger.info("Server started successfully!");
        this.Listen();
    }

    /**
     * The server listens for new connections.
     * After receiving a connection from a Client, a new Thread will call the Serve() method.
     * The main thread will then continue waiting for new connections after that.
     * @throws IOException
     */
    private void Listen() throws IOException {
        while(true) {
            logger.info("Waiting for new connections...");
            Socket clientSocket = serverSocket.accept();
            logger.info("Received connection. Creating a new thread and redirecting socket...");

            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Serve(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            logger.info("Successfully redirected socket.");
        }
    }

    /**
     * Listens to incoming packets from a certain Client (Socket)
     */
    private void Serve(Socket clientSocket) throws IOException {
        IRequestParser requestParser = new RequestParser(clientSocket);
        IResponseParser responseParser = new ResponseParser();

        Request request = requestParser.parse();
        Response response = queueMaster.HandleMessage(request);
        byte[] bytes = responseParser.handleResponse(response);

        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        dataOutputStream.write(bytes);
    }
}
