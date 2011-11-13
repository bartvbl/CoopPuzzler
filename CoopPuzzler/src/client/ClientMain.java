package client;


import common.ProtocolConstants;
import common.PuzzleTable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain implements ProtocolConstants{
	private ClientWindow window;
	private PuzzleTable puzzleTable;
	private PuzzleDrawer puzzleDrawer;
	private InputHandler inputHandler;
	
	private Socket socket;
	private BufferedReader input;
	private BufferedWriter output;
	/** When waiting for a response from the server, check back this many times a second */
	private static final int FREQUENCY = 10;
	
	public ClientMain()
	{
		try {
			shakeHands(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.window = new ClientWindow(this);
		this.puzzleTable = new PuzzleTable();
		this.puzzleTable.initialize();
		this.inputHandler = new InputHandler(this.puzzleTable.puzzleTable[0].length, this.puzzleTable.puzzleTable.length);
		this.puzzleDrawer = new PuzzleDrawer(this.puzzleTable, this.inputHandler);
		this.window.mainLoop();
	}

	public void doFrame() {
		this.inputHandler.update();
		this.puzzleDrawer.draw();
		this.inputHandler.handleSelection();
	}

	/**Client side of the handshake. Only protocol test for now */
	public void shakeHands(InetAddress server) throws IOException{
		Socket socket = new Socket(server,4444);
		if(!socket.isConnected()){
			throw new IOException("Server not found.");
		}
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
		int waits = 0;
		while(!input.ready() && waits < HANDSHAKE_TIMEOUT/FREQUENCY){
			waits++;
			try {Thread.sleep(1000/FREQUENCY);} catch (InterruptedException e) {}
		}
		if(!input.ready() || !input.readLine().equals(HANDSHAKE_SYN) || waits >= 2000){
			output.write(HANDSHAKE_CANCEL);
			output.newLine();
			output.flush();
			socket.close();
			return;
		}
		output.write(HANDSHAKE_SYNACK);
		output.newLine();
		output.flush();
		waits = 0;
		while(!input.ready() && waits < HANDSHAKE_TIMEOUT/FREQUENCY){
			waits++;
			try {Thread.sleep(1000/FREQUENCY);} catch (InterruptedException e) {}
		}
		if(!input.ready() || !input.readLine().equals(HANDSHAKE_ACK) || waits >= 2000){
			output.write(HANDSHAKE_CANCEL);
			output.newLine();
			output.flush();
			socket.close();
			return;
		}
		
		//Having completed the handshake, log this to sysout and immediately close the connection.
		System.out.println("Client shook hands successfully.");
		output.write(SESSION_TEARDOWN);
		output.newLine();
		output.flush();
		socket.close();
	}
}
