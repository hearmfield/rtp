import java.io.*;
import java.net.*;

/**
 * This is the server class for the RTP 
 * 
 * @author Hailey Armfield
 *
 */
public class server
{
	/**
	 * The main method takes in the port number provided and creates a socket for listening 
	 * at the given port. It creates a buffer in which it will receive the datagram packets 
	 * from the client. The server reads these packets and returns the computed result to 
	 * the client.
	 * 
	 * @param args
	 */
    public static void main(String args[])
    {
         
        try
        {	
        	// read in port from input     	
        	int port = Integer.parseInt(args[0]);
        	// create socket for listening
            DatagramSocket serverSocket = new DatagramSocket(port);
            
            // buffer to receive packets from client
            // 32 for two numbers and add extra for computation string
            byte[] buffer = new byte[64];
            DatagramPacket packets = new DatagramPacket(buffer, buffer.length);
            
            //receive packets from the client
            serverSocket.receive(packets);
            byte[] data = packets.getData();
            
            //split bytes into different strings
            String str = new String(data, 0, packets.getLength());       
            String nums[] = str.split(" ",3); 
            
            //do math
            String answer = computation(nums);
            //create datagram packet to send back to the client
            DatagramPacket dp = new DatagramPacket(answer.getBytes() , answer.getBytes().length , packets.getAddress() , packets.getPort());
            serverSocket.send(dp);
        }
         
        // IO exception for port number
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
    }
    
    //converts integers and computes math
    /**
     * This is the computation method.
     * 
     * @param inputArray
     * @return
     */
    public static String computation(String[] inputArray){
 	   String cc = "Cannot Compute";
    	   int a = Integer.parseInt(inputArray[1]);
        int b = Integer.parseInt(inputArray[2]);
        int c = 0;
       
    	if(inputArray[0].equalsIgnoreCase("add")){
    		c = a+b;
    		if(checkNum(c)){
    			return c+"";
    		}
    		return cc;
    	}
    	else if (inputArray[0].equalsIgnoreCase("multiply")){
    		c = a*b;
    		if(checkNum(c))
    			return c+"";
    		else
    			return cc;
    	}
    	else 
    		return cc;
    }
    
    /**
     * Checks if result of two numbers is 2 bytes.
     * @param result
     * @return
     */
    public static boolean checkNum(int result){
	   	if (result<0 || result>65535){
	       	return false;
	       }
	   	return true;
   }
    
    public static void connEstablishment(){
    	//wait for client
    	
    	//receive syn 
    	//send syn+ack
    	
    	//wait for ack
    	
    	//recieve ack
    }
}