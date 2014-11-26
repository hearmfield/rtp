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
		
		String serverPort;	
		String netEmuIP;
		String netEmuPort;
		int windowSize = 1;
		RTPserver rtps = new RTPserver();
		boolean running = true;
		BufferedReader userInput;
		String input;
		
		try{
			while(running){
				if(args.length == 3){
					if(checkValidPort(Integer.parseInt(args[0])) == true){
						serverPort = args[0];
						if(Integer.parseInt(serverPort) % 2 == 1){
							netEmuIP = args[1];
							netEmuPort = args[2];
							rtps = new RTPserver(netEmuIP, netEmuPort, Integer.parseInt(serverPort));
							System.out.println("Connecting to RTP Server : "); 
							rtps.setWindowSize(windowSize);
							rtps.listen();
							running = false;
						}
					}
					else if(checkValidPort(Integer.parseInt(args[0])) == false){
						throw new NumberFormatException("Potential Port Number is not in range.");
					}	
				}
				else if(args.length == 2){
					if(args[0].equalsIgnoreCase("window")){
						windowSize = Integer.parseInt(args[1]);
						System.out.println("Window of size: " + windowSize);
					}
					System.out.println("Enter a command: ");
					userInput = new BufferedReader(new InputStreamReader(System.in));					
					input = userInput.readLine();
					args = input.split(" ");
				}
				else if(args.length == 1){
					if(args[0].equalsIgnoreCase("terminate")){
						rtps.terminate();
						System.out.println("Done.");
						break;
					}
				}
				else{
					System.out.println("Do Nothing");

				}
			}//end of while loop
		}//end try
		catch(NumberFormatException e){
			e.printStackTrace();
		}
        catch(IOException e){
            e.printStackTrace();
            
        }//end try catch
        
	}//end main
	private static boolean checkValidPort(int potentialPortNum) {
    	
		if(potentialPortNum < 0 || potentialPortNum > 65535){
			return false;
		}
		else{
			return true;
		}
	}
}
