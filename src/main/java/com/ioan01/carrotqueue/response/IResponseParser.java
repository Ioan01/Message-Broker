package com.ioan01.carrotqueue.response;

public interface IResponseParser {
    byte[] handleResponse(Response response);
}
