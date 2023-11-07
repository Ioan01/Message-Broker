package com.ioan01.carrotqueue.request;

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
    private byte[] queueId;
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

    public byte[] getQueueId() {
        return queueId;
    }

    public void setQueueId(byte[] queueId) {
        this.queueId = queueId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
