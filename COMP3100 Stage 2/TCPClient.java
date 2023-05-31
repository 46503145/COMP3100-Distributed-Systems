// import Java.util.concurrent.TimeUnit;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.*;


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
                        
                    
                         boolean flag = true;
                        
                        ArrayList<String[]> serverList = new ArrayList<>(); //to assign the list of servers
                        for (int i =0; i < serverNumber; i++){
                            receiveMessage();
                            String[] serverInfo = str.split(" ");
                            serverList.add(serverInfo); //adding servers to the list
                        }
                      
                        Collections.sort(serverList, new Comparator<String[]>() { //order the list in ascending order
                            @Override
                            public int compare(String[] s1, String[] s2) {
                                return Integer.parseInt(s1[4]) - Integer.parseInt(s2[4]);
                            }
                        });
                       
                        for (String[] server : serverList) { //itrate trough the list untill find a server that fits 
                            serverType = server[0];
                            serverId = Integer.parseInt(server[1]);
                     
                            int serverCores = Integer.parseInt(server[4]);
                            int serverMemory = Integer.parseInt(server[5]);
                            int serverDisk = Integer.parseInt(server[6]);
                       
                            if (serverCores >= jobCore && serverMemory >= jobMemory && serverDisk >= jobDisk) {
                                break;
                            }
                        }
                     
                        sendMessage("OK");
                        receiveMessage();
                        
                 
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
