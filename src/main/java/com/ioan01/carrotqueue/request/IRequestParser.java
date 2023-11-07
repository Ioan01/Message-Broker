package com.ioan01.carrotqueue.request;

import java.io.IOException;
import java.net.Socket;

public interface IRequestParser {

    Request parse() throws IOException;

}
