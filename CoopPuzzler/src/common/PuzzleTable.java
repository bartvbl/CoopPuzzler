package common;


public class PuzzleTable {
	public PuzzleField[][] puzzleTable;
	private static final String DEFAULT_FILE_SOURCE = "res/puzzle.txt";
	
	public void initialize()
	{
		this.puzzleTable = PuzzleLoader.loadTableFromFile(DEFAULT_FILE_SOURCE);
	}
	public boolean fieldIsOccupied(int row, int column)
	{
		if(row < 0)
		{
			return false;
		} else if(row >= this.puzzleTable.length)
		{
			return false;
		} else if (column < 0)
		{
			return false;
		} else if (column >= this.puzzleTable[0].length)
		{
			return false;
		} else {
			return this.puzzleTable[row][column].isFilled;
		}
	}
}
