package client;

public class MainLoopThread implements Runnable{
	private ClientMain main;

	public MainLoopThread(ClientMain main)
	{
		this.main = main;
	}

	@Override
	public void run() {
		main.doMainLoop();
	}
}
