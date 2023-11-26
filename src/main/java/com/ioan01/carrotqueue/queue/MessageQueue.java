package com.ioan01.carrotqueue.queue;

import java.util.ArrayDeque;
import java.util.Queue;

public class MessageQueue {
    private final Queue<String> queue = new ArrayDeque<>();

    public synchronized boolean offer(String e) {
        return queue.offer(e);
    }

    public synchronized String poll() {
        return queue.poll();
    }

    public synchronized String peek() {
        return queue.peek();
    }
}
