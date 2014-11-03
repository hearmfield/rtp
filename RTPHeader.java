
package com.sun.snoop;

/**
 * This java class is a data structure for RTP storing information header like in RFC 793.
 *
 * @author   Hailey
 */
public class RTPHeader
{
  /**
   * The IP protocol value that indicates the data contains RTP information.
   */
  public static final byte IP_PROTOCOL_RTP = 0x06;


  /**
   * The bitmask that may be applied to the RTP flags to determine whether the
   * urgent pointer field should be considered significant.
   */
  public static final byte RTP_FLAG_MASK_URG = 0x20;



  /**
   * The bitmask that may be applied to the RTP flags to determine whether the
   * acknowledgement field should be considered significant.
   */
  public static final byte RTP_FLAG_MASK_ACK = 0x10;



  /**
   * The bitmask that may be applied to the RTP flags to determine whether the
   * push flag is set.
   */
  public static final byte RTP_FLAG_MASK_PSH = 0x08;


  /**
   * The bitmask that may be applied to the RTP flags to determine whether the
   * reset flag is set, indicating the forceful destruction of a connection.
   */
  public static final byte RTP_FLAG_MASK_RST = 0x04;


  /**
   * The bitmask that may be applied to the RTP flags to determine whether the
   * SYN flag is set, indicating a new connection.
   */
  public static final byte RTP_FLAG_MASK_SYN = 0x02;



  /**
   * The bitmask that may be applied to the RTP flags to determine whether the
   * FIN flag is set, indicating the connection is being closed.
   */
  public static final byte RTP_FLAG_MASK_FIN = 0x01;



  // Any data associated with options in this RTP header.
  byte[] optBytes;

  // The acknowledgement number for this RTP header.
  int ackNumber;

  // The checksum for this RTP header.
  int checksum;

  // The position of the end of this RTP header and the beginning of the data.
  int dataOffset;

  // The destination port for this RTP header.
  int dstnPort;

  // The sequence number for this RTP header.
  int seqNum;

  // The source port for this RTP header.
  int srcPort;

  // The set of flags for this RTP header.
  int RTPFlags;

  // The urgent pointer for this RTP header.
  int urgentPointer;

  // The window for this RTP header.
  int window;

  /**
   * Creates a new RTP header with the provided information.
   *
   * @param  srcPort          The source port for this RTP header.
   * @param  dstnPort         The destination port for this RTP header.
   * @param  seqNum           The sequence number for this RTP header.
   * @param  ackNumber        The acknowledgement number for this RTP header.
   * @param  dataOffset       The data offset for this RTP header, measured in
   *                          32-bit words.
   * @param  RTPFlags         The set of flags associated with this RTP header.
   * @param  window           The window for this RTP header.
   * @param  checksum         The checksum for this RTP header.
   * @param  urgentPointer    The urgent pointer for this RTP header.
   * @param  optBytes         The raw data associated with any options in this
   *                          RTP header.
   */
  public RTPHeader(int srcPort, int dstnPort, int seqNum,
                   int ackNum, int dataOffset, int RTPFlags, int window,
                   int checksum, int urgentPointer, byte[] optBytes)
  {
    this.srcPort         = srcPort;
    this.dstnPort  		 = dstnPort;
    this.seqNum  		 = seqNum;
    this.ackNum      	 = ackNum;
    this.dataOffset      = dataOffset;
    this.RTPFlags        = RTPFlags;
    this.window          = window;
    this.checksum        = checksum;
    this.urgentPointer   = urgentPointer;
    this.optBytes     	 = optBytes;
  }


  /**
   * Decodes information in the provided byte array as a RTP header.
   *
   * @param  headerBytes  The byte array containing the header information to
   *                      decode.
   * @param  offset       The position in the byte array at which to begin
   *                      decoding.
   *
   * @return  The decoded RTP header.
   *
   * @throws  SnoopException  If a problem occurs while trying to decode the
   *                          RTP header.
   */
  public static RTPHeader decodeRTPHeader(byte[] headerBytes, int offset)
         throws SnoopException
  {
    // First, make sure the provided byte array is long enough to hold the
    // minimum RTP header length of 20 bytes.
    if ((offset+20) > headerBytes.length)
    {
      throw new SnoopException("Provided byte array is not large enough to " +
                               "hold the minimum RTP header size of 20 bytes.");
    }


    int srcPort      = SnoopDecoder.byteArrayToInt(headerBytes, offset, 2);
    int dstnPort = SnoopDecoder.byteArrayToInt(headerBytes, offset+2, 2);
    int seqNum  = SnoopDecoder.byteArrayToInt(headerBytes, offset+4, 4);
    int ackNumber       = SnoopDecoder.byteArrayToInt(headerBytes, offset+8, 4);


    int dataOffset = ((headerBytes[offset+12] >>> 4) & 0x0F);
    if ((offset+(dataOffset*4)) > headerBytes.length)
    {
      throw new SnoopException("Provided byte array is not large enough to " +
                               "hold the decoded RTP header size of " +
                               (dataOffset*4) + " bytes.");
    }

    int RTPFlags      = (headerBytes[offset+13] & 0x3F);
    int window        = SnoopDecoder.byteArrayToInt(headerBytes, offset+14, 2);
    int checksum      = SnoopDecoder.byteArrayToInt(headerBytes, offset+16, 2);
    int urgentPointer = SnoopDecoder.byteArrayToInt(headerBytes, offset+18, 2);


    byte[] optBytes;
    if (dataOffset > 5)
    {
    	optBytes = new byte[(dataOffset - 5) * 4];
    	System.arraycopy(headerBytes, offset+20, optBytes, 0,
                       optBytes.length);
    }
    else
    {
      optBytes = new byte[0];
    }


    return new RTPHeader(srcPort, dstnPort, seqNum, ackNumber,
                         dataOffset, RTPFlags, window, checksum, urgentPointer,
                         optBytes);
  }



  /**
   * Retrieves the source port for this RTP packet.  The port will be encoded in
   * the lower 16 bits of the returned int value.
   *
   * @return  The source port for this RTP packet.
   */
  public int getsrcPort()
  {
    return srcPort;
  }



  /**
   * Retrieves the destination port for this RTP packet.  The port will be
   * encoded in the lower 16 bits of the returned int value.
   *
   * @return  The destination port for this RTP packet.
   */
  public int getdstnPort()
  {
    return dstnPort;
  }



  /**
   * Retrieves the sequence number for this RTP packet.  The value will use all
   * 32 bits of the returned int value.
   *
   * @return  The sequence number for this RTP packet.
   */
  public int getseqNum()
  {
    return seqNum;
  }



  /**
   * Retrieves the acknowledgement number for this RTP packet.  The value will
   * use all 32 bits of the returned int value.
   *
   * @return  The acknowledgement number for this RTP packet.
   */
  public int getAcknowledgementNumber()
  {
    return ackNumber;
  }



  /**
   * Retrieves the data offset for this RTP header.  The value will be encoded
   * in the lower 4 bits of the provided value and will indicate the number of
   * 32-bit words contained in the header.
   *
   * @return  The data offset for this RTP header.
   */
  public int getDataOffset()
  {
    return dataOffset;
  }



  /**
   * Retrieves the length in bytes of this RTP header.
   *
   * @return  The length in bytes of this RTP header.
   */
  public int getHeaderLength()
  {
    return (dataOffset * 4);
  }



  /**
   * Retrieves the set of flags for this RTP header.  The flags will be encoded
   * in the lower 6 bits of the returned int value, and individual flags may
   * be checked by ANDing them with the values of the
   * <CODE>RTP_FLAG_MASK_*</CODE> constants.
   *
   * @return  The set of flags for this RTP header.
   */
  public int getRTPFlags()
  {
    return RTPFlags;
  }



  /**
   * Retrieves the window for this RTP header.  The value will be encoded in
   * the lower 16 bits of the returned int value.
   *
   * @return  The window for this RTP header.
   */
  public int getWindow()
  {
    return window;
  }



  /**
   * Retrieves the checksum for this RTP header.  The value will be encoded in
   * the lower 16 bits of the returned int value.
   *
   * @return  The checksum for this RTP header.
   */
  public int getChecksum()
  {
    return checksum;
  }


  /**
   * Retrieves the urgent pointer for this RTP header.  The window will be
   * encoded in the lower 16 bits of the returned int value.
   *
   * @return  The urgent pointer for this RTP header.
   */
  public int getUrgentPointer()
  {
    return urgentPointer;
  }



  /**
   * Retrieves the data for any options associated with this RTP header.
   *
   * @return  The data for any options associated with this RTP header.
   */
  public byte[] getoptBytes()
  {
    return optBytes;
  }
}

