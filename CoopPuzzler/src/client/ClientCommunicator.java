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

import javax.swing.JOptionPane;

import client.gui.FeedbackProvider;

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
			FeedbackProvider.showFailedToFindServerMessage();
			System.exit(0);
		} catch (IOException e) {
			FeedbackProvider.showConnectingToServerFailedMessage();
			System.exit(0);
		}
	}

	/**Client side of the handshake. Only protocol test for now */
	private void shakeHands(InetAddress server) throws IOException{
		socket = new Socket(server,PORT);
		if(!socket.isConnected()){
			throw new IOException("Server not found.");
		}
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
		if(!waitForInput() || !input.readLine().equals(ProtocolConstants.HANDSHAKE_SYN)){
			output.write(ProtocolConstants.HANDSHAKE_CANCEL);
			flush();
			socket.close();
			return;
		}
		output.write(ProtocolConstants.HANDSHAKE_SYNACK);
		flush();
		if(!waitForInput() || !input.readLine().equals(ProtocolConstants.HANDSHAKE_ACK)){
			output.write(ProtocolConstants.HANDSHAKE_CANCEL);
			flush();
			socket.close();
			return;
		}
		if(!waitForInput() || !input.readLine().equals(BOARD_TRANSFER_START)){
			output.write(ProtocolConstants.HANDSHAKE_CANCEL);
			flush();
			socket.close();
			return;
		}
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
			connected = true;
		} else {
			JOptionPane.showMessageDialog(null, "failed to connect to server!");
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
			output.newLine();
			output.flush();
		} catch (IOException e) {
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
					this.main.sendEventToClient(new BoardUpdateEvent(message));
					message = this.readNextMessage();
				}
				if(message.equals(SESSION_TEARDOWN)){
					output.write(SESSION_TEARDOWN_ACK);
					flush();
					socket.close();
					main.serverRequestsShutDown();
					return;
				}
				ArrayList<BoardUpdateEvent> outgoing = getBoardUpdateEventQueue();
				synchronized(outgoing)
				{
					for(BoardUpdateEvent event : outgoing){
						output.write(event.toString());
						output.newLine();
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
