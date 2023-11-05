package com.ioan01.carrotqueue.server;
// listener (1 thread) -> new client socket (1 thread / socket) -> creates new packet reader -> reads package

// threadul initial e suspendat, socketul se tine
// based on request type -> packet -> queue master -> queue event bus -> (actiunea pe queue) -> Packet Writer -> socket
// actiunea e gata, threadul e reluat, se trimite raspunsul actiunii pe socket

import java.io.IOException;

public interface IServer {
    void Run() throws IOException;
}
