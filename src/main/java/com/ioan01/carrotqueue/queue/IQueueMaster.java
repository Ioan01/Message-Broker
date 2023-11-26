package com.ioan01.carrotqueue.queue;

import com.ioan01.carrotqueue.request.Request;
import com.ioan01.carrotqueue.response.Response;

public interface IQueueMaster {
    Response HandleMessage(Request request);
}
