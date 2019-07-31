package client_package;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread
{
     /* psuedo-code
      * {(+)"\n\n//This is a comment"(line, 7)(fileName, Main.java)}
      * (+) OR (-) -> addition OR deletion to the file
      * "" -> what message is being added or deleted
      * (line, ...) -> where the edit has started
      * (fileName, ...) -> which file the edit is occuring
      * (character, ...) -> this the character after which the edit occurs
      * 
          Contains changes from the Editor every x seconds...
          
          private Queue<String> incomingQueue;
          
          Contains the messages going to the server in proper protocol...
          
          private Queue<String> outgoingQueue;
     */
     private Socket clientSocket;
     private String serverName;
     private int port;
     
     public Client(String serverName, int port)
     {
          this.serverName = serverName;
          this.port = port;
          
          System.out.println("Connecting to " + serverName + " on port " + port);
          try
          {
               clientSocket = new Socket(serverName, port);
               System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());

               OutputStream outToServer = clientSocket.getOutputStream();
               DataOutputStream out = new DataOutputStream(outToServer);              
               out.writeUTF("Hello from " + clientSocket.getLocalSocketAddress() + " \r\n");
               
          } catch (Exception e)
          {
               e.printStackTrace();
          }
          
          this.start();
     }
     
     @Override
     public void run()
     {
          boolean stop = false;
          try(BufferedReader cin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())))
          {
               while(!stop)
               {
                    char temp;
                    String msg = "";
                    while(cin.ready())
                    {
                         temp = (char) cin.read();
                         msg += temp;
                    }
                    if(!msg.isEmpty())
                    {
                         System.out.println(msg);
                         stop = true;
                    }
               }
               if(stop)
               {
                    try
                    {
                         clientSocket.close();
                    } catch (IOException e)
                    {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                    }
               }
          } catch (IOException e)
          {
               e.printStackTrace();
          }
     }
     
     public static void main(String[] args)
     {
         String serverName = args[0];
         int port = Integer.parseInt(args[1]);
         
         new Client(serverName, port);
         new Editor();
}

}
