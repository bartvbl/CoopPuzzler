package server;

public class PuzzleTable {
	private char[][] puzzleTable;
	private static final String DEFAULT_FILE_SOURCE = "res/puzzle.txt";
	
	public void initialize()
	{
		PuzzleLoader.loadTableFromFile(DEFAULT_FILE_SOURCE);
	}
}
