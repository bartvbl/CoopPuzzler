package client.net;

import static common.ProtocolConstants.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.lwjgl.util.Timer;

import client.ClientMain;
import client.gui.FeedbackProvider;

import common.BoardUpdateEvent;
import common.ProtocolConstants;

public class ClientCommunicator implements Runnable{
	private Socket socket;
	private boolean isExpectingPingReply = false;
	private Timer pingTimer;
	private BufferedReader input;
	private BufferedWriter output;
	private final ClientMain main;
	private boolean connected = false;
	/** When waiting for a response from the server, check back this many times a second */
	private static final int FREQUENCY = 10;

	public ClientCommunicator(ClientMain main)
	{
		this.main = main;
		this.pingTimer = new Timer();
		pingTimer.pause();
	}

	private ArrayList<BoardUpdateEvent> getBoardUpdateEventQueue()
	{
		return this.main.getEventQueueToServer();
	}

	public void init(InetAddress server, int serverPort)
	{
		System.out.println("initializing..");
		try {
			openSession(server, serverPort);
		} catch (UnknownHostException e) {
			FeedbackProvider.showFailedToFindServerMessage();
			e.printStackTrace();
			System.out.println("Client communicator error: " + e.getMessage());
			System.exit(0);
		} catch (IOException e) {
			FeedbackProvider.showConnectingToServerFailedMessage();
			e.printStackTrace();
			System.out.println("Client communicator error: " + e.getMessage());
			System.exit(0);
		}
	}


	private void openSession(InetAddress server, int serverPort) throws IOException{
		System.out.println("Connecting to server at: " + server + ":" + serverPort);
		socket = new Socket();
		InetSocketAddress address = new InetSocketAddress(server, serverPort);
		socket.connect(address, 5000);
		if(!socket.isConnected()){
			throw new IOException("Server not found.");
		}
		socket.setKeepAlive(false);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new BufferedWriter(new PrintWriter(socket.getOutputStream()));

		shakeHands();
		retrieveBoard();
		
		connected = true;
		System.out.println("Connection to server established.");
		
		Timer.tick();
		pingTimer.reset();
		pingTimer.resume();
	}

	private void shakeHands() throws IOException {
		if(!waitForInput() || !input.readLine().equals(HANDSHAKE_SYN)){
			cancelHandshake();
			return;
		}
		output.write(HANDSHAKE_SYNACK);
		flush();
		if(!waitForInput() || !input.readLine().equals(HANDSHAKE_ACK)){
			cancelHandshake();
			return;
		}
		if(!waitForInput() || !input.readLine().equals(BOARD_TRANSFER_START)){
			cancelHandshake();
			return;
		}
	}

	private void cancelHandshake() throws IOException {
		output.write(HANDSHAKE_CANCEL);
		flush();
		socket.close();
		return;
	}

	private void retrieveBoard() throws IOException {
		String message = input.readLine();
		if(message.startsWith(BOARD_SIZE))
		{
			String[] parts = message.split(" ");
			this.main.puzzleTable.createNewMap(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
			message = input.readLine();
			while(!message.equals(BOARD_TRANSFER_END))
			{
				parts = message.split(" ");
				this.main.puzzleTable.createFieldAt(message, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
				message = input.readLine();
			}
			output.write(BOARD_TRANSFER_ACK);
			flush();
		}
	}

	/** Whether the Communicator has an active connection. */
	public boolean isConnected(){
		return connected;
	}


	/** Close the session */
	public void close(){
		if(!connected){return;}
		try {
			output.write(SESSION_TEARDOWN);
			flush();
		} catch (IOException e) {
			System.out.println("Client communicator error: " + e.getMessage());
			e.printStackTrace();
		}
		connected = false;

	}

	/** Listens for board update events from the server 
	 * 	and fills the incoming event queue. Also transmits any events
	 *  outstanding in main's event queue to the server. Ensure that connection is
	 *  established before using. 
	 */
	public void run(){
		while(connected){
			try {
				String message = this.readNextMessage();
				while(message != null && message.startsWith(BOARD_UPDATE)){
					this.main.sendEventToClient(new BoardUpdateEvent(message));
					message = this.readNextMessage();
				}
				if(message != null) {
					if(message.equals(PING_REPLY)) {
						isExpectingPingReply = false;
						pingTimer.reset();
						pingTimer.resume();
					}
				}
				if(message.equals(SESSION_TEARDOWN)){
					output.write(SESSION_TEARDOWN_ACK);
					flush();
					socket.close();
					FeedbackProvider.showServerShutdownMessage();
					main.serverRequestsShutDown();
					return;
				}
				ArrayList<BoardUpdateEvent> outgoing = getBoardUpdateEventQueue();
				synchronized(outgoing)
				{
					for(BoardUpdateEvent event : outgoing){
						output.write(event.toString());
						flush();
					}
				}
				Timer.tick();
				if((!isExpectingPingReply) && (pingTimer.getTime() > PING_FREQUENCY)) {
					output.write(PING);
					flush();
					pingTimer.reset();
					pingTimer.resume();
					isExpectingPingReply = true;
				}
				if((isExpectingPingReply) && (pingTimer.getTime() > PING_TIMEOUT)) {
					socket.close();
					FeedbackProvider.showConnectionLostMessage();
					main.serverRequestsShutDown();
					connected = false;
				}
				Thread.sleep(1000/FREQUENCY);

			} catch (IOException e) {
				System.out.println("Client communicator error: " + e.getMessage());
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("Client communicator error: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private String readNextMessage() throws IOException
	{
		return (input.ready() ? input.readLine() : "");
	}

	private boolean waitForInput() throws IOException{
		int waits = 0;
		while(!input.ready() && waits < HANDSHAKE_TIMEOUT/(1000/FREQUENCY)){
			waits++;
			try {Thread.sleep(1000/FREQUENCY);} catch (InterruptedException e) {}
		}
		return input.ready();
	}

	private void flush() throws IOException{
		output.newLine();
		output.flush();
	}
}
