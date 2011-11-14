package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import common.BoardUpdateEvent;
import common.ProtocolConstants;

public class ClientCommunicator implements Runnable {
	private Socket socket;
	private BufferedReader input;
	private BufferedWriter output;
	private final ClientMain main;
	/** When waiting for a response from the server, check back this many times a second */
	private static final int FREQUENCY = 10;
	
	public ClientCommunicator(ClientMain main)
	{
		this.main = main;
	}
	
	private ArrayList<BoardUpdateEvent> getBoardUpdateEventQueue()
	{
		return this.main.getEventQueueToServer();
	}
	
	public void run()
	{
		try {
			shakeHands(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**Client side of the handshake. Only protocol test for now */
	private void shakeHands(InetAddress server) throws IOException{
		Socket socket = new Socket(server,4444);
		if(!socket.isConnected()){
			throw new IOException("Server not found.");
		}
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
		int waits = 0;
		while(!input.ready() && waits < ProtocolConstants.HANDSHAKE_TIMEOUT/FREQUENCY){
			waits++;
			try {Thread.sleep(1000/FREQUENCY);} catch (InterruptedException e) {}
		}
		if(!input.ready() || !input.readLine().equals(ProtocolConstants.HANDSHAKE_SYN) || waits >= 2000){
			output.write(ProtocolConstants.HANDSHAKE_CANCEL);
			output.newLine();
			output.flush();
			socket.close();
			return;
		}
		output.write(ProtocolConstants.HANDSHAKE_SYNACK);
		output.newLine();
		output.flush();
		waits = 0;
		while(!input.ready() && waits < ProtocolConstants.HANDSHAKE_TIMEOUT/FREQUENCY){
			waits++;
			try {Thread.sleep(1000/FREQUENCY);} catch (InterruptedException e) {}
		}
		if(!input.ready() || !input.readLine().equals(ProtocolConstants.HANDSHAKE_ACK) || waits >= 2000){
			output.write(ProtocolConstants.HANDSHAKE_CANCEL);
			output.newLine();
			output.flush();
			socket.close();
			return;
		}
		
		//Having completed the handshake, log this to sysout and immediately close the connection.
		System.out.println("Client shook hands successfully.");
		output.write(ProtocolConstants.SESSION_TEARDOWN);
		output.newLine();
		output.flush();
		socket.close();
	}
}
