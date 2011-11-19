package client;

public class GameStarter implements Runnable{

	private ClientMain main;
	private String hostName;
	private boolean isOnline;
	
	public GameStarter(ClientMain main, boolean isOnline, String hostName)
	{
		this.main = main;
		this.hostName = hostName;
		this.isOnline = isOnline;
	}
	
	@Override
	public void run() {
		main.window.createOpenGLContext();
		main.runGame(isOnline, hostName);
	}

}
