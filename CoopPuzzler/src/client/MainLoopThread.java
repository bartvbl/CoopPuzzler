package client;

import org.lwjgl.opengl.Display;

public class MainLoopThread implements Runnable{
	private ClientMain main;

	public MainLoopThread(ClientMain main)
	{
		this.main = main;
	}

	@Override
	public void run() {
		while(!Display.isCloseRequested())
		{
			main.window.doFrame();
		}
	}
}
