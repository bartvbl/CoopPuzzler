package common;


public class PuzzleTable implements BoardUpdateListener{
	public PuzzleField[][] puzzleTable;
	
	public void loadMapFromLocalFile(String src)
	{
		this.puzzleTable = PuzzleLoader.loadTableFromFile(src);
	}
	
	public void generateEmptyMap(int rows, int columns) {
		this.createNewMap(rows, columns);
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				this.puzzleTable[i][j] = new PuzzleField(false, -1, false);
			}
		}
		ReferenceUpdater.updateReferences(this.puzzleTable);
	}
	
	public void createNewMap(int rows, int columns)
	{
		this.puzzleTable = new PuzzleField[rows][columns];
	}
	
	public void createFieldAt(String message, int x, int y)
	{
		this.puzzleTable[x][y] = new PuzzleField(message);
	}
	
	public boolean fieldIsOccupied(int x, int y)
	{
		if(x < 0)
		{
			return false;
		} else if(x >= this.puzzleTable.length)
		{
			return false;
		} else if (y < 0)
		{
			return false;
		} else if (y >= this.puzzleTable[0].length)
		{
			return false;
		} else {
			return this.puzzleTable[x][y].isFilled;
		}
	}
	
	public BoardUpdateEvent getFieldAsBoardUpdateMessage(int row, int column)
	{
		return new BoardUpdateEvent(row, column, this.puzzleTable[row][column].getCurrentValueOfField(), this.puzzleTable[row][column].getFieldTextColour());
	}
	
	@Override
	public void boardUpdated(BoardUpdateEvent event) {
		puzzleTable[event.getRow()][event.getColumn()].setNewCharacterValue(event.getCharacterValue());
		puzzleTable[event.getRow()][event.getColumn()].setFieldTextColour(event.getColour());
	}
}

