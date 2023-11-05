package com.ioan01.carrotqueue.request;

enum RequestType
{
    READ,
    WRITE,
    MANAGE,
}

public class Request {
    RequestType type;

    int length;

    byte[] data;
}
