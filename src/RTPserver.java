import java.io.*;
import java.net.*;
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
            //String s = "file.txt";
    		
            File file = new File(strFile);
            //byte[] bFile = new byte[(int) file.length()];
            
            RTPPacket p = new RTPPacket((int)file.length());
            
      	    fileInputStream = new FileInputStream(file);
    	    fileInputStream.read(p.payload); 	    
    	    fileInputStream.close();
    	    
            DatagramPacket receiveAck = new DatagramPacket(buffer,buffer.length);
            
            int payloadIndex = 0;
            int sentCounter = 0;
            int ackCounter = 0; //compare to sent counter
            int lastAckNumber; //resend if one is missing
            DatagramPacket dp;
    	    
           
    	    //SENDER WAITS FOR ACKS - SELECTIVE REPEAT PIPELINED            
            while (payloadIndex < p.payload.length -1){//this is 0 to the files full length
            	System.out.println(p.payload.length);
            	System.out.println("Sending pipelined packets");
            	
	                byte[] aFile = new byte[p.mss];
	                //if (p.payload.length + p.header_length > aFile.length){
	                if ((p.getDataLength()-payloadIndex)/(p.mss-p.header_length) > 0){
	                	System.out.println("here");
	                    p.increaseSequenceNumber();
	                    System.arraycopy(p.header, 0, aFile, 0, p.header_length);
	                    System.arraycopy(p.payload, payloadIndex, aFile, p.header_length, p.mss - p.header_length);
	                    payloadIndex = payloadIndex + (p.mss-p.header_length);      
	                }
	                //case for last packet that does not have the same length payload
	                else{   
	                    p.increaseSequenceNumber();
	                    System.arraycopy(p.header, 0, aFile, 0, p.header_length);
	                    System.arraycopy(p.payload, payloadIndex, aFile, p.header_length, p.payload.length - payloadIndex);
	                    payloadIndex = payloadIndex + (p.mss-p.header_length);  
	                }
	                //create datagram packet to send back to the client
	                dp = new DatagramPacket(aFile , aFile.length , incomingPackets.getAddress() , incomingPackets.getPort());
	                serverSocket.send(dp);
	                sentCounter++;
            	 
                /*try {
                    serverSocket.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/
                serverSocket.receive(receiveAck);
                byte[] arr = receiveAck.getData();
                RTPPacket receivedPacket = new RTPPacket(arr);
                System.out.println("Received Packet: " + receivedPacket.getAckNumber());
                //ackCounter++;
                //resend the packet because the client did not get it this is for stop and wait
//                if(receivedPacket.getAckNumber() != ackCounter){
//                	serverSocket.send(dp);
//                }
//                else{
//                	ackCounter++;
//                }
                
            }
    		/*
    	    dp = new DatagramPacket(p.returnPacket() , p.returnPacket().length , incomingPackets.getAddress() , incomingPackets.getPort());
    		serverSocket.send(dp);
    		*/
    		
    		//System.out.println(answer.length());
    		/*
    		while(answer.length() > 5){
                DatagramPacket send = new DatagramPacket(answer.getBytes() , answer.getBytes().length , incoming.getAddress(), incoming.getPort());
            	//	InetAddress.getByName(netEmuIp) , Integer.parseInt(netEmuPort));
                System.out.println("sending responce from the server");
                serverSocket.send(send);
    		}*/
    		//s or the incoming will be the filename
            //DatagramSocket serverSocket = new DatagramSocket(port);
            
            // buffer to receive packets from client
            // 32 for two numbers and add extra for computation string
    		//getFile(clientInput);
            //do math
            //create datagram packet to send back to the client
           // DatagramPacket send = new DatagramPacket(answer.getBytes() , answer.getBytes().length , incomingPackets.getAddress(), incomingPackets.getPort());
            	//	InetAddress.getByName(netEmuIp) , Integer.parseInt(netEmuPort));
            System.out.println("sending responce from the server");
            //serverSocket.send(send);
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
    	//String directory = "C:\\Users\\Andrew Ford\\Documents\\Eclipse\\ReliableTransportProtocol\\src\\";
    	BufferedReader reader = new BufferedReader(new FileReader(file));//directory + 
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
