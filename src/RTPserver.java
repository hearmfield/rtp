import java.io.*;
import java.net.*;

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
	
	DatagramSocket serverSocket = null;
	boolean connectionUp = false;
    //public static void main(String args[]) throws SocketException

	public RTPserver(String netEmuIp, String netEmuPort, int serverPort) throws Exception{
		System.out.println("Setting up RTPserver...");
		this.netEmuIp = netEmuIp;
		this.netEmuPort = netEmuPort;
		this.serverPort = serverPort;
		//this.filename = filename;
		this.connectionUp = true;
		//setUp();
	}
	public void listen() throws Exception{
		System.out.println("RTPserver is listening...");
		setUp();
	}
    public void setUp() throws Exception
    {
    	System.out.println("In setUp");
    	serverSocket = new DatagramSocket(serverPort);   
    	//serverSocket.setSoTimeout(timeout);//timeout for acks
    	
    	InetAddress emulatorIP = InetAddress.getByName(netEmuIp);		
        try
        {	
            
    		byte[] buffer = new byte[64];
    		DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
    		serverSocket.receive(incoming);
    		//receive the packets from the client
    		clientPort = incoming.getPort();
    		if(validateClientPort(clientPort) == false){
    			System.out.println("ERROR: The client port is not (serverPort - 1)");
    		}
    		byte[] data = incoming.getData();
    		//System.out.println(data[0]);    -    This will print out the ASCII value of the first character in the data array
    		System.out.println(data.length);
    		
    		String s = new String(data, 0, incoming.getLength());
    		String answer = getFile(s);
    		
    		/**
    		 * 
    		 * 
    		 * WE HAVE RECIEVED THE INPUT FROM THE FILE NOW WE NEED TO TRANSFER IT TO THE CLIENT IN PACKETS 
    		 * 
    		 * 
    		 */
    		
    		
    		
    		
    		
    		System.out.println(answer.length());
    		
    		while(answer.length() > 5){
                DatagramPacket send = new DatagramPacket(answer.getBytes() , answer.getBytes().length , incoming.getAddress(), incoming.getPort());
            	//	InetAddress.getByName(netEmuIp) , Integer.parseInt(netEmuPort));
                System.out.println("sending responce from the server");
                serverSocket.send(send);
    		}
    		//s or the incoming will be the filename
            //DatagramSocket serverSocket = new DatagramSocket(port);
            
            // buffer to receive packets from client
            // 32 for two numbers and add extra for computation string
    		//getFile(clientInput);
            //do math
            //create datagram packet to send back to the client
            DatagramPacket send = new DatagramPacket(answer.getBytes() , answer.getBytes().length , incoming.getAddress(), incoming.getPort());
            	//	InetAddress.getByName(netEmuIp) , Integer.parseInt(netEmuPort));
            System.out.println("sending responce from the server");
            serverSocket.send(send);
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
    	String directory = "C:\\Users\\Andrew Ford\\Documents\\Eclipse\\ReliableTransportProtocol\\src\\";
    	BufferedReader reader = new BufferedReader(new FileReader(directory + file));
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
		if(serverPort - cp == 1){
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
