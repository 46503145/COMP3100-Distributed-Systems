import java.net.IOException;
import java.io.DataInputStream;
import java.io.DataOutoutStream;

import java.net.ServerSocket;
import java.net.socket;

import Java.util.concurrent.TimeUnit;

public Class TCPServer{
    public static void main(string[] args) throws IOException {
        int aPort = integer.parseInt(args[0]);
        system.out.println("Port Number: "+ aPort);
        ServerSocket ss = new ServerSocket(aPort);
    while (true){
        try{
            socket s = ss.accept(); //establish connection
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());

            system.out.println("target IP: "+ s.getInetAddress() + " Traget Port: "+ s.getPort());
            System.out.println("Local IP:"+ s.getLocalAddress() + "Local Port: " + s.getLocalPort());
            try {TimeUnit.SECONDS.sleep(10);} catch(InterruptedException e){System.out.println(e);}

            String str = (String)din.readUTF();
            System.out.println("RCVD: "+str);

            dout.writeUTF("G'DAY");
            System.out.println("SENT: G'DAY");

            str = (String)sin.readUTF();
            system.out.println("RCVD: "+str);

            dout.writeUTF("BYE");
            System.out.println("SENT: BYE");
            
            din.close();
            dout.close();
            s.close();
        }
        catch(Exception e){System.out.println(e);}
    }
    }
}