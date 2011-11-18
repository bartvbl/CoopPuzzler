package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import common.BoardUpdateEvent;
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
		this.window = new ServerWindow();
		this.handlers = new ArrayList<ClientHandler>();
		this.threadpool = Executors.newCachedThreadPool();
		try{this.serverSocket = new ServerSocket(4444);}
		catch(IOException e){e.printStackTrace();}
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
				this.window.writeMessage("Accepted client " + clientSocket.toString());
			} catch (IOException e) {
				System.out.println("Accept failed: 4444");
				e.printStackTrace();
				System.exit(-1);
			} 
		}
	}

	public synchronized void broadcastMessage(BoardUpdateEvent event)
	{
		for(ClientHandler handler : handlers){
			handler.broadcastUpdateToClient(event);
		}
	}
}
