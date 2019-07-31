1. Make a client
1.1 Set up sockets
1.1 Test connections with server
2. Start Handling connections in Client
3. Develop Cuncurrent systems 
4. Develop a Protocol for server/client communication
5. GUI
  a. Use Document Listener to listen to documents and send changes to client; changes will then go to server
 
 Processes in Client:
  1. Send the server commands (create a protocol). 
    a. When the client receives changes from editor, client should said those changes to the server (as "change in document" command)
    b. Open file command
    c. Protocol must tell server what changes are happening, where those changes are occuring, and in which file those changes atre occurring. 
    
 
 Processes in Editor:
  1. Send changes in the document to the client every 15 seconds (Seconds may vary)
  2. 
