package common.puzzle;

import common.BoardUpdateEvent;
import common.BoardUpdateListener;
import common.ReferenceUpdater;


public class PuzzleTable implements BoardUpdateListener{
	public Board board;
	
	public void loadMapFromLocalFile(String src)
	{
		this.board = PuzzleLoader.loadTableFromFile(src);
	}
	
	public void generateEmptyMap(int sizeX, int sizeY) {
		this.createNewMap(sizeX, sizeY);
		for(int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeY; j++) {
				this.board.setField(i, j, new PuzzleField(false, -1, false));
			}
		}
		ReferenceUpdater.updateReferences(this.board);
	}
	
	public void createNewMap(int sizeX, int sizeY)
	{
		this.board = new Board(sizeX, sizeY);
	}
	
	public void createFieldAt(String message, int x, int y)
	{
		this.board.setField(x, y, new PuzzleField(message));
	}
	
	public BoardUpdateEvent getFieldAsBoardUpdateMessage(int x, int y)
	{
		return new BoardUpdateEvent(x, y, this.puzzleTable[x][y].getCurrentValueOfField(), this.puzzleTable[x][y].getFieldTextColour());
	}
	
	@Override
	public void boardUpdated(BoardUpdateEvent event) {
		this.board.update(event);
	}
}

