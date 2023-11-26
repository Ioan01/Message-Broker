package com.ioan01.carrotqueue.request;

import java.nio.charset.StandardCharsets;

enum RequestType
{
    WRITE_QUEUE,
    READ_QUEUE,
    ADD_QUEUE,
    REMOVE_QUEUE
}

public class Request {
    private RequestType type;
    private int length;
    private String queueId;
    private byte[] data;

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(byte[] queueId) {
        this.queueId = new String(queueId, StandardCharsets.UTF_8);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
