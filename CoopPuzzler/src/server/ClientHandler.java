package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
	private Socket clientSocket;
	private BufferedWriter output;
	private BufferedReader input;
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
			int waits = 0;
			while(!input.ready() && waits < 200){
				waits++;
				try {Thread.sleep(100);} catch (InterruptedException e) {}
			}
			String response = input.readLine();
			if(!response.equals("ACK") || waits >= 200){
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
			//TODO: Handle giving clients the board data.
			String request = "";
			while(!request.equals("BYE")){
				while(!input.ready()){
					try {Thread.sleep(100);} catch (InterruptedException e) {}
				}
				request = input.readLine();
				//TODO: Handle board update requests from clients.
			}
			output.write("BYE ACK");
			output.newLine();
			output.flush();
			clientSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
