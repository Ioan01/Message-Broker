package com.ioan01.carrotqueue;

import java.net.Socket;

public interface IRequestParser {

    Request ParseRequest(Socket socket);

}
