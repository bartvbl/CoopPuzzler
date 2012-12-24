package client;

public class GameSettings {

	public static String puzzleFileSrc;
	public static OperationMode operationMode;

	public GameSettings(GameStartSettings gameSettings) {
		puzzleFileSrc = gameSettings.puzzleFileSrc;
		operationMode = gameSettings.operationMode;
	}

}
