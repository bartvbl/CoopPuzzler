package server;

import java.awt.ScrollPane;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

public class ServerMain implements Runnable{
	private ServerSocket serverSocket = null;
	private ArrayList<ClientHandlingThread> clients = new ArrayList<ClientHandlingThread>();
	private ServerWindow window;
	
	public void initialize()
	{
		this.window = new ServerWindow();
	}
	
	public void run()
	{
		Socket clientSocket = null;
		try {
			this.window.writeMessage("starting server..");
			this.serverSocket = new ServerSocket(4444);
			clientSocket = this.serverSocket.accept();
		} catch (IOException e) {
		    System.out.println("Accept failed: 4444");
		    System.exit(-1);
		} finally
		{
			this.window.writeMessage("closing connection..");
			try {
				clientSocket.close();
				serverSocket.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "The final catch went horribly wrong!");
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void broadcastMessage(String message)
	{
		
	}
}
