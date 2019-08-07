package client_package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client extends Thread
{
     /* 
      * Current protocol
      * 
      * {EventType Offset Length StringValue}
      * 
      * The whole message (0)
      * EventType -> [+ (EventType.INSERT) OR - (EventType.REMOVE)] (1)
      * Offset -> [off, #] (2)
      * Length -> [len, #] (3)
      * StringValue -> "val" (4)
      * 
      * Example:
      * 
      * {[+][off12][len1]"d"}
      **/

     private Socket clientSocket;
     private String serverName;
     private int port;
     private Editor e;

     private ConcurrentLinkedQueue<String> com;
     private ThreadWriter writer;

     public Client(String serverName, int port)
     {
          this.serverName = serverName;
          this.port = port;

          com = new ConcurrentLinkedQueue<String>();

          System.out.println("Connecting to " + this.serverName + " on port " + this.port);
          try
          {
               clientSocket = new Socket(InetAddress.getByName(this.serverName), this.port);
               //clientSocket = new Socket(serverName, port);
               System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());

          } catch (Exception e)
          {
               e.printStackTrace();
          }

          e = new Editor(this);

          //send("Hello from " + clientSocket.getLocalSocketAddress() + " \r\n");

          this.startThreads();
          e.start();
     }

     public void startThreads()
     {
          this.start();
          writer.start();
     }

     @Override
     public void run()
     {
          try (BufferedReader cin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));)
          {
               while (true)
               {
                    char temp;
                    String msg = "";
                    while (cin.ready())
                    {
                         temp = (char) cin.read();
                         msg += temp;
                         ArrayList<String> check = RegexParser.matches("^\\{(.*)\\}$", msg);
                         if(temp == '\n')
                         {
                      	   msg = "";
                         }
                         if (!check.isEmpty())
                         {
                        	 System.out.println(this.getName());
                              System.out.println("Command: " + check.get(1));
                              e.addUpdate(check.get(1));
                              msg = "";
                         }
                    }

               }
          } catch (IOException e)
          {
               e.printStackTrace();
          }
     }

     public void send(String msg)
     {
          msg = "{" + msg + "}";
          com.add(msg);
     }

     private class ThreadWriter extends Thread
     {
          @Override
          public void run()
          {

               try (PrintWriter cpw = new PrintWriter(clientSocket.getOutputStream(), true);)
               {
                    while (true)
                    {
                         if (!com.isEmpty())
                         {
                              byte[] encoded = com.poll().getBytes(Charset.forName("UTF-8"));
                              //System.out.println(new String(encoded, Charset.forName("UTF-8")));
                              cpw.println(new String(encoded, Charset.forName("UTF-8")));
                              
                         }
                    }
               } catch (IOException e)
               {
                    e.printStackTrace();
               }
          }
     }

     public static void main(String[] args)
     {
          String serverName = args[0];
          int port = Integer.parseInt(args[1]);

          new Client(serverName, port);
     }
}