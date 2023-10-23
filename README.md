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
- The client will indicate (first x bytes == WRITE_BYTES)
- Next x bytes are MSG_LEN
- Next x bytes (null-terminated) are the id of the queue
- Next MSG_LEN are the message bytes
- Broker can ACK, NOT ACK (queue full) or timeout

##### Reading
- The client will indicate (first x bytes == READ_BYTES)
- Next x bytes (null terminated) are the id of the queue
- The broker will respond : first x bytes are status
- if applicable
- Next x bytes are MSG_LEN
- Next MSG_LEN are the message bytes
- After receiving, the client can ACK, NOT ACK  or timeout, the last two will cause the message to be added to the front of the queue
- Message will be temporarily removed from queue until ACK/NACK/Timeout -> if NACK/Timeout message is added again

##### Add queue
- The client will indicate (first x bytes == ADD_QUEUE)
- Next x bytes (null terminated) are the id of the new queue
- Broker sends message with status

##### Remove queue
- The client will indicate (first x bytes == REMOVE_QUEUE)
- Next x bytes (null terminated) are the id of the removed queue
- Broker sends message with status 
- Ongoing operations are cancelled

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
