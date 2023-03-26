import java.net.socket;
import java.net.Inet Address;
import java.io.DataInputStream;
import java.io.DataOutoutStream;
import Java.util.concurrent.TimeUnit;

public Class TCPClient{
    public static void main(string[] args){
        while(true){
            try{
                InetAddress aHost = InetAddress.getByName(args[0]);
                int aPort = Integer.ParseINt(args[1]);
                Socket s = new Socket(aHost, aPort);
                DataOutoutStream dout = new DataOutoutStream(s.getOutputStream());
                DataInputStream din = new DataInputStream(s.getInputStream());

                System.out.println("Target IP:" + s.getInetAddress() + "Target Port: "+ s.getPort());
                System.out.println("Local IP: " + s.getlocalAddress() + "Local Port "+ s.getLocalPort());
                try{TimeUnit.SECONDS.sleep(10);}catch(InterruptedException e){System.out.println(e);}

                dout.writeUTF("HELLO");
                System.out.println("SENT: HELO");

                String str = (String)din.readUTF();
                System.out.println("RCVD: "+str);
                dout.writeUTF("BYE");
                System.out.println("SENT: BYE");

                str = (String)din.readUTF();
                System.out.println("RCVD:"+str);

                din.close();
                dout.close();
                s.close();
                
            }
            catch(Exception e){System.out.println(e);}
            try{TimeUnit.SECONDS.sleep(1);} catch(InterruptedException e){System.out.println(e);}
        }
    }
}