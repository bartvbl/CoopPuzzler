package client;

import common.PuzzleTable;

public class ClientMain {
	private ClientWindow window;
	private PuzzleTable puzzleTable;
	private PuzzleDrawer puzzleDrawer;
	private InputHandler inputHandler;
	
	public ClientMain()
	{
		this.window = new ClientWindow(this);
		this.puzzleTable = new PuzzleTable();
		this.puzzleTable.initialize();
		this.inputHandler = new InputHandler(this.puzzleTable.puzzleTable[0].length, this.puzzleTable.puzzleTable.length);
		this.puzzleDrawer = new PuzzleDrawer(this.puzzleTable, this.inputHandler);
		this.window.mainLoop();
	}
	
	public void doFrame() {
		this.inputHandler.update();
		this.puzzleDrawer.draw();
		this.inputHandler.handleSelection();
	}

}
