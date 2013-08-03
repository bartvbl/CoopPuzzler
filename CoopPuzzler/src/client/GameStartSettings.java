package client;

public class GameStartSettings {
	public final String serverHostName;
	public final String puzzleFileSrc;
	public final int serverPort;
	public OperationMode operationMode;
	public int rows = -1;
	public int columns = -1;
	public boolean startWithEmptyEditor = true;

	public GameStartSettings(OperationMode operationMode, String serverHostName, int serverPort, String puzzleFileSrc) {
		this.serverHostName = serverHostName;
		this.puzzleFileSrc = puzzleFileSrc;
		this.operationMode = operationMode;
		this.serverPort = serverPort;
	}
}
