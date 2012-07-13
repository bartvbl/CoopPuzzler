package core;
import server.ServerMain;


public class ServerRunner {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerMain main = new ServerMain();
		main.initialize();
		Thread server = new Thread(main);
		server.start();
	}
}
