# Summary of the Implementation:

It is a collaborative text editor application.
In general, when the application is launched, and the user is not connected to anyone,
it acts as a simple notepad editor, where user can type things and also save the file. 
When it connects to a peer, it can see the collaborative contents on the editor for all connected peers,
and can also write things in the editor, which is reflected to all other peers' UIs.

# GUI Package:

This package mainly handles the user interfaces, which is our editor, and the connection information UI. 
In the editor, we can collaboratively write contents, and save it.
The connection information UI is used to connect/disconnect to/from other peers. 
All the necessary codes for them are handled inside GUI Package. 

# client Package

A peer can be a client to connect to other peers, using TCP connections. 
It sends keep-alive messages periodically to detect lag and network condition. 
If there is no response in 5 seconds, the connection will be closed.
All the necessary codes for them are handled inside this client Package.

# crdt Package:

This CRDT implementation is based on Binary Tree data type. For synchronisation it uses operation-based model.
Atom operation is sending DocElement and Operation Type. 
There are two types of opreations: insert and remove. All operations are indempotent.
Datastamps, which are created, when DocElement(s) are created, are used to solve conflicts between concurrent operations. 
All CRDT classes and their implementations are in the Crdt package.

# main Package:
It is just used to run the application

# network Package:

The network package provides an interface for communication between peers. 
The message sending process is managed by a message queue, where requests will be processed sequentially. 
It also periodically broadcasts host information.
All the necessary codes for them are handled inside this network Package.

# server Package:

A peer can be a server to receive and handle request from other peers. 
The server will listen at a random port between 8000 to 9000.
All the necessary codes for them are handled inside this server Package.

# tests Package:
It was used to do some necessary testings for crdt implementation.