package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.lwjgl.util.Timer;

import common.ProtocolConstants;
import common.BoardUpdateEvent;
import common.puzzle.Board;
import common.puzzle.PuzzleField;

public class ClientHandler implements Runnable,ProtocolConstants {
	public final int clientID;

	private Socket clientSocket;
	private BufferedWriter output;
	private BufferedReader input;
	private ServerMain main;
	private final Timer pingTimer;
	private boolean shutdownRequested = false;
	/** 
	 * When this task is waiting for a response from client,
	 * it will check again this many times a second.
	 */
	private static final int FREQUENCY = 10;

	private ArrayList<BoardUpdateEvent> outgoingMessageQueue = new ArrayList<BoardUpdateEvent>();

	public ClientHandler(int clientID, ServerMain main,Socket clientSocket)
	{
		this.clientID = clientID;
		pingTimer = new Timer();
		this.clientSocket = clientSocket;
		this.main = main;
		try {
			this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			this.output = new BufferedWriter(new PrintWriter(clientSocket.getOutputStream()));
		} catch (IOException e) {
			this.main.writeMessageInWindow(e.getMessage());
		}
	}

	public void run()
	{
		try {
			output.write(HANDSHAKE_SYN);
			flush();
			if(!waitForInput() ||!input.readLine().equals(HANDSHAKE_SYNACK)){
				output.write(HANDSHAKE_CANCEL);
				flush();
				clientSocket.close();
				return;
			}
			output.write(HANDSHAKE_ACK);
			output.newLine();
			this.sendBoard();
			String request = "";
			Timer.tick();
			pingTimer.reset();
			pingTimer.resume();
			boolean isRunning = true;
			
			while(!request.equals(SESSION_TEARDOWN) && !shutdownRequested && isRunning){
				request = (input.ready() ? input.readLine() : "");
				if(request != null) {
					if(request.startsWith(BOARD_UPDATE)){
						if(!main.processMessage(new BoardUpdateEvent(request))){
							output.write(BOARD_UPDATE_REJECT);
							flush();
						}
					} else if(request.equals(PING)) {
						output.write(PING_REPLY);
						pingTimer.reset();
						pingTimer.resume();
						flush();
					}
					Timer.tick();
					if(pingTimer.getTime() > ProtocolConstants.PING_SERVER_TIMEOUT) {
						isRunning = false;
					}
				}
				processEvents();
				try {Thread.sleep(1000/FREQUENCY);} catch (InterruptedException e) {this.main.writeMessageInWindow(e.getMessage());e.printStackTrace();}
			}
			if(shutdownRequested){
				output.write(SESSION_TEARDOWN);
				flush();
				if(!waitForInput() || input.readLine().equals(SESSION_TEARDOWN_ACK)){
					clientSocket.close();
				}
			} else{
				output.write(SESSION_TEARDOWN_ACK);
				flush();
				clientSocket.close();
			}
			main.removeHandler(this.clientID);//Unsubscribe this handler from updates and mark it for garbage collection.
		} catch (IOException e) {
			this.main.writeMessageInWindow(e.getMessage());
		}
	}

	private void sendBoard() throws IOException
	{
		output.write(BOARD_TRANSFER_START);
		output.newLine();
		Board board = this.main.puzzleTable.board;
		output.write(BOARD_SIZE + " " + board.sizeX + " " + board.sizeY);
		flush();
		for(int x = 0; x < board.sizeX; x++)
		{
			for(int y = 0; y < board.sizeY; y++)
			{
				output.write(BOARD_FIELD + " " + x + " " + y + " " + table[x][y].toString());
				output.newLine();
			}
//			output.flush(); //periodic flush
		}
		output.write(BOARD_TRANSFER_END);
		flush();
	}

	private void processEvents() throws IOException
	{
		BoardUpdateEvent event = this.getNextMessage();
		while(event != null)
		{
			output.write(event.toString());
			flush();
			event = this.getNextMessage();
		}
	}
	private BoardUpdateEvent getNextMessage()
	{
		BoardUpdateEvent message = null;
		synchronized(this.outgoingMessageQueue)
		{
			message = (this.outgoingMessageQueue.isEmpty() ? null : this.outgoingMessageQueue.remove(0));
		}
		return message;
	}

	public void broadcastUpdateToClient(BoardUpdateEvent event)
	{
		synchronized(this.outgoingMessageQueue)
		{
			this.outgoingMessageQueue.add(event);
		}
	}

	public void initateShutdown(){
		shutdownRequested = true;
	}
	
	private boolean waitForInput() throws IOException{
		int waits = 0;
		while(!input.ready() && waits < HANDSHAKE_TIMEOUT/(1000/FREQUENCY)){
			waits++;
			try {Thread.sleep(1000/FREQUENCY);} catch (InterruptedException e) {this.main.writeMessageInWindow(e.getMessage());}
		}
		return input.ready();
	}
	
	private void flush() throws IOException{
		output.newLine();
		output.flush();
	}
	
	public String toString(){
		return clientSocket.getRemoteSocketAddress().toString();
	}
}

