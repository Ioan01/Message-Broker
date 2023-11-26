package com.ioan01.carrotqueue.queue;

import com.ioan01.carrotqueue.request.Request;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Queue;

public class QueueMaster implements IQueueMaster {

    private HashMap<String,MessageQueue> Queues = new HashMap<>();

    @Override
    public void HandleMessage(Request message) {
        switch (message.getType())
        {

            case WRITE_QUEUE:
                    var queue = Queues.get(message.getQueueId());
                    if (queue == null)
                            throw new Exception();

        }
    }
}
