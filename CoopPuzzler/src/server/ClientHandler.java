package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import common.ProtocolConstants;
import common.BoardUpdateEvent;
import common.PuzzleField;

public class ClientHandler implements Runnable,ProtocolConstants {
	private Socket clientSocket;
	private BufferedWriter output;
	private BufferedReader input;
	private ServerMain main;
	/** 
	 * When this task is waiting for a response from client,
	 * it will check again this many times a second.
	 */
	private static final int FREQUENCY = 10;

	private ArrayList<BoardUpdateEvent> outgoingMessageQueue = new ArrayList<BoardUpdateEvent>();

	public ClientHandler(ServerMain main,Socket clientSocket)
	{
		this.clientSocket = clientSocket;
		this.main = main;
		try {
			this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			this.output = new BufferedWriter(new PrintWriter(clientSocket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run()
	{
		try {
			output.write(HANDSHAKE_SYN);
			output.newLine();
			output.flush();
			int waits = 0;
			while(!input.ready() && waits < HANDSHAKE_TIMEOUT/FREQUENCY){
				waits++;
				try {Thread.sleep(1000/FREQUENCY);} catch (InterruptedException e) {}
			}
			if(!input.ready() || !input.readLine().equals(HANDSHAKE_SYNACK) || waits >= HANDSHAKE_TIMEOUT/FREQUENCY){
				output.write(HANDSHAKE_CANCEL);
				output.newLine();
				output.flush();
				clientSocket.close();
				return;
			}
			output.write(HANDSHAKE_ACK);
			output.newLine();
			this.sendBoard();
			String request = "";
			while(!request.equals(SESSION_TEARDOWN)){
				processEvents();
				if(request != null && request.startsWith(BOARD_UPDATE)){
					main.broadcastMessage(new BoardUpdateEvent(request));
				}
				try {Thread.sleep(100);} catch (InterruptedException e) {}
				request = (input.ready() ? input.readLine() : "");
			}

			output.write(SESSION_TEARDOWN_ACK);
			output.newLine();
			output.flush();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendBoard() throws IOException
	{
		output.write(BOARD_TRANSFER_START);
		output.newLine();
		PuzzleField[][] table = this.main.puzzleTable.puzzleTable;
		output.write(BOARD_SIZE + " " + table.length + " " + table[0].length);
		output.flush();
		for(int row = 0; row < table.length; row++)
		{
			for(int column = 0; column < table[0].length; column++)
			{
				output.write(BOARD_FIELD + " " + row + " " + column + " " + table[row][column].toString());
				output.newLine();
			}
			output.flush(); //periodic flush
		}
		output.write(BOARD_TRANSFER_END);
		output.newLine();
		output.flush();
	}
	
	private void processEvents() throws IOException
	{
		BoardUpdateEvent event = this.getNextMessage();
		while(event != null)
		{
			output.write(event.toString());
			output.newLine();
			event = this.getNextMessage();
		}
		output.flush();
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
}
