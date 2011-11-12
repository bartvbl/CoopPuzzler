package client;

public class ClientMain {
	private ClientWindow window;
	
	public ClientMain()
	{
		this.window = new ClientWindow(this);
		this.window.mainLoop();
	}
	
	public void doFrame() {
		// TODO Auto-generated method stub
		
	}

}
