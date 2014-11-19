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
		DatagramSocket sock = null;
		
		try{
			sock = new DatagramSocket();
			sock.setSoTimeout(5000);//change later
		}
		catch(SocketException e){
			System.out.println("Unable to open the socket");
			System.exit(1);
		}
		
		//Declare Variables to be interpreted
		String inputFromUser;
		String[] results;
		
		String netEmuIP = null;
		String netEmuPort = null;
		String clientPort = null;
		String filename = null;
		
		while(true){
			//The first thing that we test is whether or not the input is even valid then we can start making decisions
			inputFromUser = args[0];
			results = inputFromUser.split(" ");
			
			if(results.length == 2){
				if(results[0].equals("connect-get")){
					filename = results[1];
					
					File file = new File("local_" + filename);
					if(file.exists()){
						System.out.println("Cannot write to disk");
					}	

					DatagramPacket connect = null;
					DatagramPacket recieved = null;
					
					System.out.println("Retrieving File: " + filename + " from server...");
					
					//add implementation for retrieving file from FTA server
					FTAserver ftaS = new FTAserver();
					
					int filesize = 65535; 
					int bytesRead; 
					int numBytesRead = 0; 
					
					Socket socket = new Socket(netEmuIP, Integer.parseInt(netEmuPort)); 
					
					byte [] buffer = new byte [filesize]; 
					InputStream is = socket.getInputStream(); 
					FileOutputStream fos = new FileOutputStream(filename); 
					BufferedOutputStream bos = new BufferedOutputStream(fos); 
					bytesRead = is.read(buffer,0,buffer.length); 
					numBytesRead = bytesRead; 
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
					
					//RTPclient client = new RTPclient(clientPort, netEmuIP, netEmuPort);
				}
				else{
					System.out.println("The client port number is odd and it needs to be even");
					break;
				}
				
	            sock = new DatagramSocket();
	            sock.setSoTimeout(3000);
	            InetAddress host = InetAddress.getByName(netEmuIP);
	            
	            /**
	             * You need to make sure that the client port number is even. So this is the check for that.
	             * Once this is checked, you can finally connect to the RTP client.
	             */
			
			}
			else{
				System.out.println("You have not made a valid entry. Please re-input your command.");
			}
		}	
	}


	
	
}

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
X: the port number at which the fta-client’s UDP socket should bind to (even number). Please remember that this port number should be equal to the server’s port number minus 1.
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