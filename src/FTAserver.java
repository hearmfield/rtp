import java.io.*;
import java.net.*;
/**
 * 
 * @author Andrew Ford
 *
 */
public class FTAserver {
	
	public static void main(String[] args) throws IOException{
		System.out.println("-----FTA Server is Running------");
		
		ServerSocket sock = null;
		
		String answer;
		String inFromUser = args[0];
		String[] results = inFromUser.split(" ");
		
		try{
			while(true){
				//Based on the input from the command line we will figure out what the message will be.
				//somehow this transfer file will be specified by the client, so those parameters will be the deciding factor
				//We need to check somewhere to see if the port is client port +1 and its also odd
				if(results.length == 3){
					String netEmuIP, netEmuPort;
					int clientPort;
					clientPort = Integer.parseInt(args[0]);
					netEmuIP = args[1];
					netEmuPort = args[2];
					
					if(clientPort % 2 == 1){
						sock = new ServerSocket(clientPort);
						System.out.println("Accepted connection : " + sock); 
						//byte [] bytearray = new byte [(int)fileToClient.length()]; 
						
						//File transferFile = new File (fileToClient); 
						
						//FileInputStream fin = new FileInputStream(transferFile); 
						//BufferedInputStream bin = new BufferedInputStream(fin); 
						//bin.read(bytearray,0,bytearray.length); 
						//OutputStream os = socket.getOutputStream(); 
						System.out.println("Sending Files..."); 
						//os.write(bytearray,0,bytearray.length); 
						System.out.println("File transfer complete");
						//The port of the server has to be one more than the clients to therefore it needs to be odd.
					}
					else{
						System.out.println("The client port number is odd and it needs to be even");
					}
							
				}
				else if(results.length == 1){
					if(results[0].equals("terminate")){		
						System.out.println("Terminating the FTA server...");
						
						//os.flush();
						//socket.close();
						
						System.out.println("Done.");
						
					}
					else{
						System.out.println("Incorrect command: please enter another command.");
					}
				}
				else{
					System.out.println("You have not made a valid entry. Please re-input your command.");
				}

				
				
			}
		}
        catch(IOException e){
            e.printStackTrace();
        }
		
	}
}
/**
 * FTA SERVER
Command-line: 	fta-server X A P
The command-line arguments are:
X: the port number at which the fta-server’s UDP socket should bind to (odd number)
A: the IP address of NetEmu
P: the UDP port number of NetEmu
Command: 		window W (only for projects that support pipelined and bi-directional transfers)
W: the maximum receiver’s window-size at the FTA-Server (in segments).
Command: 		terminate
Shut-down FTA-Server gracefully.
*/