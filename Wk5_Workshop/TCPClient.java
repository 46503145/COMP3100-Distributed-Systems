// import Java.util.concurrent.TimeUnit;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class TCPClient{
    public static void main(String[] args){
        while(true){
            try{
                //InetAddress aHost = InetAddress.getByName(args[0]);
                //int aPort = Integer.ParseINt(args[1]);
                //Socket s = new Socket(aHost, aPort);

                Socket s = new Socket("127.0.0.1", 50000);

                DataOutputStream dout = new DataOutputStream(s.getOutputStream());

                DataInputStream din = new DataInputStream(s.getInputStream());

                String str;
                //BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));

                //System.out.println("Target IP:" + s.getInetAddress() + "Target Port: "+ s.getPort());
                //System.out.println("Local IP: " + s.getLocalAddress() + "Local Port "+ s.getLocalPort());
                
                //try{TimeUnit.SECONDS.sleep(10);}catch(InterruptedException e){System.out.println(e);}

              
                //System.out.println("SENT: HELO");

                System.out.println("SENT: GETS ALL");
  
                dout.write(("GETS All\n").getBytes());
                dout.flush();

                str = din.readLine();
                System.out.println("RCVD: "+ str);
                String[] serverInfoList = str.split(" ");

                System.out.println("SENT: OK");
                dout.write(("OK\n").getBytes());
                dout.flush();

                int serverNumber = Integer.parseInt(serverInfoList[1]);
                System.out.println("Server number: " + serverNumber);

                for (int i =0; i< serverNumber; i++){
                    str = din.readLine();
                    System.out.println("RCVD: "+ str);
                }

                din.close();
                dout.close();
                s.close();
                
            }
            catch(Exception e){System.out.println(e);}
           // try{TimeUnit.SECONDS.sleep(1);} catch(InterruptedException e){System.out.println(e);}
        } //
    }
}
