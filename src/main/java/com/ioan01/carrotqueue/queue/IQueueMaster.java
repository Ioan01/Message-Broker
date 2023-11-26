package com.ioan01.carrotqueue.queue;

import com.ioan01.carrotqueue.request.Request;

public interface IQueueMaster {

    void HandleMessage(Request request);

    // create queue or/and
    // pass to queue
}
