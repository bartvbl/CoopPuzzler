package common;


public class PuzzleTable {
	public PuzzleField[][] puzzleTable;
	private static final String DEFAULT_FILE_SOURCE = "res/puzzle.txt";
	
	public void initialize()
	{
		this.puzzleTable = PuzzleLoader.loadTableFromFile(DEFAULT_FILE_SOURCE);
	}
}
