package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import common.ProtocolConstants;

public class ClientHandler implements Runnable,ProtocolConstants {
	private Socket clientSocket;
	private BufferedWriter output;
	private BufferedReader input;
	/** 
	 * When this task is waiting for a response from client,
	 * it will check again this many times a second.
	 */
	private static final int FREQUENCY = 10;
	
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
			output.write(BOARD_TRANSFER_START);
			output.newLine();
			output.flush();
			//TODO: Handle giving clients the board data.
			String request = "";
			while(!request.equals(SESSION_TEARDOWN)){
				while(!input.ready()){
					try {Thread.sleep(100);} catch (InterruptedException e) {}
				}
				request = input.readLine();
				//TODO: Handle board update requests from clients.
				processMessages();
			}
			output.write(SESSION_TEARDOWN_ACK);
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
