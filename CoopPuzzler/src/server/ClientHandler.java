package server;

import java.net.Socket;

public class ClientHandler implements Runnable {
	private Socket clientSocket;
	public ClientHandler(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
	}
	
	public void run()
	{
		
	}
	
	
}
