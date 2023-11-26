package com.ioan01.carrotqueue.request;

public class Request {
    private RequestType type;
    private int length;
    private String queueId;
    private String data;

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

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static enum RequestType
    {
        WRITE_QUEUE,
        READ_QUEUE,
        ADD_QUEUE,
        REMOVE_QUEUE
    }
}
