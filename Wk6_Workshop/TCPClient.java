// import Java.util.concurrent.TimeUnit;
import java.net.Socket;

import javax.swing.plaf.TreeUI;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;


      
public class TCPClient{
    public static void main(String[] args){
                  
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
                out.flush();
                System.out.println("SEND: HELO");

                str = in.readLine();
                System.out.println("RCVD: "+ str);

                out.write(("AUTH can46503145"+ "\n").getBytes());
                out.flush();
                System.out.println("SEND: AUTH");
                str = in.readLine();
                System.out.println("RCVD: "+str);

                
                
                while (true){

                out.write(("REDY"+ "\n").getBytes());
                out.flush();

                System.out.println("SEND: REDY");

                str = in.readLine();
                System.out.println("RCVD: "+str);

                if (str.equals("NONE"))
                    break;
                else{
                    String[] msg = str.split(" ");
                    if(msg[0].equals("JCPL")){
                        System.out.println("RCVD: "+ str);
                    }
                    else{
                        String[] jobInfo = str.split(" ");
                        int jobID;
                        int jobCore;
                        int jobMemory;
                        int jobDisk;
                        jobInfo = str.split(" ");
                        jobID = Integer.parseInt(jobInfo[2]);
                        jobCore = Integer.parseInt(jobInfo[4]);
                        jobMemory = Integer.parseInt(jobInfo[5]);
                        jobDisk = Integer.parseInt(jobInfo[6]);
                        
                        out.write(("GETS Capable "+jobCore+" "+jobMemory+" "+jobDisk +"\n").getBytes());
                        out.flush();
                        System.out.println("SEND: GETS Capable");

                        str = in.readLine();
                        System.out.println("RCVD: "+str);
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
                        String serverState = "";
                        int serverCurStartTime = -1;
                        boolean flag = true;
                        
                        for (int i =0; i < serverNumber; i++){
                            str = in.readLine();
                            System.out.println("RCVD: "+str);
                           if(flag){
                                String[] serverInfo = str.split(" ");
                            if (Integer.parseInt(serverInfo[4]) > serverCore){
                                serverType = serverInfo[0];
                                serverId = Integer.parseInt(serverInfo[1]);
                                serverState = serverInfo[2];
                                serverCurStartTime = Integer.parseInt(serverInfo[3]);
                                serverCore = Integer.parseInt(serverInfo[4]);
                                serverMemory = Integer.parseInt(serverInfo[5]);
                                serverDisk = Integer.parseInt(serverInfo[6]);
                            }else // else if (Integer.parseInt(serverInfo[4]) == serverCore && !serverType.equals(serverInfo[0])) {
                               // serverType = serverInfo[0];
                               // typeNumber = 1;}
                                if((Integer.parseInt(serverInfo[4]) == serverCore && Integer.parseInt(serverInfo[5]) > serverMemory)){
                                    serverType = serverInfo[0];
                                    serverId = Integer.parseInt(serverInfo[1]);
                                    serverState = serverInfo[2];
                                    serverCurStartTime = Integer.parseInt(serverInfo[3]);
                                    serverCore = Integer.parseInt(serverInfo[4]);
                                    serverMemory = Integer.parseInt(serverInfo[5]);
                                    serverDisk = Integer.parseInt(serverInfo[6]);
                            }else  if((Integer.parseInt(serverInfo[4]) == serverCore && Integer.parseInt(serverInfo[5]) == serverMemory && Integer.parseInt(serverInfo[6]) > serverDisk)){
                                serverType = serverInfo[0];
                                serverId = Integer.parseInt(serverInfo[1]);
                                serverState = serverInfo[2];
                                serverCurStartTime = Integer.parseInt(serverInfo[3]);
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
                        System.out.println("SEND:OK");
                        out.write(("OK"+"\n").getBytes());
                        out.flush();
                        str = in.readLine();
                        System.out.println("RCVD: "+ str);
                        
                        
                        serverId = serverId % typeNumber;
                        System.out.println("SEND: SCHD "+jobID+" "+ serverType+ " "+ serverId);
                        out.write(("SCHD "+jobID+" "+ serverType+ " "+ serverId+"\n").getBytes());
                        out.flush();
                        str = in.readLine();
                        
                    }
                    
                }
            }
                out.write(("QUIT\n").getBytes());
                out.flush();
                System.out.println("SEND: QUIT");
                str = in.readLine();
                System.out.println("RCVD: "+ str);

                in.close();
                out.close();
                s.close();
                
            
        }
            catch(Exception e){System.out.println(e);}
           // try{TimeUnit.SECONDS.sleep(1);} catch(InterruptedException e){System.out.println(e);}
            
    }
}
