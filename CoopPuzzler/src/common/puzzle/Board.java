package common.puzzle;

import common.BoardUpdateEvent;

public class Board {
	public final int sizeX, sizeY;
	
	private final PuzzleField[][] board;
	
	public Board(PuzzleField[][] board) {
		this.board = board;
		this.sizeX = board.length;
		this.sizeY = board[0].length;
	}

	public Board(int sizeX, int sizeY) {
		this(new PuzzleField[sizeX][sizeY]);
	}

	public void setField(int x, int y, PuzzleField puzzleField) {
		board[x][y] = puzzleField;
	}
	
	public boolean fieldIsOccupied(int x, int y)
	{
		if(x < 0)
		{
			return false;
		} else if(x >= sizeX)
		{
			return false;
		} else if (y < 0)
		{
			return false;
		} else if (y >= sizeY)
		{
			return false;
		} else {
			return this.board[x][y].isFilled;
		}
	}

	public void update(BoardUpdateEvent event) {
		board[event.getX()][event.getY()].setNewCharacterValue(event.getCharacterValue());
		board[event.getX()][event.getY()].setFieldTextColour(event.getColour());
	}
}
