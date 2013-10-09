package common;


public class PuzzleTable implements BoardUpdateListener{
	public PuzzleField[][] puzzleTable;
	
	public void loadMapFromLocalFile(String src)
	{
		this.puzzleTable = PuzzleLoader.loadTableFromFile(src);
	}
	
	public void generateEmptyMap(int sizeX, int sizeY) {
		this.createNewMap(sizeX, sizeY);
		for(int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeY; j++) {
				this.puzzleTable[i][j] = new PuzzleField(false, -1, false);
			}
		}
		ReferenceUpdater.updateReferences(this.puzzleTable);
	}
	
	public void createNewMap(int sizeX, int sizey)
	{
		this.puzzleTable = new PuzzleField[sizeX][sizey];
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
	
	public BoardUpdateEvent getFieldAsBoardUpdateMessage(int x, int y)
	{
		return new BoardUpdateEvent(x, y, this.puzzleTable[x][y].getCurrentValueOfField(), this.puzzleTable[x][y].getFieldTextColour());
	}
	
	@Override
	public void boardUpdated(BoardUpdateEvent event) {
		puzzleTable[event.getX()][event.getY()].setNewCharacterValue(event.getCharacterValue());
		puzzleTable[event.getX()][event.getY()].setFieldTextColour(event.getColour());
	}
}

