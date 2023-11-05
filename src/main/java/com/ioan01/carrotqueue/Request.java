package com.ioan01.carrotqueue;

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