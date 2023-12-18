#  [![N|Solid](https://cdn-icons-png.flaticon.com/32/1628/1628870.png)](https://nodesource.com/products/nsolid) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CarrotQueue 
## _The Last Message Broker, Ever_ 




CarrotQueue is a Push-Pull Message Queue written in Java

## Features

- Creation / Deletion of Queues
- Reading / Wrtiting from Queus



## Implementation

##### Structure
- The message broker will have a thread listening for incoming connections. Upon receiving one, a new thread handling that socket will be created. 
- The message broker will use TCP and a custom message format ( to be defined )


##### Writing
- The first byte will have a value of **0x00** (WRITE_QUEUE)
- Next byte represents the length of the message (MSG_LEN)
- Next x bytes (null-terminated) are the id of the queue
- Next MSG_LEN bytes represent the message
- Broker can ACK, NOT ACK (queue full) or timeout

##### Reading
- The first byte will have a value of **0x0F** (READ_QUEUE)
- Next x bytes (null terminated) are the id of the queue
- The broker will respond : first x bytes are status
- if applicable
- Next x bytes are MSG_LEN
- Next MSG_LEN are the message bytes
- After receiving, the client can ACK, NOT ACK  or timeout, the last two will cause the message to be added to the front of the queue
- Message will be temporarily removed from queue until ACK/NACK/Timeout -> if NACK/Timeout message is added again

##### Add queue
- The first byte will have a value of **0xF0** (ADD_QUEUE)
- Next x bytes (null terminated) are the id of the new queue
- Broker sends message with status

##### Remove queue
- The first byte will have a value of **0xFF** (REMOVE_QUEUE)
- Next x bytes (null terminated) are the id of the removed queue
- Broker sends message with status 
- Ongoing operations are cancelled

### How does the protocol work?

1. The server constantly listens for incoming connections on a certain port.
2. One (or multiple) clients will send a message to the server, keeping the above message structure. The client will now wait for a response from the server.
3. The server will receive the message and process it (via a `RequestParser` instance).
4. The server will internally manage its inner queues (via a `QueueMaster` instance), based on the client's message type (and other parameters mentioned in the message header). This means, the server can either internally create a new queue, add messages to an existent one, poll a queue for messages, or delete a queue.
5. The server will then manage the client's request via the **RabbitMQ** API. If, at the previous step, a new queue is internally created, it will also be created in the RabbitMQ instance. If a message is sent, RabbitMQ will also be sent the same message. If a queue is deleted, the RabbitMQ corresponding queue (based on the provided ID) will also be deleted. If a message is polled from the queue, the latest message will also be deleted in the corresponding RabbitMQ queue.
6. The server will send an **acknowledgement** to the client. This means, the client will either receive `SUCCESS` (byte 0) or `ERROR` (byte 1), and a further `RESPONSE_MESSAGE` consisting of an arbitrary number of bytes, null-terminated.
7. The client-server connection will be ended.

### Concurrency Issues
- Race Conditions: When multiple consumers or producers attempt to access and modify the queues simultaneously, race conditions can occur, leading to data corruption or unexpected behavior ( if unmanaged ) 
- Message Order: Maintaining the order of messages can be a challenge, especially when multiple consumers are processing messages concurrently, especially since clients can not acknowledge reading messages. Some messages may be processed out of order.
- Starvation: Certain consumers or producers may not get their fair share of resources due to the first-come, first-served policy of the message broker

## Installation

TBA

## Development

TBA

#### Building for source

TBA


## License

GPL-3
