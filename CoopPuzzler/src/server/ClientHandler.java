package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
	private Socket clientSocket;
	private BufferedWriter output;
	private BufferedReader input;
	
	private ArrayList<String> messagesToProcess = new ArrayList<String>();
	
	public ClientHandler(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
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
			output.write("INVITE");
			output.newLine();
			output.flush();
			int waits = 0;
			while(!input.ready() && waits < 2000){
				waits++;
				try {Thread.sleep(100);} catch (InterruptedException e) {}
			}
			if(!input.ready() || !input.readLine().equals("ACK") || waits >= 2000){
				output.write("CANCEL");
				output.newLine();
				output.flush();
				clientSocket.close();
				return;
			}
			output.write("ACK");
			output.newLine();
			output.write("BOARD");
			output.newLine();
			output.flush();
			//TODO: Handle giving clients the board data.
			String request = "";
			while(!request.equals("BYE")){
				while(!input.ready()){
					try {Thread.sleep(100);} catch (InterruptedException e) {}
				}
				request = input.readLine();
				//TODO: Handle board update requests from clients.
				processMessages();
			}
			output.write("BYE");
			output.newLine();
			output.flush();
			clientSocket.close();
			
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void processMessages()
	{
		String message = this.getNextMessage();
		while(message != null)
		{
			
			message = this.getNextMessage();
		}
	}
	private String getNextMessage()
	{
		String message = null;
		synchronized(this.messagesToProcess)
		{
			message = (this.messagesToProcess.isEmpty() ? null : this.messagesToProcess.remove(0));
		}
		return message;
	}
	
	public void broadcastMessageToClient(String message)
	{
		synchronized(this.messagesToProcess)
		{
			this.messagesToProcess.add(message);
		}
	}
}
