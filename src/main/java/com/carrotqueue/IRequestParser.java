package main.java.com.carrotqueue;

import java.net.Socket;

public interface IRequestParser {

    Request ParseRequest(Socket socket);

}
