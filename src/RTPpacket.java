import java.util.Arrays;
import java.util.Random;
import java.util.zip.*;
import java.lang.Object;
import java.nio.ByteBuffer;

public class RTPPacket {
    public byte[] segment;
    public byte[] header;
    public byte[] payload;
    

    // acknowledge field significant
    public int ackNumber;
    // non-cummulative extended acknowledgement
    public int eack;
    //reset the connection
    public int rst;

    // shows length of header length to help find the start of the data field 
    // header with no variable header section = 9
    public final int header_length = 20;
    
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
    public long checksum;
    // the maximum number of outstanding segments that will be sent without receiving an acknowledgement first
    public int rcvWindow;
    // the maximum size segment that the sender should send
    public int mss;
    // option flag field - sequence delivery mode
    // flag to signal if delivery mode is sequenced (1) or non-sequenced (0)
    public int sdm;
    // packet that was received with a correct checksum 
    public int length;
    
    //public byte[] checksumArr;
    
    public CRC32 crc;
    
    public RTPPacket(){
        mss = 64;
    }
    
    public RTPPacket(byte[] c){
        segment = c;
        mss = 64;
        header = Arrays.copyOfRange(segment,0, 20);
        payload = Arrays.copyOfRange(segment,20,mss);
        //computeChecksum(payload);
    }
    

    public RTPPacket(int fileLength){
        this();
        header = new byte[header_length];
        header[2] = (byte) sourcePort;
        header[3] = (byte) destinationPort;
        setDataLength(fileLength);
        payload = new byte[fileLength];
        
        //first sequence number for the server
        sequenceNumber = isn();
        setSequenceNumber(sequenceNumber);
    }
    public RTPPacket(int sPort, int dPort){
        eack = 0;
        rst = 0;

        sourcePort = sPort;
        destinationPort = dPort;
        data_length = 0;
        sequenceNumber = 0;
        acknowledgementNumber = isn();
        checksum = 0;
        rcvWindow = 0;
        mss = 64;
        sdm = 0;
    
        header = new byte[header_length];
        header[2] = (byte) sourcePort;
        header[3] = (byte) destinationPort;
        //checksumArr = new byte[] {header[14], header[15], header[16], header[17]};
    }

    public void setSyn(){
        //check to see if syn is 0 or 1
        
        if (header[0] >> 7 == 0){
            //change 0 to 1
            header[0] = (byte) (1 << 7 | header[0]);
        }
        else if(header[0] >> 7 == -1){
            header[0] = (byte)(1 << 7 ^ header[0]);
        }
        //System.out.println(header[0] >> 7);
    }
    
    public void setAckBit(){
        //may need extra case if syn bit = 1 
        if (header[0] >> 6 == 0){
            //change 0 to 1
            header[0] = (byte) (1 << 6 | header[0]);
        } 
        //set 1 to 0
        else if(header[0] >> 6 == 1){
            header[0] = (byte) (1 << 6 ^ header[0]);
        }
    }
    public long getSenderChecksum(){
    	
    	
    	long  he = header[14] << 24 | header[15] << 16 | header[16] << 8 | header[17];
    	return he;
    }
    public boolean checkSyn(){
        
        if (header[0] >> 7 == -1){
            return true;
        }
        return false;
    }
    
    public boolean checkAck(){
        // may need extra case if syn bit = 1 
        //System.out.println(header[0] >> 6);
        if (header[0] >> 6 == 1)
            return true;
        return false;
    }
    
    public void setRSTbit(byte input){
        if(input == 0 || input == 1){
            if(header[0] >> 4 != input){
                if(input == 1){
                    header[0] = (byte)(1 << 4 ^ header[0]);
                    //this will turn the RST bit from a 0 to a 1
                }
                else{
                    header[0] = (byte)(0 << 4 ^ header[0]);
                    //this will turn the RST bit from a 1 to a 0
                }
            }
        }
    }
    public int getAckBit(){
    	// may need extra case if syn bit = 1 
    	return (header[0] >> 6);
    }
    //I dont know what to do with this
    public boolean getRSTbit(){
        if(header[0] >> 4 == 1){
            //this will read the header and then give the value for reset true
            header[0] = (byte)(1 << 4 ^ header[0]);
            return(true);
            //reset the variable in this header 
        }
        else{
            return(false);
        }
    }
    /**
     * Compute the checksum from the information given
     * @param mss2 
     * @param header_length2 
     * @param mss2 
     * @param header_length2 
     * @return
     */
    
    public void computeChecksum(byte[] payload){
    	//byte[] newPayload = new Byte();
    	//payload is the data that will be sent in the next packet
    	//here we have to compute the checksum on that data and then set it in the headers 14-17
    	
    	//compute checksum from header.length - mss
    	crc = new CRC32();
    	crc.reset();
    	crc.update(payload, 0, payload.length);
    	checksum =  crc.getValue();
    	
    	header[14] = (byte) (checksum >> 24);
    	header[15] = (byte) (checksum >> 16);
    	header[16] = (byte) (checksum >> 8);
    	header[17] = (byte) (checksum);
    	//header[14] - header[17]r
    	//return checksum;
    }
    public void getOppChecksum(byte[] payload){
    	int ans = getChecksum();
    	ans = ~ans;
    }
    public void increaseSequenceNumber(){
        int n = getSequenceNumber();
        n++;
        setSequenceNumber(n);
        System.out.println("Sequence Number: " + sequenceNumber);
    }
    
    
    public void setSequenceNumber(int seqNum){
        header[6] = (byte) (seqNum >> 24);
        header[7] = (byte) (seqNum >> 16);
        header[8] = (byte) (seqNum >> 8);
        header[9] = (byte) (seqNum);
        sequenceNumber = getSequenceNumber();
    }
    
    public void setAckNumber(){
        header[10] = header[6];
        header[11] = header[7];
        header[12] = header[8];
        header[13] = header[9];
        //do we remove it from the seq position?
    }

    public void setDataLength(int fileLength){
        header[4] = (byte) (fileLength >> 8);
        header[5] = (byte) fileLength;
    }
    
    public byte[] returnPacket(){
        byte[] combined = new byte[header.length + payload.length];
        System.arraycopy(header, 0, combined, 0, header.length);
        System.arraycopy(payload, 0, combined, header.length, payload.length);
          
        return combined;
    }
    
    public void sendACK(){
        setAckBit();
        setAckNumber();
    }
    
    
    public byte[] getHeader(){
        return Arrays.copyOfRange(segment,0, header_length);
    }
    
    public byte[] getPayload(){
        return payload;
    }
    
    public int getAckNumber(){
        int value = 0;
        for (int i = 10; i < 14; i++) {
            int shift = (14 - 1 - i) * 8;
            value += (header[i] & 0x000000FF) << shift;
        }
        acknowledgementNumber = value;
        return value;
    }
    
    public int getDataLength(){
            int value = 0;
            for (int i = 4; i < 6; i++) {
                int shift = (6 - 1 - i) * 8;
                value += (header[i] & 0x000000FF) << shift;
            }
            data_length = value;
            return value;
        }
        
    public int getNumberOfPackets(){
            data_length = getDataLength();
            if (data_length % 2 == 0)
                return data_length/(mss-header_length);
            return data_length/(mss-header_length) + 1;
        }
    
    public int getSequenceNumber() 
    {
        int value = 0;
        for (int i = 6; i < 10; i++) {
            int shift = (10 - 1 - i) * 8;
            value += (header[i] & 0x000000FF) << shift;
        }
        sequenceNumber = value;
        return value;
    }
    
    public int getChecksum() 
    {
        int value = 0;
        for (int i = 14; i < 18; i++) {
            int shift = (18 - 1 - i) * 8;
            value += (header[i] & 0x000000FF) << shift;
        }
        checksum = value;
        return value;
    }
    
    public static int isn(){
        // randomly generate a 32bit number 
        Random r = new Random();
        int n = r.nextInt(3000);
        //byte[] a = n;
         return n;
    }

}
