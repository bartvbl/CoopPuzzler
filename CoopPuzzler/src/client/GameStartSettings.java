package client;

public class GameStartSettings {
	public final boolean isOnlineGame;
	public final String serverHostName;
	public final String puzzleFileSrc;

	public GameStartSettings(boolean isOnlineGame, String serverHostName, String puzzleFileSrc) {
		this.isOnlineGame = isOnlineGame;
		this.serverHostName = serverHostName;
		this.puzzleFileSrc = puzzleFileSrc;
	}
}
