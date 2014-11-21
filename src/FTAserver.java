import java.io.*;
import java.net.*;
/**
 * 
 * @author Andrew Ford
 *
 */
public class FTAserver {
	
	public static void main(String[] args) throws Exception{
		System.out.println("-----FTA Server is Running------");
		
		//DatagramSocket sock = null;
		//DatagramPacket answer;
		//String answer;
		String input;// = args[0];
		// = inFromUser.split(" ");
		
		String serverPort;	
		String netEmuIP;
		String netEmuPort;
		//int maxWindowSize;
		System.out.println(args.length);
		RTPserver rtps;
		try{
			while(true){
				//Based on the input from the command line we will figure out what the message will be.
				//somehow this transfer file will be specified by the client, so those parameters will be the deciding factor
				//We need to check somewhere to see if the port is client port +1 and its also odd
				if(args.length == 3){
					//Check if it is a valid port number
					if(checkValidPort(Integer.parseInt(args[0])) == true){
						//if it enters this then from what we know so far the command that was entered was the X A P
						
						serverPort = args[0];
						
						if(Integer.parseInt(serverPort) % 2 == 0){//check that server port is odd
							//check that there is 2 more elements in the user input
							netEmuIP = args[1];
							netEmuPort = args[2];
							rtps = new RTPserver("127.0.0.1", "4000", Integer.parseInt(serverPort));
							rtps.listen();
							//sock = new DatagramSocket(serverPort);//create socket and bind to serverPort

							System.out.println("Connecting to RTP Server : "); 
							
//							byte[] buffer = new byte[1024];//not sure about the size of the byte buffer
//							DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
//							sock.receive(incoming);
//							
//							byte[] data = incoming.getData();
//							if((incoming.getPort() + 1) == serverPort){//now we check to see if this is 1 more than the client port
//								//now we have the correct port for the server and the client
//								
//								//send file
//								
//								answer = new DatagramPacket(buffer, buffer.length, incoming.getAddress(), incoming.getPort());
//								sock.send(answer);
//							}
//							}
						}
				}
					/*
				else if(results.length == 1){
					if(results[0].equalsIgnoreCase("terminate")){
						rtps.terminate();
						System.out.println("Done.");
						break;
					}
					else if(results[0].equalsIgnoreCase("window")){
						//for pipelined projects
						maxWindowSize = Integer.parseInt(results[1]);
						
					}
				}
*/
				
				}
				else if(checkValidPort(Integer.parseInt(args[0])) == false){
					throw new NumberFormatException("Potential Port Number is not in range.");
				}
				/*
					if(serverPort % 2 == 1 && serverPort - clientPort = 1){
				
						//byte [] bytearray = new byte [(int)fileToClient.length()]; 
						
						File transferFile = new File (fileToClient);
						System.out.println("Sending Files..."); 
						os.write(bytearray,0,bytearray.length); 
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
						
						os.flush();
						socket.close();
						
						System.out.println("Done.");
						
					}
					else{
						System.out.println("Incorrect command: please enter another command.");
					}
				}
				else{
					System.out.println("You have not made a valid entry. Please re-input your command.");
				}
				 */
				
				
			}//end of while loop
		}
		catch(NumberFormatException e){
			e.printStackTrace();
		}
        catch(IOException e){
            e.printStackTrace();
            
        }//end try catch
	}//end main
    private static boolean checkValidPort(int potentialPortNum) {
    	System.out.println("lllllleeeee");
		if(potentialPortNum < 0 || potentialPortNum > 65535){
			return false;
		}
		else{
			return true;
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
