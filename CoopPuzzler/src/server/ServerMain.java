package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.AutoSaver;
import common.BoardUpdateEvent;
import common.ProtocolConstants;
import common.PuzzleField;
import common.PuzzleTable;

public class ServerMain implements Runnable{
	private ServerSocket serverSocket = null;
	private ServerWindow window;
	private ExecutorService threadpool;
	private ArrayList<ClientHandler> handlers;
	public final PuzzleTable puzzleTable;

	public ServerMain()
	{
		this.puzzleTable = new PuzzleTable();
	}

	public void initialize()
	{
		this.puzzleTable.loadMapFromLocalFile();
		this.window = new ServerWindow(this);
		this.handlers = new ArrayList<ClientHandler>();
		this.threadpool = Executors.newCachedThreadPool();
		try{this.serverSocket = new ServerSocket(ProtocolConstants.PORT);}
		catch(IOException e){e.printStackTrace();}
		this.writeMessageInWindow("listening on port " + ProtocolConstants.PORT);
		new AutoSaver(this.puzzleTable.puzzleTable);
	}

	public void writeMessageInWindow(String message)
	{
		this.window.writeMessage(message);
	}

	public void run()
	{
		Socket clientSocket = null;
		this.window.writeMessage("server started.");
		while(true){
			try {
				clientSocket = this.serverSocket.accept();
				ClientHandler handler = new ClientHandler(this, clientSocket);
				handlers.add(handler);
				this.threadpool.execute(handler);
				this.window.writeMessage("Accepted client from " + handler.toString());
			} catch (IOException e) {
				System.err.println("Accept failed: " + ProtocolConstants.PORT );
				e.printStackTrace();
				return;
			} 
		}
	}

	/** 
	 * Process a BoardUpdateEvent proposed by a client. 
	 * Verifies that it references a real board field which is not filled, and updates state if valid. 
	 * Returns whether or not the proposed update was accepted.
	 */
	public synchronized boolean processMessage(BoardUpdateEvent event)
	{
		if(!validateField(event)){return false;}
		PuzzleField targetField = this.puzzleTable.puzzleTable[event.getRow()][event.getColumn()];
		if(targetField.isFilled){return false;}
		targetField.setFieldTextColour(event.getColour());
		targetField.setNewCharacterValue(event.getCharacterValue());
		for(ClientHandler handler : handlers){
			handler.broadcastUpdateToClient(event);
		}
		return true;
	}

	private boolean validateField(BoardUpdateEvent event) {
		return event.getRow()>= 0 &&
		event.getRow() <= this.puzzleTable.puzzleTable.length &&
		event.getColumn() >= 0 &&
		event.getColumn() <= this.puzzleTable.puzzleTable[event.getRow()].length
		;
	}

	public synchronized void removeHandler(ClientHandler handler){
		handlers.remove(handler);
		this.window.writeMessage("Closing session from " + handler.toString());
	}

	public void shutdown() {
		this.window.writeMessage("Attempting graceful shutdown.");
		threadpool.shutdown();
		for(ClientHandler handler : handlers){
			handler.initateShutdown();
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(handlers.size() > 0){
			try{Thread.sleep(100);}catch(InterruptedException e){}
		}
		System.exit(0);
	}
}
