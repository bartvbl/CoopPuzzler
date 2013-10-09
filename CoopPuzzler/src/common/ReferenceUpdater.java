package common;

public class ReferenceUpdater {
	public static void updateReferences(PuzzleField[][] table) {
		int count = 0;
		int rowCount = table.length;
		for(int i = rowCount - 1; i >= 0; i--) {
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
	
	public static boolean fieldHasReference(char[][] board, int row, int column) {
		boolean north = fieldIsInBounds(board, row+1, column);
		boolean south = fieldIsInBounds(board, row-1, column);
		boolean east = fieldIsInBounds(board, row, column+1);
		boolean west = fieldIsInBounds(board, row, column-1);
		return ((!north && south) || (!west && east));
	}
	
	private static boolean fieldIsInBounds(char[][] board, int row, int column)
	{
		if(row < 0)
		{
			return false;
		} else if (row >= board.length)
		{
			return false;
		} else if (column < 0)
		{
			return false;
		} else if (column >= board[0].length)
		{
			return false;
		} else {
			return ((board[row][column] == 'i') ||(board[row][column] == ' '));
		}
	}
	
	public static boolean fieldHasReference(PuzzleField[][] board, int row, int column) {
		boolean north = fieldIsInBounds(board, row+1, column);
		boolean south = fieldIsInBounds(board, row-1, column);
		boolean east = fieldIsInBounds(board, row, column+1);
		boolean west = fieldIsInBounds(board, row, column-1);
		return ((!north && south) || (!west && east));
	}
	
	private static boolean fieldIsInBounds(PuzzleField[][] board, int row, int column)
	{
		if(row < 0)
		{
			return false;
		} else if (row >= board.length)
		{
			return false;
		} else if (column < 0)
		{
			return false;
		} else if (column >= board[0].length)
		{
			return false;
		} else {
			return ((board[row][column].hasIgnoreReference) || (!board[row][column].isFilled));
		}
	}
}
