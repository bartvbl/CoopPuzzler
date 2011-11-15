package server;

import java.awt.ScrollPane;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import common.BoardUpdateEvent;

public class ServerMain implements Runnable{
	private ServerSocket serverSocket = null;
	private ServerWindow window;
	private ExecutorService threadpool;
	private ArrayList<ClientHandler> handlers;

	public void initialize()
	{
		this.window = new ServerWindow();
		this.handlers = new ArrayList<ClientHandler>();
		this.threadpool = Executors.newCachedThreadPool();
		try{this.serverSocket = new ServerSocket(4444);}
		catch(IOException e){e.printStackTrace();}
	}

	public void run()
	{
		Socket clientSocket = null;
		this.window.writeMessage("starting server..");
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
