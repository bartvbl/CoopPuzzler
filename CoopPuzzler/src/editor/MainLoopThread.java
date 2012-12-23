package editor;

public class MainLoopThread implements Runnable{
	private EditorMain main;

	public MainLoopThread(EditorMain main)
	{
		this.main = main;
	}

	@Override
	public void run() {
		main.doMainLoop();
	}
}
