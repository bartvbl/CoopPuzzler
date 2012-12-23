package editor.common;


public class PuzzleTable {
	public PuzzleField[][] puzzleTable;
	
	public void loadMapFromFile(String src)
	{
		this.puzzleTable = PuzzleLoader.loadTableFromFile(src);
	}
	
	public void createNewMap(int rows, int columns)
	{
		this.puzzleTable = new PuzzleField[rows][columns];
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
	

	public void generateEmptyMap(int rows, int columns) {
		this.createNewMap(rows, columns);
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				this.puzzleTable[i][j] = new PuzzleField(false, -1, false);
			}
		}
	}
}

