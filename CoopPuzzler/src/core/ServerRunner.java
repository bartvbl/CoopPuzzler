package core;
import common.ProtocolConstants;

import server.ServerMain;


public class ServerRunner {
	@Deprecated //The server is integrated with the client as of 1.6 and onwards.
	public static void main(String[] args) {
		ServerMain main = new ServerMain();
		main.initialize("res/puzzle.txt", ProtocolConstants.PORT);
		Thread server = new Thread(main);
		server.start();
	}
}
