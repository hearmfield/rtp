
public class RTPpacket {
	public byte[] header;
	//public int payload_length; 
	//public byte[] payload;

	
	//establishes connection and synchronizes sequence numbers
	public int syn; 
	// acknowledge field significant
	public int ackNumber;
	// non-cummulative extended acknowledgement
	public int eack;
	//reset the connection
	public int rst;
	// zero data length segment
	public int nul;
	//contains version number of protocol = 1
	public int versionNumber;
	// shows length of header length to help find the start of the data field 
	// header with no variable header section = 9
	public int header_length;
	
	// allows port numbers between 0 and 255
	public int sourcePort;
	// allows port numbers between 0 and 255
	public int destinationPort;
	// length of payload
	public int data_length;
	// sequence number of this segment
	public int sequenceNumber;
	// if the ack bit is set, this is the sequence number of the last segment that the receiver received correctly
	public int acknowledgementNumber;
	//32 bit checksum
	public int checksum;
	// the maximum number of outstanding segments that will be sent without receiving an acknowledgement first
	public int rcvWindow;
	// the maximum size segment that the sender should send
	public int mss;
	// option flag field - sequence delivery mode
	// flag to signal if delivery mode is sequenced (1) or non-sequenced (0)
	public int sdm;
	// packet that was received with a correct checksum 
	
	public boolean ackReceived;


	public int length;
	
	
	
	//offsets of header -- dont need
    public static final int SEQ_NUM_OFFSET = 0;
    public static final int ACK_NUM_OFFSET = 4;
    public static final int FLAGS_OFFSET = 8;
    public static final int CHECKSUM_OFFSET = 12;
    public static final int RCV_WIN_OFFSET = 16;
    public static final int LENGTH_OFFSET = 20;
    public static final int HDR_SIZE = 24; 
    public static final int FLAGS_ACK = 1;
	
	public RTPpacket(int dPort){

		syn = 0; 
		ackNumber = 0;
		eack = 0;
		rst = 0;
		nul = 0;
		versionNumber = 0;
		header_length = 20;
		//sourcePort = sPort;
		destinationPort = dPort;
		data_length = 0;
		sequenceNumber = 0;
		acknowledgementNumber = 0;
		checksum = 0;
		rcvWindow = 0;
		mss = 0;
		sdm = 0;
		
		
		header = new byte[header_length];
	}
	
	public void setSyn(){
		//check to see if syn is 0 or 1
		if (header[0] >> 7 == 0){
			//change 0 to 1
			header[0] = (byte) (1 << 7 | header[0]);
		}
	}
	
	public void setAckBit(){
		if (header[0] >> 6 == 0){
			//change 0 to 1
			header[0] = (byte) (1 << 6 | header[0]);
		} 
	}
	
	
	/**
	 * Comput the checksum from the information given
	 * @return
	 */
	public int computeChecksum(){
		return 0;
	}
	
	/**
	 * Decide if the data is corrupt or not based on the calculation of the checksum
	 * @return
	 */
	public boolean isValid(){
		if (computeChecksum() - checksum == 0)
			return true;
		else
			return false;
	}

	//need to convert packet to bytes
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}

}
