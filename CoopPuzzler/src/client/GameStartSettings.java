package client;

public class GameStartSettings {
	public final String serverHostName;
	public final String puzzleFileSrc;
	public OperationMode operationMode;
	public int rows = -1;
	public int columns = -1;

	public GameStartSettings(OperationMode operationMode, String serverHostName, String puzzleFileSrc) {
		this.serverHostName = serverHostName;
		this.puzzleFileSrc = puzzleFileSrc;
		this.operationMode = operationMode;
	}
}
