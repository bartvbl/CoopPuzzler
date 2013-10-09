package common;

public class ReferenceUpdater {
	public static void updateReferences(PuzzleField[][] table) {
		int count = 0;
		int sizeY = table.length;
		for(int i = sizeY - 1; i >= 0; i--) {
			for(int j = 0; j < table[0].length; j++) {
				if(!table[i][j].isFilled && !table[i][j].hasIgnoreReference && fieldHasReference(table, i, j)) {
					count++;
					table[i][j].questionReference = count;
				} else {
					table[i][j].questionReference = -1;
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
