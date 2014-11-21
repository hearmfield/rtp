import java.io.*;
import java.net.*;
//import org.apache.*;
/**
 * 
 * @author Andrew Ford
 *
 */
public class FTAclient {
	
	public static void main(String[] args) throws Exception{
		System.out.println("-----FTA Client is Running------");
		boolean run = true;
		
		//DatagramSocket sock = null;
		
		try{			
			while(run){
				
				//this is UDP needs to be in the RTP class
				//sock = new DatagramSocket();
				//sock.setSoTimeout(5000);//change later
				//Declare Variables to be interpreted

				
				String netEmuIP = null;
				String netEmuPort = null;
				String clientPort = null;
				String filename = null;
				File file = null;
				InetAddress host = null;
				//The first thing that we test is whether or not the input is even valid then we can start making decisions
				
				
				//I need to make sure and check that I have the IP and ports before I begin the file transfer
				//System.out.println(args.length);
				if(args.length == 2){//this is a check to make sure that we have done the XAP command already
					if(args[0].equalsIgnoreCase("connect-get")){
						filename = args[1];
						
						//Hailey can help since she got a file to transfer
						file = new File("local_" + filename);//change depending on what the file is in the folder
						if(file.exists()){
							System.out.println("Cannot write to disk");
						}	
						/*
						DatagramPacket connect = null;
						DatagramPacket recieved = null;
						*/
						System.out.println("Retrieving File: " + filename + " from server...");
						System.out.println("Connecting to RTP client...");
						
						//add implementation for retrieving file from FTA server
						//FTAserver ftaS = new FTAserver();
						
						RTPclient rtpc = new RTPclient(netEmuIP, netEmuPort, clientPort);
						transferStatus(rtpc.downloadFromServer(filename));
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
						*/
						System.out.println("Done. File: " + filename + " retrived.");
						
						//The client needs to disconnect from the server for UNIDIRECTIONAL
					}
					else{
						System.out.println("Incorrect command: please enter another command.");
					}
				}
				else if(args.length == 3){
					clientPort = args[0];
					if(Integer.parseInt(clientPort) % 2 == 0){//it is even
						netEmuIP = args[1];
						netEmuPort = args[2];
						//host = InetAddress.getByName(netEmuIP);	
						//add catch for last two varibles to make sure that they are valid
						//RTPclient client = new RTPclient(clientPort, netEmuIP, netEmuPort);
					}
					else{
						//add a try again statement
						System.out.println("The client port number is odd and it needs to be even");
						run = false;
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
					run = false;
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