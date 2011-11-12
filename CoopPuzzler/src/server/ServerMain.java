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

public class ServerMain implements Runnable{
	private ServerSocket serverSocket = null;
	private ServerWindow window;
	private ExecutorService threadpool;

	public void initialize()
	{
		this.window = new ServerWindow();
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
				ClientHandler handler = new ClientHandler(clientSocket);
				this.threadpool.execute(handler);
			} catch (IOException e) {
				System.out.println("Accept failed: 4444");
				e.printStackTrace();
				System.exit(-1);
			} 
//			finally
//			{
//				this.window.writeMessage("closing connection..");
//				try {
//					clientSocket.close();
//					serverSocket.close();
//				} catch (IOException e) {
//					JOptionPane.showMessageDialog(null, "The final catch went horribly wrong!");
//					e.printStackTrace();
//				}
//			}
		}
	}

	public synchronized void broadcastMessage(String message)
	{

	}
}
