import java.io.*;
import java.net.*;
import java.util.Random;
/**
 * This class represents the client for RTP
 * 
 * @author Hailey Armfield
 *
 */
public class RTPclient
{
/**
 * This class is the client file for udp. It reads in information from the input arguments. 
 * It passes in the host name and port number to communicate with the server. This client 
 * sends the input numbers and string in datagram packets to the server and receives the answer
 * in bytes from the server.
 * @param args
 * @throws SocketException
 */
	String netEmuIp;
	String netEmuPort;
	String filename;
	String clientPort;
	
	int windowSize;
	int timeout = 3000;
	InetAddress emulatorIP;
	DatagramSocket clientSocket = null;
	boolean connectionUp = false;
    //public static void main(String args[]) throws SocketException

	public RTPclient(String netEmuIp, String netEmuPort, String clientPort) throws Exception{
		System.out.println("Setting up RTPclient...");

		this.netEmuIp = netEmuIp;
		this.netEmuPort = netEmuPort;
		this.clientPort = clientPort;
		//this.filename = filename;
		this.connectionUp = true;
		
	}
	public int downloadFromServer(String file) throws Exception{
		filename = file;
		System.out.println("download");
		//if download was successful then we return a 1 to the FTA client, 0 otherwise
		setUp();
		return 0;//return status of the download loss packets? duplicate? more to come
	}
	public void setWindowSize(int windowSize){
		this.windowSize = windowSize;
	}
	public void setUp() throws Exception
    {
		
    	//creates a socket for the connection
    	clientSocket = new DatagramSocket();   
    	clientSocket.setSoTimeout(timeout);//timeout for acks
    	
    	emulatorIP = InetAddress.getByName(netEmuIp);
        try
        {
        	//while(connectionUp){
        		//this will send the packet file
                byte[] b = filename.getBytes();
                DatagramPacket  dPacket = new DatagramPacket(b , b.length , emulatorIP , Integer.parseInt(clientPort));
                clientSocket.send(dPacket);
                StringBuffer received = new StringBuffer("");

                //this will recieve the responce from the server
                int numPackets = 0;
                while(numPackets < 5){
	                byte[] buffer = new byte[6];
	                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
	                clientSocket.setSoTimeout(2000);
	                clientSocket.receive(reply);   
	                String rec = new String(reply.getData());
	                received.append(rec);
	                numPackets++;
	                System.out.println(rec);
	                //byte[] data = reply.getData();
                }
                String s = received.toString();
                Writer writer = new FileWriter(filename);
                writer.write(s);
                writer.close();
                //String received = new String(data, 0, reply.getLength());
                
                //createPacket(newPacket, input_data);
               
                //There needs to be a function that updates all the acks recieved and so on. 
                //connectionUp needs to be updated to know when to terminate the program
                
                // output from the server
                System.out.println("From server: " + received);
                clientSocket.close();	
        	//}
        }
        // catches if host is not valid
        catch(UnknownHostException e) {
            //System.out.println("UnknownHostException: " + e);
        	System.err.println("Invalid Command");
        	clientSocket.close();	
        }
        
        /**
         * This catch statement is what is read when the timer times out. The users chooses whether
         * they want to retry or exit.
         * 
         */
        catch(SocketTimeoutException e){
        	BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        	System.out.println("The server has not answered in 2 seconds.\n " +
        			"Enter 'retry' to resend the message or 'exit' to exit the application: \n");
        			try {
						String ui = userInput.readLine();
						if(ui.equalsIgnoreCase("retry")){
							
						}
						else if(ui.equalsIgnoreCase("exit")){
							clientSocket.close();
						}
					} catch (IOException e1) {
						
						System.err.println("Invalid Command");
						clientSocket.close();	
					}
        }
        
        catch(IOException e)
        {
        	System.err.println("Invalid Command");
        	clientSocket.close();	
        }
        
        catch(NumberFormatException e){
        	System.err.println("Invalid Command");
        	clientSocket.close();	
      	  
        }
    }
 

    
    /**
     * Creates the connection establishment through the three way handshake
     */
    public static void connEstablishment(){
    	//Create packet
    	 //RTPpacket packet = new RTPpacket();
    	 //packet.header[0] = packet.header[0] >> 7;
                
    	
    	//Send syn
    	
    	
    	//wait for ack+syn
    	 //packet.header[0] bit 0 is syn bit 1 is the ack
    	
    	//send ack
    	 //packet.header[0] turn off bit 0 and syn = 1
    	


    }
    /*
    public void createChecksum(){
    	int checksumAcc = 0;
    	int checksumPointer = 0;
    	
    	
    }
    */
    /**
     * Generates the initial sequence number for a packet
     * 
     * @return int
     */
    public static int isn(){
    	// randomly generate a 32bit number 
    	Random r = new Random();
    	 return r.nextInt();
    }
    
    
}


/**
import java.io.*;
import java.net.*;
//import org.apache.*;

public class FTAclient {
	
	public static void main(String[] args) throws Exception{
		System.out.println("-----FTA Client is Running------");
		//DatagramSocket sock = null;
		
		try{			
			while(true){
				
				//this is UDP needs to be in the RTP class
				//sock = new DatagramSocket();
				//sock.setSoTimeout(5000);//change later
				//Declare Variables to be interpreted
				String inputFromUser;
				String[] results;
				
				String netEmuIP = null;
				String netEmuPort = null;
				String clientPort = null;
				String filename = null;
				File file = null;
				InetAddress host = null;
				//The first thing that we test is whether or not the input is even valid then we can start making decisions
				inputFromUser = args[0];
				results = inputFromUser.split(" ");
				
				//I need to make sure and check that I have the IP and ports before I begin the file transfer
				
				if(results.length == 2){//this is a check to make sure that we have done the XAP command already
					if(results[0].equalsIgnoreCase("connect-get")){
						filename = results[1];
						
						//Hailey can help since she got a file to transfer
						file = new File("local_" + filename);//change depending on what the file is in the folder
						if(file.exists()){
							System.out.println("Cannot write to disk");
						}	
						
						DatagramPacket connect = null;
						DatagramPacket recieved = null;
						
						System.out.println("Retrieving File: " + filename + " from server...");
						System.out.println("Connecting to RTP client...");
						
						//add implementation for retrieving file from FTA server
						//FTAserver ftaS = new FTAserver();
						
						RTPclient rtpc = new RTPclient(netEmuIP, netEmuPort, clientPort);
						transferStatus(rtpc.downloadFromServer(file));
						/*
						int filesize = 65535; 
						int bytesRead; 
						int numBytesRead = 0; 
						
						Socket socket = new Socket(netEmuIP, Integer.parseInt(netEmuPort)); 
						
						byte [] buffer = new byte [filesize]; 
						//this is Tcp
						InputStream is = socket.getInputStream(); 
						FileOutputStream fos = new FileOutputStream(filename); 
						BufferedOutputStream bos = new BufferedOutputStream(fos); 
						bytesRead = is.read(buffer,0,buffer.length); 
						numBytesRead = bytesRead; 
						//end of TCP
						do{ 
							bytesRead = is.read(buffer, numBytesRead, (buffer.length-numBytesRead)); 
							if(bytesRead >= 0) 
								numBytesRead += bytesRead; 
						} 
						while(bytesRead > -1); 
						
						bos.write(buffer, 0 , numBytesRead); 
						
						bos.flush(); 
						bos.close(); 
						socket.close();
						
						System.out.println("Done. File: " + filename + " retrived.");
						
						//The client needs to disconnect from the server for UNIDIRECTIONAL
					}
					else{
						System.out.println("Incorrect command: please enter another command.");
					}
				}
				else if(results.length == 3){
					clientPort = results[0];
					if(Integer.parseInt(clientPort) % 2 == 0){//it is even
						netEmuIP = results[1];
						netEmuPort = results[2];
						//host = InetAddress.getByName(netEmuIP);	
						//add catch for last two varibles to make sure that they are valid
						//RTPclient client = new RTPclient(clientPort, netEmuIP, netEmuPort);
					}
					else{
						//add a try again statement
						System.out.println("The client port number is odd and it needs to be even");
						break;
					}
					//This is done in the client for RTP, we cannot connect with UDP on this level yet
		            //sock = new DatagramSocket();
		            //sock.setSoTimeout(3000);
		            host = InetAddress.getByName(netEmuIP);			
		            //end rtpclient
				}
				else{
					System.out.println("You have not made a valid entry. Please re-input your command.");
					//main(args);
				}
			}
		}
		catch(SocketException e){
			System.out.println("Unable to open the socket");
			System.exit(1);
		}
	}//end main

	private static void transferStatus(int message) {
		if(message == 1){
			//we got a successful transfer
		}
		else{
			//we failed
		}
		
		
	}
}//end class

/**
 * Command: 		connect-get F  (only for projects that do NOT support bi-directional transfers)
This command does two things. First, the FTA-client connects to the FTA-server and, if the connection is successfully completed, 
the client downloads file F from the server (if F exists in the same directory that the fta-server executable is stored at). 
If you only support uni-directional transfers (from the server to the client), the client should disconnect from the server at 
the end of the transfer.   
 */

/**
FTA CLIENT
Command-line: 	fta-client X A P
The command-line arguments are:
X: the port number at which the fta-client’s UDP socket should bind to (even number). 
	Please remember that this port number should be equal to the server’s port number minus 1.
A: the IP address of NetEmu
P: the UDP port number of NetEmu

Command: 		connect (only for projects that support bi-directional transfers)
The FTA-client connects to the FTA-server (running at the same IP host). 
Command: 		get F (only for projects that support bi-directional transfers)
The FTA-client downloads file F from the server (if F exists in the same directory as the fta-server executable).
Command: 		post F (only for projects that support bi-directional transfers)
The FTA-client uploads file F to the server (if F exists in the same directory as the fta-client executable).
Command: 		window W (only for projects that support pipelined transfers)
W: the maximum receiver’s window-size at the FTA-Client (in segments).
Command: 		disconnect (only for projects that support bi-directional transfers)
The FTA-client terminates gracefully from the FTA-server. 
*/
