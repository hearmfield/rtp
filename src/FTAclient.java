import java.io.*;
import java.net.*;

/**
 * 
 * @author Andrew Ford
 *
 */
public class FTAclient {
	private static String netEmuIP;
	private static String netEmuPort;
	private static String clientPort;
	private static String filename;
	private static RTPclient rtpc;
	private static int windowSize;
	
	public static void main(String[] args) throws Exception{
		System.out.println("-----FTA Client is Running------");		
				if(args.length == 3){
					clientPort = args[0];
					if(Integer.parseInt(clientPort) % 2 == 0){
						netEmuIP = args[1];
						netEmuPort = args[2];
						rtpc = new RTPclient(netEmuIP, netEmuPort, clientPort);
					}
					else
						System.out.println("The client port number is odd and it needs to be even");        
	            	waitForCommand();
				}
				else{
					System.out.println("You have not made a valid entry. Please re-input your command.");
					waitForCommand();
				}
	}

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
					System.out.println("Retrieving File: " + filename + " from server...");
			
					rtpc.downloadFromServer(filename);
					System.out.println("File Transfer Completed. File: " + filename + " has been retrieved.");
					run = false;
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

			else{
				System.out.println("Enter a command: ");
				input = userInput.readLine();
				args = input.split(" ");
			}
		}
	}
}
