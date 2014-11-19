
public class RTPpacket {
	final static int HEADER_SIZE = 20;
	
	//rtp header info
	public int version;
	public int padding;
	public int extension;
	public int cc;
	public int marker;
	public int payloadType;
	public int sequenceNumber;
	public int ssrc;
	
	public byte[] header;
	public int payload_length; 
	public byte[] payload;
	
	//details of header
	public int ackNumber;
	public int flags;
	public int checksum;
	public int rcvWindow;
	public boolean ackReceived;
	
	//offsets of header
    public static final int SEQ_NUM_OFFSET = 0;
    public static final int ACK_NUM_OFFSET = 4;
    public static final int FLAGS_OFFSET = 8;
    public static final int CHECKSUM_OFFSET = 12;
    public static final int RCV_WIN_OFFSET = 16;
    public static final int LENGTH_OFFSET = 20;
    public static final int HDR_SIZE = 24; 
    public static final int FLAGS_ACK = 1;
	
	public RTPpacket(){
		version = 2;
		padding = 0;
		extension = 0;
		cc = 0;
		marker = 0;
		ssrc = 0;
		
		sequenceNumber = 0;
		checksum = 0;
		ackNumber = 0;
		flags = 0;
		ackReceived = false;
		
		
		header = new byte[HEADER_SIZE];
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

}
