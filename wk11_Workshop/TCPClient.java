// import Java.util.concurrent.TimeUnit;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;


      
public class TCPClient{

    private static Socket s;
    private static String str;
    private static BufferedReader in;
    private static DataOutputStream  out;  
    
    public static void main(String[] args){
           
            try{
                 s = new Socket("localhost", 50000);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new DataOutputStream(s.getOutputStream());

                 sendMessage("HELO");
                 receiveMessage();
                 sendMessage("AUTH can46503145");
                 receiveMessage();
 
                while (true){

               sendMessage("REDY");
               receiveMessage();
               
                if (str.equals("NONE"))
                    break;
                else{
                    String[] msg = str.split(" ");
                    if(msg[0].equals("JCPL")){
                        System.out.println("RCVD: "+ str);
                    }
                    else{
                        
                        String[] jobInfo = str.split(" ");
                        int jobID = Integer.parseInt(jobInfo[2]);
                        int jobCore = Integer.parseInt(jobInfo[4]);
                        int jobMemory = Integer.parseInt(jobInfo[5]);
                        int jobDisk = Integer.parseInt(jobInfo[6]);
                        
                       sendMessage("GETS Capable "+jobCore+" "+jobMemory+" "+jobDisk); 
                       receiveMessage();
                        
                        String[] serverInfoList = str.split(" "); // nrecs so we get the number of records

                        int serverNumber  = Integer.parseInt(serverInfoList[1]);
                        System.out.println("Server number: "+ serverNumber);
                        
                        out.write(("OK"+"\n").getBytes());
                        out.flush();
                          
                        String serverType = "";
                        int serverId = 0;
                        int serverCore = 0;
                        int serverMemory =0;
                        int serverDisk =0;
                        int typeNumber=0; 
                        //String serverState = "";
                        //int serverCurStartTime = -1;
                         boolean flag = true;
                        
                        for (int i =0; i < serverNumber; i++){
                           receiveMessage();
                           if(flag){
                                String[] serverInfo = str.split(" ");
                            if (Integer.parseInt(serverInfo[4]) > serverCore){
                                serverType = serverInfo[0];
                                serverId = Integer.parseInt(serverInfo[1]);
                                //serverState = serverInfo[2];
                                //serverCurStartTime = Integer.parseInt(serverInfo[3]);
                                serverCore = Integer.parseInt(serverInfo[4]);
                                serverMemory = Integer.parseInt(serverInfo[5]);
                                serverDisk = Integer.parseInt(serverInfo[6]);
                            }else if((Integer.parseInt(serverInfo[4]) == serverCore && Integer.parseInt(serverInfo[5]) > serverMemory)){
                                    serverType = serverInfo[0];
                                    serverId = Integer.parseInt(serverInfo[1]);
                                   // serverState = serverInfo[2];
                                    //serverCurStartTime = Integer.parseInt(serverInfo[3]);
                                    serverCore = Integer.parseInt(serverInfo[4]);
                                    serverMemory = Integer.parseInt(serverInfo[5]);
                                    serverDisk = Integer.parseInt(serverInfo[6]);
                            }else  if((Integer.parseInt(serverInfo[4]) == serverCore && Integer.parseInt(serverInfo[5]) == serverMemory && Integer.parseInt(serverInfo[6]) > serverDisk)){
                                serverType = serverInfo[0];
                                serverId = Integer.parseInt(serverInfo[1]);
                               // serverState = serverInfo[2];
                               // serverCurStartTime = Integer.parseInt(serverInfo[3]);
                                serverCore = Integer.parseInt(serverInfo[4]);
                                serverMemory = Integer.parseInt(serverInfo[5]);
                                serverDisk = Integer.parseInt(serverInfo[6]);
                            }
                            
                                if(serverType.equals(serverInfo[0])){
                                  typeNumber+=1;
                                }

                             
                       
                        }else{
                           ;
                        }
                        }
                        flag = false;
                        
                        System.out.println("Largest Server: " + "Type: "+ serverType + " serverId: " + serverId+ " serverCore: "+ serverCore);
                        
                        sendMessage("OK");
                        receiveMessage();
                        
                        serverId = serverId % typeNumber;
                        sendMessage("SCHD "+jobID+" "+ serverType+ " "+ serverId);
                        receiveMessage();
                        
                    }
                    
                }
            }
                sendMessage("QUIT");
                receiveMessage();
                in.close();
                out.close();
                s.close();
                
               
        }
            catch(Exception e){System.out.println(e);}
           
            
    }

    private static void sendMessage(String message) throws Exception{
        out.write((message + "\n").getBytes());
        out.flush();
        System.out.println("SEND: "+ message);
    }

    private static void receiveMessage() throws Exception{
        str = in.readLine();
        System.out.println("RCVD: " + str);
        
    }

    

    
}
