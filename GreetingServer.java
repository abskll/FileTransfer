// File Name GreetingServer.java
import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class GreetingServer extends Thread {
   private ServerSocket serverSocket;
   String isr; //input string
   String osr; //output string
 
   
   public GreetingServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
        
   }

   public void run() {
      while(true) {
         try {
            System.out.println("Waiting for client on port " + 
               serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(server.getInputStream());
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(System.in));


            //isr = in.readUTF();
            isr = in.readUTF();
            String path = isr.toString();
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            Integer j = 0;
            for (byte s: encoded) {
                j++;
            }
            out.writeUTF(j.toString());
            for(byte s: encoded) {
                out.writeByte(s);
            }
            

            //System.out.println(in.readUTF());
            //DataOutputStream out = new DataOutputStream(server.getOutputStream());
            out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
               + "\nGoodbye!");
            server.close();
            
         } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
            break;
         } catch (IOException e) {
            e.printStackTrace();
            break;
         }
      }
   }
   
   public static void main(String [] args) {
      int port = Integer.parseInt(args[0]);
      try {
         Thread tlisten = new GreetingServer(port);
         tlisten.start();
        
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}