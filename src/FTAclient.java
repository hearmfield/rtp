import java.io.*;
import java.net.*;
//import org.apache.*;
/**
 * 
 * @author Andrew Ford
 *
 */
public class FTAclient {
	//private static String directory = "C:\\Users\\Andrew Ford\\Documents\\Eclipse\\ReliableTransportProtocol\\src\\";
	private static String netEmuIP;// = null;
	private static String netEmuPort;// = null;
	private static String clientPort;// = null;
	private static String filename;// = null;
	private static File file;// = null;
	private static InetAddress host;// = null;
	private static RTPclient rtpc;
	private static int windowSize;
	
	public static void main(String[] args) throws Exception{
		System.out.println("-----FTA Client is Running------");
		//boolean run = true;
		
		//DatagramSocket sock = null;
		
		try{		
			System.out.println(args.length);
			//while(run){
				
				//this is UDP needs to be in the RTP class
				//sock = new DatagramSocket();
				//sock.setSoTimeout(5000);//change later
				//Declare Variables to be interpreted
				//The first thing that we test is whether or not the input is even valid then we can start making decisions
				
				
				//I need to make sure and check that I have the IP and ports before I begin the file transfer
				//System.out.println(args.length);
				
				if(args.length == 3){
					clientPort = args[0];
					if(Integer.parseInt(clientPort) % 2 == 0){//it is even
						netEmuIP = args[1];
						netEmuPort = args[2];
						rtpc = new RTPclient(netEmuIP, netEmuPort, clientPort);
					}
					else{
						//add a try again statement
						System.out.println("The client port number is odd and it needs to be even");
						//run = false;
						//break;
					}
					//This is done in the client for RTP, we cannot connect with UDP on this level yet
		            //sock = new DatagramSocket();
		            //sock.setSoTimeout(3000);
		            host = InetAddress.getByName(netEmuIP);			
		            //end rtpclient
	            	waitForCommand();
	            	//int response = waitForCommand
	            	//translateMessage(response);
				}
				else{
					System.out.println("You have not made a valid entry. Please re-input your command.");
					//run = false;
					//main(args);
				}
	        	
		}
		finally{
			//this should prompt the user to enter another command if we need to check for cases
			System.out.println("here");
		}
				
	}//end main

	private static void waitForCommand() throws Exception {
		
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the command (connect-get *filename):   ");
		String input = userInput.readLine();
		String[] args = input.split(" ");
		boolean run = true;
		
		while(run){
			if(args.length == 2){//this is a check to make sure that we have done the XAP command already
				if(args[0].equalsIgnoreCase("connect-get")){
					filename = args[1];
					System.out.println(filename);
					//Hailey can help since she got a file to transfer
					file = new File(filename);//change depending on what the file is in the folder directory + 
					if(!file.exists()){
						System.out.println("THE FILE: ("+ filename + ") exists.");
						System.out.println("Retrieving File: " + filename + " from server...");
						System.out.println("Connecting to RTP client...");
						
						//add implementation for retrieving file from FTA server
						//FTAserver ftaS = new FTAserver();
						
						
						rtpc.downloadFromServer(filename);
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
						run = false;
					}	
					else{
						System.out.println("File does not exist in the provided directory");
						//try again function if file does not exist
						run = false;
					}
					//The client needs to disconnect from the server for UNIDIRECTIONAL
				}
				else if(args[0].equalsIgnoreCase("window")){
					windowSize = Integer.parseInt(args[1]);
					rtpc.setWindowSize(windowSize);
				}
				else{
					System.out.println("Incorrect command: please enter another command.");
					input = userInput.readLine();
					args = input.split(" ");
				}
			}
			/*
			else if(args.length == 3){
				clientPort = args[0];
				if(Integer.parseInt(clientPort) % 2 == 0){//it is even
					netEmuIP = args[1];
					netEmuPort = args[2];
					//host = InetAddress.getByName(netEmuIP);	
					//add catch for last two varibles to make sure that they are valid
					//RTPclient client = new RTPclient(clientPort, netEmuIP, netEmuPort);
					//System.out.println(clientPort + netEmuIP + netEmuPort);
				}
				else{
					//add a try again statement
					System.out.println("The client port number is odd and it needs to be even");
					run = false;
					//break;
				}
				//This is done in the client for RTP, we cannot connect with UDP on this level yet
	            //sock = new DatagramSocket();
	            //sock.setSoTimeout(3000);
	            host = InetAddress.getByName(netEmuIP);			
	            //end rtpclient
	        	
			}
			*/
			else{
				System.out.println("Enter a command: ");
				input = userInput.readLine();
				args = input.split(" ");
				//main(args);
			}
		}
	}
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
