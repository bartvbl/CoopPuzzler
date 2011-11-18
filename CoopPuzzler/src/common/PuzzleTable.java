package common;


public class PuzzleTable implements BoardUpdateListener{
	public PuzzleField[][] puzzleTable;
	private static final String DEFAULT_FILE_SOURCE = "res/puzzle.txt";
	
	public void loadMapFromLocalFile()
	{
		this.puzzleTable = PuzzleLoader.loadTableFromFile(DEFAULT_FILE_SOURCE);
	}
	
	public void createNewMap(int rows, int columns)
	{
		this.puzzleTable = new PuzzleField[rows][columns];
	}
	
	public void createFieldAt(String message, int row, int column)
	{
		this.puzzleTable[row][column] = new PuzzleField(message);
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

