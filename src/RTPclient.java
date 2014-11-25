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
	String netEmuIp;
	String netEmuPort;
	String filename;
	String clientPort;
	
	int windowSize;
	int timeout = 3000;
	InetAddress emulatorIP;
	DatagramSocket clientSocket = null;
	boolean connectionUp = false;

	public RTPclient(String netEmuIp, String netEmuPort, String clientPort) throws Exception{
		System.out.println("Setting up RTPclient...");

		this.netEmuIp = netEmuIp;
		this.netEmuPort = netEmuPort;
		this.clientPort = clientPort;
		this.connectionUp = true;
		
	}
	public int downloadFromServer(String file) throws Exception{
		filename = file;
		//if download was successful then we return a 1 to the FTA client, 0 otherwise
		setUp();
		return 0;//return status of the download loss packets? duplicate? more to come
	}
	public void setWindowSize(int windowSize){
		this.windowSize = windowSize;
	}
	public void setUp() throws Exception
    {
		System.out.println("In the client setup");
    	//creates a socket for the connection which will be at 4000
    	clientSocket = new DatagramSocket(Integer.parseInt(clientPort));   
    	//clientSocket.setSoTimeout(timeout);//timeout for acks
    	
    	emulatorIP = InetAddress.getByName(netEmuIp);
        try
        {
        	//while(connectionUp){
        	
        		//this will send the packet file
                byte[] b = filename.getBytes();//user entered file name at the FTAclient command
                DatagramPacket  dPacket = new DatagramPacket(b , b.length , emulatorIP , Integer.parseInt(netEmuPort));
                clientSocket.send(dPacket);//send the file to be downloaded to the netEmu then to the server
                StringBuffer received = new StringBuffer("");

                //this will recieve the responce from the server
                byte[] buffer = new byte[64];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                DatagramPacket ackPacket;
                //n is the window size
                int n = 0;
                int timeout = 5000;
                RTPPacket p = new RTPPacket();
                clientSocket.setSoTimeout(timeout);
                
                clientSocket.receive(reply);
                //System.out.println("Got message from the server");
                byte[] response = reply.getData();
                p = new RTPPacket(response);
                
                //System.out.println("Sequence Number is: "+ p.getSequenceNumber());
                
                String rec = new String(p.getPayload());
                int lastSequenceNumber = 0;
                received.append(rec);
                n++;//we recrived our first packet
                
           		// if the first packet is received correctly
           		p.setAckBit();
           		p.setAckNumber();

        		ackPacket = new DatagramPacket(p.header , p.header.length , emulatorIP , Integer.parseInt(netEmuPort));
        		clientSocket.send(ackPacket);
        		System.out.println("From server: " + received);
                //int numPackets = 0;
           		do{
                	System.out.println(n);
                	try{
                		clientSocket.receive(reply); 
	                    response = reply.getData();
	                    p = new RTPPacket(response);
	                    /*Test for out of order packets
	                    if(lastSequenceNumber == p.getSequenceNumber() - 1){
	                    	
	                    }
	                    */
	                    System.out.println("Sequence Number: " + p.getSequenceNumber());
	                   
	                	rec = new String(p.getPayload());
	                	System.out.println(p.payload.length);
	                	received.append(rec);
	                	n++;
	                	
	                	//if received correctly?
	                	p.setAckBit();
	                	p.setAckNumber();
	                	//send an Ack to the server
	                	if(p.checkAck() == true){
	                		System.out.println("sending ACK");
	                		ackPacket = new DatagramPacket(p.header , p.header.length , emulatorIP , Integer.parseInt(netEmuPort));
	                		clientSocket.send(ackPacket);
	                	}
	                	System.out.println("From server: " + received);
	                	lastSequenceNumber = p.getSequenceNumber();
                	}
                	catch (SocketTimeoutException e) {
                	       continue;
                	}
                	/*
	                byte[] buffer = new byte[6];
	                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
	                clientSocket.setSoTimeout(2000);
	                clientSocket.receive(reply);   
	                String rec = new String(reply.getData());
	                received.append(rec);
	                numPackets++;
	                System.out.println(rec);
	                //byte[] data = reply.getData();
	                 
	                 */
                }
           		while(n < p.getNumberOfPackets());
                
                String s = received.toString();
                Writer writer = new FileWriter(filename);
                writer.write(s);
                
                System.out.println("From Server " + s);
                clientSocket.close();
                writer.close();
                //String received = new String(data, 0, reply.getLength());
                
                //createPacket(newPacket, input_data);
               
                //There needs to be a function that updates all the acks recieved and so on. 
                //connectionUp needs to be updated to know when to terminate the program
                
                // output from the server
                System.out.println("From server: " + received);
                clientSocket.close();	
        	//}
        }
        // catches if host is not valid
        catch(UnknownHostException e) {
            //System.out.println("UnknownHostException: " + e);
        	System.err.println("Invalid Command");
        	clientSocket.close();	
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
							setUp();
						}
						else if(ui.equalsIgnoreCase("exit")){
							clientSocket.close();
						}
					} catch (IOException e1) {
						
						System.err.println("Invalid Command");
						clientSocket.close();	
					}
        }
        
        catch(IOException e)
        {
        	System.err.println("IO Invalid Command");
        	clientSocket.close();	
        }
        
        catch(NumberFormatException e){
        	System.err.println("Invalid Command");
        	clientSocket.close();	
      	  
        }
    }
 

    //private int getLast
    /**
     * Creates the connection establishment through the three way handshake
     */
    public static void connEstablishment(){
    	//Create packet
    	 //RTPpacket packet = new RTPpacket();
    	 //packet.header[0] = packet.header[0] >> 7;
                
    	
    	//Send syn
    	
    	
    	//wait for ack+syn
    	 //packet.header[0] bit 0 is syn bit 1 is the ack
    	
    	//send ack
    	 //packet.header[0] turn off bit 0 and syn = 1
    	


    }
    /*
    public void createChecksum(){
    	int checksumAcc = 0;
    	int checksumPointer = 0;
    	
    	
    }
    */
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
    public void checksum(){
    	
    }
    
    
}


/**
 * Command: 		connect-get F  (only for projects that do NOT support bi-directional transfers)
This command does two things. First, the FTA-client connects to the FTA-server and, if the connection is successfully completed, 
the client downloads file F from the server (if F exists in the same directory that the fta-server executable is stored at). 
If you only support uni-directional transfers (from the server to the client), the client should disconnect from the server at 
the end of the transfer.   
 */

/**
FTA CLIENT
Command-line: 	fta-client X A P
The command-line arguments are:
X: the port number at which the fta-client’s UDP socket should bind to (even number). 
	Please remember that this port number should be equal to the server’s port number minus 1.
A: the IP address of NetEmu
P: the UDP port number of NetEmu

Command: 		connect (only for projects that support bi-directional transfers)
The FTA-client connects to the FTA-server (running at the same IP host). 
Command: 		get F (only for projects that support bi-directional transfers)
The FTA-client downloads file F from the server (if F exists in the same directory as the fta-server executable).
Command: 		post F (only for projects that support bi-directional transfers)
The FTA-client uploads file F to the server (if F exists in the same directory as the fta-client executable).
Command: 		window W (only for projects that support pipelined transfers)
W: the maximum receiver’s window-size at the FTA-Client (in segments).
Command: 		disconnect (only for projects that support bi-directional transfers)
The FTA-client terminates gracefully from the FTA-server. 
*/
