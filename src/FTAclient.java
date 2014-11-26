import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.management.timer.Timer;
//import org.apache.*;
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
	private static File file;
	private static InetAddress host;
	private static RTPclient rtpc;
	private static int windowSize;
	
	public static void main(String[] args) throws Exception{
		System.out.println("-----FTA Client is Running------");
		
		try{		
			if(args.length == 3){
				clientPort = args[0];
				if(Integer.parseInt(clientPort) % 2 == 0){
					netEmuIP = args[1];
					netEmuPort = args[2];
					rtpc = new RTPclient(netEmuIP, netEmuPort, clientPort);
				}
				else{
					System.out.println("The client port number is odd and it needs to be even");
				}
	            host = InetAddress.getByName(netEmuIP);		
            	waitForCommand();
			}
			else{
				System.out.println("You have not made a valid entry. Please re-input your command.");
			}
		}
		finally{
			System.out.println("here");
		}		
	}//end main

	private static void waitForCommand() throws Exception {
		ActionListener listener = new ActionListener();
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the command (connect-get *filename):   ");
		String input = userInput.readLine();
		String[] args = input.split(" ");
		boolean run = true;
		Timer timer = new Timer(0, listener);
		
		while(run){
			if(args.length == 2){
				if(args[0].equalsIgnoreCase("connect-get")){
					filename = args[1];
					System.out.println(filename);
					file = new File(filename);
					if(!file.exists()){
						System.out.println("Retrieving File: " + filename + " from server...");
						System.out.println("Connecting to RTP client...");
						timer.start();
						rtpc.downloadFromServer(filename);
						System.out.println(timer.readTimeInMillis());
						timer.stop();
						System.out.println("Done. File: " + filename + " retrived.");
						run = false;
					}	
					else{
						System.out.println("File does not exist in the provided directory");
						run = false;
					}
				}
				else if(args[0].equalsIgnoreCase("window")){
					windowSize = Integer.parseInt(args[1]);
					rtpc.setWindowSize(windowSize);
					System.out.println("Window Size of: " + windowSize);
					System.out.println("-----------------------");
					System.out.println("Enter a command: ");
					input = userInput.readLine();
					args = input.split(" ");
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
