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
    public static void main(String args[]) throws SocketException
    {
    	//creates a socket for the connection
    	DatagramSocket socketClient = new DatagramSocket();
    	
    	
        try
        {
        	//create new packet header for packet
        	//RTPpacket newPacket = new RTPpacket();
        	
        	//connEstablishment();
        	
        	  String input = "";
                for(int i = 1;i<args.length;i++){
                	if (i+1 == args.length){
                		input += args[i];
                	}
                	else
                		input += args[i] + " ";    
                }

                //takes input and converts in to bytes to prepare for packet transfer
                byte[] input_data = input.getBytes();
                //add header to input
                
                
               //**need to change to correct variables
                InetAddress host = InetAddress.getByName("localhost"); 
                int port = Integer.parseInt("7000"); 


                //send packet
                DatagramPacket  dpacket = new DatagramPacket(input_data , input_data.length , host , port);
                socketClient.send(dpacket);
               
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
     * Creates the connection establishment through the three way handshake
     */
    public static void connEstablishment(){
    	//Create packet
    	 RTPpacket packet = new RTPpacket();
    	 //packet.header[0] = packet.header[0] && 0x80;
    	
    	//Send syn
    	
    	
    	//wait for ack+syn
    	 //packet.header[0] bit 0 is syn bit 1 is the ack
    	
    	//send ack
    	 //packet.header[0] turn off bit 0 and syn = 1
    	
+

    }
    
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