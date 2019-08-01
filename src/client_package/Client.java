package client_package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client extends Thread
{
     /* Current protocol
      * 
      * {IP, EventType Offset Length StringValue}
      * 
      * IP -> self-explainable
      * EventType -> [+ (EventType.INSERT) OR - (EventType.REMOVE)]
      * Offset -> [off, #]
      * Length -> [len, #]
      * StringValue -> "val"
      * 
      * Example:
      * 
      * {[xxx.xxx.xxx][+][off12][len1]"d"}
      **/
     
     private Socket clientSocket;
     private String serverName;
     private int port;

     private ConcurrentLinkedQueue<String> com;

     public Client(String serverName, int port)
     {
          this.serverName = serverName;
          this.port = port;

          com = new ConcurrentLinkedQueue<String>();

          System.out.println("Connecting to " + serverName + " on port " + port);
          try
          {
               clientSocket = new Socket(serverName, port);
               System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());

          } catch (Exception e)
          {
               e.printStackTrace();
          }

          new Editor(this);

          //send("Hello from " + clientSocket.getLocalSocketAddress() + " \r\n");

          this.start();
     }

     @Override
     public void run()
     {
          try (BufferedReader cin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); PrintWriter cpw = new PrintWriter(clientSocket.getOutputStream(), true);)
          {
               while (true)
               {
                    char temp;
                    String msg = "";
                    while (cin.ready())
                    {
                         temp = (char) cin.read();
                         msg += temp;
                         ArrayList<String> check = RegexParser.matches("^\\{(.*|\\s)\\}$", msg);
                         if (!check.isEmpty())
                         {
                              System.out.println("Check 1: " + check.get(1));
                              msg = "";
                         }
                    }

                    if (!com.isEmpty())
                    {
                         byte[] encoded = com.poll().getBytes(Charset.forName("UTF-8"));
                         cpw.println(new String(encoded, Charset.forName("UTF-8")));
                    }
               }
          } catch (IOException e)
          {
               e.printStackTrace();
          }
     }

     public void send(String msg)
     {
          msg = "[" + clientSocket.getInetAddress().getHostAddress() + "]" + msg;
          msg = "{" + msg + "}";
          com.add(msg);
     }

     public static void main(String[] args)
     {
          String serverName = args[0];
          int port = Integer.parseInt(args[1]);

          new Client(serverName, port);
     }
}
