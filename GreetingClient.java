// File Name GreetingClient.java
import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class GreetingClient extends Thread {
    String sr;
    String serverName;
    int port;
    String pathfrom;
    String pathto;
    Integer numbytes;

    public GreetingClient(int port, String serverName, String pathfrom, String pathto){
        this.serverName = serverName;
        this.port = port;
        this.pathfrom = pathfrom;
        this.pathto = pathto;
    }
    public void run(){
        try {
            Socket client = new Socket(serverName, port);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());
           
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            System.out.println("Waiting for client input");
            ArrayList<Byte> temp = new ArrayList<Byte>();
            out.writeUTF(pathfrom);
            sr = in.readUTF();
            numbytes = Integer.parseInt(sr);
            
            for (int i= 0; i < numbytes; i++) {
                temp.add(in.readByte());
            }
            Byte[] newtemp = temp.toArray(new Byte[temp.size()]);
            //byte[] temp1 = (byte) newtemp;
            byte[] temp1 = new byte[temp.size()];
            int j = 0;
            for(Byte s: newtemp) {
                temp1[j] = (byte) newtemp[j++];
            }
            Files.write(Paths.get(pathto), temp1);
       
                
            


            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        

    }

   public static void main(String [] args) {
      String serverName = args[0];
      int portsend = Integer.parseInt(args[1]);
      String pathfrom = args[2];
      String pathto = args[3];
      Thread tsend = new GreetingClient(portsend, serverName, pathfrom, pathto);
      tsend.start();
   }
}