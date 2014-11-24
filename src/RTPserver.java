import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.io.FileInputStream;
import java.lang.Object;
/**
 * This is the server class for the RTP 
 * 
 * @author Hailey Armfield
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
		setUp();
	}
	public void setWindowSize(int windowSize){
		this.windowSize = windowSize;
	}
    public void setUp() throws Exception
    {
    	System.out.println("In setUp");
    	serverSocket = new DatagramSocket(serverPort);   	
        try
        {	
            
    		byte[] buffer = new byte[64];
    		DatagramPacket incomingPackets = new DatagramPacket(buffer, buffer.length);
    		serverSocket.receive(incomingPackets);
    		
    		//receive the packets from the client and some checks
    		clientPort = incomingPackets.getPort();
    		System.out.println("NetEmuPort is:  " + netEmuPort);
    		System.out.println("NetEmuIP is:  "+ netEmuIp);
    		
    		/* The client port is 8000 as read from the server.
    		if(validateClientPort(clientPort) == false){
    			System.out.println("ERROR: The client port is not (serverPort - 1)");
    		}
    		else{
    			System.out.println("The Ports are 1 apart");
    		}
    		*/
    		byte[] data = incomingPackets.getData();
    		String strFile = new String(data);
    		System.out.println("RTPserver "+ strFile);
    		FileInputStream fileInputStream = null;
            String s = "file.txt";
    		
    		/**
    		 * 
    		 * 
    		 * WE HAVE RECIEVED THE INPUT FROM THE FILE NOW WE NEED TO TRANSFER IT TO THE CLIENT IN PACKETS 
    		 * 
    		 * 
    		 */
            File file = new File(strFile);
            byte[] bFile = new byte[(int) file.length()];
            
            RTPPacket p = new RTPPacket((int)file.length());
            
      	    fileInputStream = new FileInputStream(file);
    	    fileInputStream.read(bFile); 	    
    	    fileInputStream.close();
    	    
    	    int counter = 0;
    	    int maxLength = 6;
    	    DatagramPacket dp;
    	    
    	    
	    	while (counter < bFile.length -1){
	    		
	    		byte[] aFile = new byte[maxLength];
	    		if (bFile.length > aFile.length){
	    			//aFile = ArrayUtils.concat(head, Arrays.copyOfRange(bFile, counter, maxLength+counter));
	    			aFile = Arrays.copyOfRange(bFile,counter, maxLength + counter);
	    			counter = counter + maxLength;
	    		}
	    		
        
	    		//create datagram packet to send back to the client
	    		dp = new DatagramPacket(aFile , aFile.length , incomingPackets.getAddress() , incomingPackets.getPort());
	    		serverSocket.send(dp);
	    		
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
