package com.ioan01.carrotqueue.queue;

import com.ioan01.carrotqueue.exceptions.MessageBrokerException;

import java.util.ArrayDeque;
import java.util.Queue;

public class MessageQueue {
    private final Object padlock;
    private final Queue<String> queue = new ArrayDeque<>();

    private boolean removed;

    public MessageQueue(Object padlock) {
        this.padlock = padlock;
    }

    public void setRemoved() {
        removed = true;
    }

    public boolean offer(String e) {
        synchronized (padlock) {
            if (removed)
                throw new MessageBrokerException("QUEUE ALREADY REMOVE!!!!!!!");
            return queue.offer(e);
        }
    }

    public String poll() {
        synchronized (padlock) {
            if (removed)
                throw new MessageBrokerException("QUEUE ALREADY REMOVE!!!!!!!");
            return queue.poll();
        }
    }

    public String peek() {
        synchronized (padlock) {
            if (removed)
                throw new MessageBrokerException("QUEUE ALREADY REMOVE!!!!!!!");
            return queue.peek();
        }
    }
}
