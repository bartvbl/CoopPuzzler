package common;
/** 
 * An interface defining some constants of the game's net protocol.
 * The protocol is a connected protocol on top of TCP. 
 * A connection is considered established after a 3-way handshake has been completed
 * and a game board has been transferred to the client (i.e. when a game is in session).  
 * @author Magnus
 *
 */
public interface ProtocolConstants {
	
	/** 
	 * The timeout a handshake participant must grant the other to respond, in milliseconds.
	 * If a participant waits this long and/or their opposite doesn't respond as expected,
	 * transmit {@link HANDSHAKE_CANCEL} and close the session. 
	 */
	public final static int HANDSHAKE_TIMEOUT = 120000;
	/** The session handshake opener (a lá TCP SYN) */
	public final static String HANDSHAKE_SYN = "INVITE";
	/** The correct response to the {@link HANDSHAKE_SYN}. */
	public final static String HANDSHAKE_SYNACK = "ACK";
	/** The final acknowledgement in the handshake */
	public final static String HANDSHAKE_ACK = "ACK";
	public final static String HANDSHAKE_CANCEL = "CANCEL";

	/** The signal to start a game board transfer. */
	public final static String BOARD_TRANSFER_START = "BOARD";
	/** The signal that board transfer was completed. */
	public final static String BOARD_TRANSFER_END = "END BOARD";
	/** 
	 * The signal from client that board was accepted.
	 * If client could not accept the board they were given,
	 * they must initiate session teardown with {@link SESSION_TEARDOWN}. 
	 */
	public final static String BOARD_TRANSFER_ACK = "ACK BOARD";
	
	/**
	 * A full board update consists of BOARD_UPDATE, two integers for board position, one char for the tile value and one integer for the FontColour, all space-separated.
	 * Server responds to board updates by testing their validity and transmitting valid ones to all clients. Clients respond to them by updating their local model.
	 */
	public final static String BOARD_UPDATE = "UPDATE";
	/** Negative acknowledgement of board update. Positive acknowledgement is to repeat the update back. */
	public final static String BOARD_UPDATE_REJECT = "REJECT UPDATE";
	
	/** The signal to initiate session teardown */
	public final static String SESSION_TEARDOWN = "BYE";
	/** The signal to acknowledge a teardown request */
	public final static String SESSION_TEARDOWN_ACK = "BYE";
	
	public static final String BOARD_FIELD = "BOARD_FIELD";
	public static final String BOARD_SIZE = "BOARD SIZE";
}
