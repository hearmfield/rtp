import java.io.*;
import java.net.*;
/**
 * This class represents the client for UDP.
 * 
 * @author Hailey Armfield
 *
 */
public class remotecalc_udp
{
/**
 * This class is the client file for udp. It reads in information from the input arguments. 
 * It passes in the host name and port number to communicate with the server. remotecalc_udp
 * sends the input numbers and string in datagram packets to the server and receives the answer
 * in bytes from the server.
 * @param args
 * @throws SocketException
 */
    public static void main(String args[]) throws SocketException
    {
    	DatagramSocket socketClient = new DatagramSocket();
    	
        try
        {
        	
            String []addressArr; 
            addressArr = args[0].split(":", 2);
            if (args.length != 4){
               	System.err.println("Invalid Command");
            }
                String input = "";
                for(int i = 1;i<args.length;i++){
                	if (i+1 == args.length){
                		input += args[i];
                	}
                	else
                		input += args[i] + " ";
                    
                }

                byte[] b = input.getBytes();
                
                InetAddress host = InetAddress.getByName(addressArr[0]); 
                int port = Integer.parseInt(addressArr[1]); 
                checkNum(port);
              
                //check to see if num1 is 2 bytes
                int num = Integer.parseInt(args[2]);
                checkNum(num);
                
                //check to see if num2 is 2 bytes
                num = Integer.parseInt(args[3]);
                checkNum(num);
                
                // check to see if string is valid argument
                checkString(args[1]);

                //send packet
                DatagramPacket  dp = new DatagramPacket(b , b.length , host , port);
                socketClient.send(dp);
               
                //buffer to receive reply
                byte[] buffer = new byte[64];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                //creates time out 
                socketClient.setSoTimeout(2000);
                // need to have statement to try again
                socketClient.receive(reply);
                byte[] data = reply.getData();
                String str = new String(data, 0, reply.getLength());
               
                // output from the server
                System.out.println("From server: " + str);
                socketClient.close();	
        }
        
        // catches if host is not valid
        catch(UnknownHostException e) {
            //System.out.println("UnknownHostException: " + e);
        	System.err.println("Invalid Command");
        	socketClient.close();	
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
							main(args);
						}
						else if(ui.equalsIgnoreCase("exit")){
							socketClient.close();
						}
					} catch (IOException e1) {
						
						System.err.println("Invalid Command");
						socketClient.close();	
					}
        }
        
        catch(IOException e)
        {
        	System.err.println("Invalid Command");
        	socketClient.close();	
        }
        
        catch(NumberFormatException e){
        	System.err.println("Invalid Command");
        	socketClient.close();	
      	  
        }
    }
 
    /**
     * Identifies where the numbers and port number are within the 2 byte range if 
     * the parameter is not within range an exception is thrown.
     * 
     * @param a
     */
    public static void checkNum(int a){
    	if (a<0 || a>65535){
        	//throw new NumberFormatException("Not in range");
    		System.err.println("Invalid Command");
        }
    }

    /**
     * Identifies whether the string that determines what type of computation to do
     * is a valid choice. 
     * 
     * @param str
     */
    public static void checkString(String str) {
    	if (!(str.equalsIgnoreCase("multiply") || str.equalsIgnoreCase("add"))){
    		//throw new IOException("Incorrect computation string");
    		System.err.println("Invalid Command");
    	}
    }
    
}