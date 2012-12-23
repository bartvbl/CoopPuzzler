package client;

public class GameStartSettings {
	public final String serverHostName;
	public final String puzzleFileSrc;
	public OperationMode operationMode;

	public GameStartSettings(OperationMode operationMode, String serverHostName, String puzzleFileSrc) {
		this.serverHostName = serverHostName;
		this.puzzleFileSrc = puzzleFileSrc;
		this.operationMode = operationMode;
	}
}
