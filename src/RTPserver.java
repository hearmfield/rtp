import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileInputStream;
import java.lang.Object;
import java.util.Random;
/**
 * This is the server class for the RTP 
 * 
 * @author Hailey Armfield
 * @author Andrew Ford
 *
 */
public class RTPserver
{
	/**
	 * The main method takes in the port number provided and creates a socket for listening 
	 * at the given port. It creates a buffer in which it will receive the datagram packets 
	 * from the client. The server reads these packets and returns the computed result to 
	 * the client.
	 * 
	 * @param args
	 */
	String netEmuIp;
	String netEmuPort;
	String filename;
	int serverPort;
	int clientPort;
	int timeout = 3000;
	int windowSize;
	DatagramSocket serverSocket = null;
	boolean connectionUp = false;
	public RTPserver(){
		
	}
	public RTPserver(String netEmuIp, String netEmuPort, int serverPort) throws Exception{
		System.out.println("Setting up RTPserver...");
		this.netEmuIp = netEmuIp;
		this.netEmuPort = netEmuPort;
		this.serverPort = serverPort;
		this.connectionUp = true;
	}
	public void listen() throws Exception{
		System.out.println("RTPserver is listening...");
		run();
	}
	public void setWindowSize(int windowSize){
		this.windowSize = windowSize;
		System.out.println(windowSize);
	}
    public void run() throws Exception
    {
    	System.out.println("In Server setUp");
    	
    	serverSocket = new DatagramSocket(serverPort);  //Binded Port for Server 	
        try
        {	
    		byte[] buffer = new byte[64];
    		DatagramPacket incomingPackets = new DatagramPacket(buffer, buffer.length);
    		serverSocket.receive(incomingPackets);
    		System.out.println("We got data");
    		//some checks to make sure we are receiving the correct route through the Network emulator

    		byte[] data = incomingPackets.getData();
    		String strFile = new String(data);
    		System.out.println("RTPserver strfile:  "+ strFile);
    		FileInputStream fileInputStream = null;
    		
            File file = new File(strFile);
            
            RTPPacket p = new RTPPacket((int)file.length());
            
      	    fileInputStream = new FileInputStream(file);
    	    fileInputStream.read(p.payload); 	    
    	    fileInputStream.close();
    	    
    	    /**
    	     * We got the message from the client and now we are sending it in packets to the client
    	     * START of the actual packet transmission
    	     */   	    
    	    
            DatagramPacket receiveAck = new DatagramPacket(buffer,buffer.length);
            
            int payloadIndex = 0;
            int sentCounter = 0;
            int ackCounter = 0; //compare to sent counter
            int lastAckNumber; //resend if one is missing
            DatagramPacket dp = null;
            serverSocket.setSoTimeout(timeout);
            
    	    //SENDER WAITS FOR ACKS - SELECTIVE REPEAT PIPELINED            
            while (payloadIndex < p.payload.length -1){
            	
            	try{
	                byte[] aFile = new byte[p.mss];
	                if ((p.getDataLength()-payloadIndex)/(p.mss-p.header_length) > 0){
	                    p.increaseSequenceNumber();
	                    System.arraycopy(p.header, 0, aFile, 0, p.header_length);
	                    
	                    byte[] payload_arr = new byte[p.mss - p.header_length]; 
		    			System.arraycopy(p.payload, payloadIndex, payload_arr, 0, p.mss - p.header_length);
		    			p.computeChecksum(payload_arr);
		    			System.out.println(p.checksum);
		    			System.arraycopy(payload_arr, 0, aFile, p.header_length, p.mss - p.header_length);
		    			
	                    //System.arraycopy(p.payload, payloadIndex, aFile, p.header_length, p.mss - p.header_length);
	                    payloadIndex = payloadIndex + (p.mss-p.header_length);      
	                }
	                //case for last packet that does not have the same length payload
	                else{   
	                    p.increaseSequenceNumber();
	                    System.arraycopy(p.header, 0, aFile, 0, p.header_length);
	                    
	                    byte[] payload_arr = new byte[p.mss - p.header_length]; 
		    			System.arraycopy(p.payload, payloadIndex, payload_arr, 0, p.payload.length - payloadIndex);
		    			p.computeChecksum(payload_arr);
		    			System.out.println(p.checksum);
		    			System.arraycopy(payload_arr, 0, aFile, p.header_length, p.payload.length - payloadIndex);
		    			
	                    //System.arraycopy(p.payload, payloadIndex, aFile, p.header_length, p.payload.length - payloadIndex);
	                    payloadIndex = payloadIndex + (p.mss-p.header_length);  
	                }
	                //p.computeChecksum(aFile);
	                dp = new DatagramPacket(aFile , aFile.length , incomingPackets.getAddress() , incomingPackets.getPort());
	                serverSocket.send(dp);
	                sentCounter++;
	            	 
	                serverSocket.receive(receiveAck);
	                byte[] arr = receiveAck.getData();
	                RTPPacket receivedPacket = new RTPPacket(arr);
	                
	                System.out.println("Received Packet: " + receivedPacket.getAckNumber());
	                if(receivedPacket.getAckNumber() != ackCounter){
	                	serverSocket.send(dp);
	                }
	                else{
	                	ackCounter++;
	                }
            	}
            	//This should detect a lost packet and then attempt to resend it
            	catch(SocketTimeoutException e){
            		if(dp!=null){
            			serverSocket.send(dp);
            			serverSocket.receive(receiveAck);
    	                byte[] arr = receiveAck.getData();
    	                RTPPacket receivedPacket = new RTPPacket(arr);
    	                System.out.println("Received Packet: " + receivedPacket.getAckNumber());
            			continue;
            		}
            		else{
            			break;
            		}
            	}
            }
            System.out.println("sending responce from the server");
            serverSocket.close();
        }
         
        // IO exception for port number
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
    }
	/**
     * This can read from a txt file and then will send what it got
     * 
     * YOU NEED TO CHANGE YOUR DIRECTORY VARIABLE
     * @param file
     * @return
     * @throws IOException
     */
    private String getFile(String file) throws IOException{
    	BufferedReader reader = new BufferedReader(new FileReader(file));
    	String line = null;
    	String result = "";
    	while((line = reader.readLine()) != null){
    		System.out.println(line);
    		result += line;
    	}
    	reader.close();
    	return result;
    }
    
    private boolean validateClientPort(int cp) {
		if(serverPort - cp == 0){
			return true;
		}
		return false;
		
	}
	/**
     * Checks if result of two numbers is 2 bytes.
     * @param result
     * @return
     */
    public static boolean checkValidPortRangef(int result){
	   	if (result<0 || result>65535){
	       	return false;
	       }
	   	return true;
   }
	public void terminate() {
		// This will need to close the socket
		serverSocket.close();
	}
}
