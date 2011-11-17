package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import common.BoardUpdateEvent;
import common.ProtocolConstants;

public class ClientCommunicator implements ProtocolConstants,Runnable{
	private Socket socket;
	private BufferedReader input;
	private BufferedWriter output;
	private final ClientMain main;
	private boolean connected = false;
	/** When waiting for a response from the server, check back this many times a second */
	private static final int FREQUENCY = 10;
	private String board;

	public ClientCommunicator(ClientMain main)
	{
		this.main = main;
	}

	private ArrayList<BoardUpdateEvent> getBoardUpdateEventQueue()
	{
		return this.main.getEventQueueToServer();
	}

	public void init(InetAddress server)
	{
		try {
			shakeHands(server);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**Client side of the handshake. Only protocol test for now */
	private void shakeHands(InetAddress server) throws IOException{
		Socket socket = new Socket(server,4444);
		if(!socket.isConnected()){
			throw new IOException("Server not found.");
		}
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
		int waits = 0;
		while(!input.ready() && waits < ProtocolConstants.HANDSHAKE_TIMEOUT/FREQUENCY){
			waits++;
			try {Thread.sleep(1000/FREQUENCY);} catch (InterruptedException e) {}
		}
		if(!input.ready() || !input.readLine().equals(ProtocolConstants.HANDSHAKE_SYN) || waits >= ProtocolConstants.HANDSHAKE_TIMEOUT/FREQUENCY){
			output.write(ProtocolConstants.HANDSHAKE_CANCEL);
			output.newLine();
			output.flush();
			socket.close();
			return;
		}
		output.write(ProtocolConstants.HANDSHAKE_SYNACK);
		output.newLine();
		output.flush();
		waits = 0;
		while(!input.ready() && waits < ProtocolConstants.HANDSHAKE_TIMEOUT/FREQUENCY){
			waits++;
			try {Thread.sleep(1000/FREQUENCY);} catch (InterruptedException e) {}
		}
		if(!input.ready() || !input.readLine().equals(ProtocolConstants.HANDSHAKE_ACK) || waits >= ProtocolConstants.HANDSHAKE_TIMEOUT/FREQUENCY){
			output.write(ProtocolConstants.HANDSHAKE_CANCEL);
			output.newLine();
			output.flush();
			socket.close();
			return;
		}
		/* While the server doesn't have a board, don't expect one to be sent.
		if(!input.ready() || !input.readLine().equals(BOARD_TRANSFER_START)){
			output.write(ProtocolConstants.HANDSHAKE_CANCEL);
			output.newLine();
			output.flush();
			socket.close();
			return;
		}
		String board = "";
		String message = input.readLine();
		while(!message.equals(BOARD_TRANSFER_END)){
			board += message;
			message = input.readLine();
		}*/
		connected = true;
	}

	/** Whether the Communicator has an active connection. */
	public boolean isConnected(){
		return connected;
	}
	
	public String getBoard(){
		return board;
	}

	/** Close the session */
	public void close(){
		if(!connected){return;}
		try {
			output.write(SESSION_TEARDOWN);
			output.newLine();
			output.flush();
		} catch (IOException e) {//The protocol calls for the server to close the TCP connection. This raises IOExceptions.
			System.out.println(e.getMessage());
		}
		connected = false;

	}

	/** Listens for board update events from the server and fills the incoming event queue.
	 * 	Also transmits any events outstanding in main's event queue. 
	 *  Ensure that connection is established before using. 
	 */
	public void run(){
		while(connected){
			try {
				String message = this.readNextMessage();
				while(message != null && message.startsWith(BOARD_UPDATE)){
					System.out.println("event received from server");
					this.main.sendEventToClient(new BoardUpdateEvent(message));
					message = this.readNextMessage();
				}
				ArrayList<BoardUpdateEvent> outgoing = getBoardUpdateEventQueue();
				synchronized(outgoing)
				{
					for(BoardUpdateEvent event : outgoing){
						System.out.println("writing to server");
						output.write(event.toString());
					}
				}
				output.flush();
				Thread.sleep(1000/FREQUENCY);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private String readNextMessage() throws IOException
	{
		String message;
		if(input.ready())
		{
			message = input.readLine();
		} else {
			message = "";
		}
		return message;
	}


}
