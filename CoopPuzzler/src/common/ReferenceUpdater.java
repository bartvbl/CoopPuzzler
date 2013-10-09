package common;

import common.puzzle.Board;
import common.puzzle.PuzzleField;

public class ReferenceUpdater {
	public static void updateReferences(Board board) {
		int count = 0;
		int sizeY = board.length;
		for(int i = sizeY - 1; i >= 0; i--) {
			for(int j = 0; j < board[0].length; j++) {
				if(!board[i][j].isFilled && !board[i][j].hasIgnoreReference && fieldHasReference(board, i, j)) {
					count++;
					board[i][j].questionReference = count;
				} else {
					board[i][j].questionReference = -1;
				}
			}
		}
	}
	
	public static boolean fieldHasReference(char[][] board, int x, int y) {
		boolean north = fieldIsInBounds(board, x+1, y);
		boolean south = fieldIsInBounds(board, x-1, y);
		boolean east = fieldIsInBounds(board, x, y+1);
		boolean west = fieldIsInBounds(board, x, y-1);
		return ((!north && south) || (!west && east));
	}
	
	private static boolean fieldIsInBounds(char[][] board, int x, int y)
	{
		if(x < 0)
		{
			return false;
		} else if (x >= board.length)
		{
			return false;
		} else if (y < 0)
		{
			return false;
		} else if (y >= board[0].length)
		{
			return false;
		} else {
			return ((board[x][y] == 'i') ||(board[x][y] == ' '));
		}
	}
	
	public static boolean fieldHasReference(PuzzleField[][] board, int x, int y) {
		boolean north = fieldIsInBounds(board, x+1, y);
		boolean south = fieldIsInBounds(board, x-1, y);
		boolean east = fieldIsInBounds(board, x, y+1);
		boolean west = fieldIsInBounds(board, x, y-1);
		return ((!north && south) || (!west && east));
	}
	
	private static boolean fieldIsInBounds(PuzzleField[][] board, int x, int y)
	{
		if(x < 0)
		{
			return false;
		} else if (x >= board.length)
		{
			return false;
		} else if (y < 0)
		{
			return false;
		} else if (y >= board[0].length)
		{
			return false;
		} else {
			return ((board[x][y].hasIgnoreReference) || (!board[x][y].isFilled));
		}
	}
}
