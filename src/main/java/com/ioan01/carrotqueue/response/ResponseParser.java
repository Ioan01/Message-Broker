package com.ioan01.carrotqueue.response;

import com.ioan01.carrotqueue.server.Server;

public class ResponseParser implements IResponseParser {

    @Override
    public byte[] handleResponse(Response response) {
        byte[] result = new byte[1 + response.getMessage().length()];

        if (response.getResponseType() == Response.ResponseType.ERROR)
            result[0] = 0;
        else result[0] = 1;

        var responseBytes = response.getMessage().getBytes();
        System.arraycopy(responseBytes, 0, result, 1, responseBytes.length);

        return result;
    }
}
