package com.ioan01.carrotqueue.request;

import java.net.Socket;

public interface IRequestParser {

    Request ParseRequest(Socket socket);

}
