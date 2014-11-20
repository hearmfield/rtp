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
	static String netEmuIp;
	static String netEmuPort;
	boolean connectionUp = false;
	static DatagramSocket socketClient;

	public RTPclient(String netEmuIp, String netEmuPort) throws SocketException{
		this.netEmuIp = netEmuIp;
		this.netEmuPort = netEmuPort;
		this.connectionUp = true;
		run();
	}
	public void run() throws SocketException
     {
			//creates a socket for the connection
			
		    try
		    {
		    	socketClient = new DatagramSocket();
		    	String args[];//remove this have it just for the syntax below
		    	
		    	while(connectionUp){
		    	//create new packet header for packet
		    	//seqnum++
		    	
		    	
		    	//connEstablishment();
		    	

		            //takes input and converts in to bytes to prepare for packet transfer
		    		String s = "bye";
		            byte[] input_data;// = input.getBytes();
		            input_data = s.getBytes();
		            //add header to input
		           
		            
		            //createPacket(newPacket, input_data);
		            
		           //**need to change to correct variables
		            InetAddress host = InetAddress.getByName(netEmuIp); 
		            int port = Integer.parseInt(netEmuPort); 
		            
		            RTPpacket newPacket = new RTPpacket(port);
		            connEstablishment(newPacket);

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
		           
		            //There needs to be a function that updates all the acks recieved and so on. 
		            //connectionUp needs to be updated to know when to terminate the program
		            
		            // output from the server
		            System.out.println("From server: " + str);
		            socketClient.close();	
		    	}
		    }
		    // catches if host is not valid
		    catch(UnknownHostException e) {
		        //System.out.println("UnknownHostException: " + e);
		    	System.err.println("Invalid Command");
		    	//socketClient.close();	
		    }
		    catch(IOException e) {
		    	System.err.println("Invalid Command");	
		    }
		    
		    
		  
		    
		    
	} 
 
    
    /**
     * Creates the connection establishment through the three way handshake
     */
    public static void connEstablishment(RTPpacket packet){
    	//Create packet

    	//Send syn
    	packet.setSyn();
    	byte[] packetInBytes = packet.getBytes();
    	//need to convert packet to byte[]
    	DatagramPacket  connectPacket = new DatagramPacket(packet, packet.length , netEmuIp , netEmuPort);
        socketClient.send(connectPacket);
        
         //buffer to receive reply
         byte[] buffer = new byte[64];
         DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
         
         //creates time out 
         socketClient.setSoTimeout(2000);
         
         // need to have statement to try again
         socketClient.receive(reply);
         byte[] data = reply.getData();
         String str = new String(data, 0, reply.getLength());
    	
    	
    	//wait for ack+syn
    	 //packet.header[0] bit 0 is syn bit 1 is the ack
    	
    	//send ack
    	 //packet.header[0] turn off bit 0 and syn = 1
    	


    }
    public void createChecksum(){
    	int checksumAcc = 0;
    	int checksumPointer = 0;
    	
    	
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