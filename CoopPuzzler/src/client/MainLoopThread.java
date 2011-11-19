package client;

public class MainLoopThread extends Thread {
	private ClientMain main;

	public MainLoopThread(ClientMain main)
	{
		this.main = main;
	}

	public void run() {
		this.main.doMainLoop();
	}
}
