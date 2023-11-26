package com.ioan01.carrotqueue.response;

public class Response {
    private ResponseType responseType;
    private String message;

    public Response(ResponseType responseType, String message) {
        this.responseType = responseType;
        this.message = message;
    }

    public static enum ResponseType {
        SUCCESS, ERROR
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
