// import Java.util.concurrent.TimeUnit;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

      
public class TCPClient{
    
    public void sendMsg(String rawMsgStr){
        String msgWthEndTag = rawMsgStr + "\n";
        try{
            out.write(msgWthEndTag.getBytes());
            out.flush();
            // System.out.println("SEND: "+ rawMsgStr);
        }catch (Exception e ){
            // System.out.println("Communicaiton with server did not succeed");
        }

    }

    public String receiveResp(){
        String ActualResp = " ";
        try {
            ActualResp = in.readLine();
            // System.out.println("RCVD: " + ActualResp);
        }catch (Exception  e){
            ActualResp = "ERR";
            // System.out.println("Something went wrong when sending or receiving message");
        }
        return ActualResp;
    }

    private void parstjob(String jobMsg){
        String[] jobInfo = jobMsg.split(" ");
         jobCreateTime = Integer.parseInt(jobInfo[1]);
         jobID = Integer.parseInt(jobInfo[2]);
         jobEstimate = Integer.parseInt(jobInfo[3]);
         jobCore = Integer.parseInt(jobInfo[4]);
        
    }
    
    
    public static void main(String[] args){
             
        while(true){
            
            try{
                
                //InetAddress aHost = InetAddress.getByName(args[0]);
                //int aPort = Integer.ParseINt(args[1]);
                //Socket s = new Socket(aHost, aPort);

                Socket s = new Socket("localhost", 50000);
                // DataInputStream din = new DataInputStream(s.getInputStream());

                String str;
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                

                //System.out.println("Target IP:" + s.getInetAddress() + "Target Port: "+ s.getPort());
                //System.out.println("Local IP: " + s.getLocalAddress() + "Local Port "+ s.getLocalPort());
                
                //try{TimeUnit.SECONDS.sleep(10);}catch(InterruptedException e){System.out.println(e);}

              
                //System.out.println("SENT: HELO");
                out.write(("HELO" + "\n").getBytes());
                str = in.readLine();
                System.out.println("RCVD: "+ str);

                out.write(("GETS All\n").getBytes());
                out.flush();
                System.out.println("SENT: GETS ALL");

                str = in.readLine();
                System.out.println("RCVD: "+ str);
                String[] serverInfoList = str.split(" ");

                System.out.println("SENT: OK");
                out.write(("OK\n").getBytes());
                out.flush();

                int serverNumber = Integer.parseInt(serverInfoList[1]);
                System.out.println("Server number: " + serverNumber);

                for (int i =0; i< serverNumber; i++){
                    str = in.readLine();
                    System.out.println("RCVD: "+ str); 
                }
                
                out.write(("REDY\n").getBytes());
                out.flush();
                str = in.readLine();
                System.out.println("RCVD: "+ str);
                if (str.equals("NONE"))
                    break;
                else{
                    String[] msg = str.split(" ");
                    if (msg[0].equals("JCPL")){
                        System.out.println("RCVD: "+ str);
                    }
                    else{
                        jobInfo = str.split(" ");
                        jobID = Integer.parseInt(jobInfo[2]);
                        System.out.println("SEND: SCHD "+ jobID+ " "+ serverType+ " ")
                        out.flush();
                        str = in.readLine();
                        System.out.println("RCVD: "+ str);
                    }
                }

                System.out.println("SEND: QUIT");
                out.write(("QUIT\n").getBytes());
                out.flush();
                str = in.readLine();
                System.out.println("RCVD: "+ str);

                in.close();
                out.close();
                s.close();
                
            }
            catch(Exception e){System.out.println(e);}
           // try{TimeUnit.SECONDS.sleep(1);} catch(InterruptedException e){System.out.println(e);}
        } //
    }
}
