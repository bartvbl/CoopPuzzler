package client;


import common.PuzzleTable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain {
	private ClientWindow window;
	private PuzzleTable puzzleTable;
	private PuzzleDrawer puzzleDrawer;
	private InputHandler inputHandler;
	
	private Socket socket;
	private BufferedReader input;
	private BufferedWriter output;
	public ClientMain()
	{
//		try {
//			shakeHands(InetAddress.getLocalHost());
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		this.window = new ClientWindow(this);
		this.puzzleTable = new PuzzleTable();
		this.puzzleTable.initialize();
		this.inputHandler = new InputHandler(this.window, this.puzzleTable.puzzleTable[0].length, this.puzzleTable.puzzleTable.length);
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
		while(!input.ready() && waits < 2000){
			waits++;
			try {Thread.sleep(100);} catch (InterruptedException e) {}
		}
		if(!input.ready() || !input.readLine().equals("INVITE") || waits >= 2000){
			output.write("CANCEL");
			output.newLine();
			output.flush();
			socket.close();
			return;
		}
		output.write("ACK");
		output.newLine();
		output.flush();
		waits = 0;
		while(!input.ready() && waits < 2000){
			waits++;
			try {Thread.sleep(100);} catch (InterruptedException e) {}
		}
		if(!input.ready() || !input.readLine().equals("ACK") || waits >= 2000){
			output.write("CANCEL");
			output.newLine();
			output.flush();
			socket.close();
			return;
		}
		
		//Having completed the handshake, log this to sysout and immediately close the connection.
		System.out.println("Client shook hands successfully.");
		output.write("BYE");
		output.newLine();
		output.flush();
		socket.close();
	}
}
