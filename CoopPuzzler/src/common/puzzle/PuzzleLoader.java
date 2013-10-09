package common.puzzle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import common.FontColour;
import common.ReferenceUpdater;


public class PuzzleLoader {

	public static synchronized Board loadTableFromFile(String src) {
		try
		{
			return loadFile(src);
		} catch(IOException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "fatal: failed to load puzzle file", "Error", JOptionPane.ERROR_MESSAGE);
		} catch(Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "fatal: failed to load puzzle file", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	private static Board loadFile(String src) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(src));
		String[] dimensionStrings = reader.readLine().split("x");
		int sizeX = Integer.parseInt(dimensionStrings[0]);
		int sizeY = Integer.parseInt(dimensionStrings[1]);
		char[][] board = new char[sizeX][sizeY];
		
		for(int x = 0; x < sizeX; x++)
		{
			String currentRow = reader.readLine();
			for(int y = 0; y < sizeY; y++)
			{
				board[x][y] = currentRow.charAt(y);
			}
		}
		PuzzleField[][] table = parsePuzzleTable(board, sizeX, sizeY);
		if(reader.ready())
		{
			if(reader.readLine().startsWith("save"))
			{
				readSaveGame(reader, table);
			}
		}
		return new Board(table);
	}
	
	private static void readSaveGame(BufferedReader reader, PuzzleField[][] table) throws IOException {
		String line;
		for(int x = 0; x < table.length; x++)
		{
			line = reader.readLine();
			System.out.println("["+line+"]");
			for(int y = 0; y < table[0].length; y++)
			{
				if(!table[x][y].isFilled)
				{
					if(line.charAt(y*2) != ' ')
					{
						table[x][y].setFieldTextColour(new FontColour(Integer.parseInt(""+line.charAt(y*2))));
						table[x][y].setNewCharacterValue(line.charAt((y*2)+1));
					}
				}
			}
		}
	}

	private static PuzzleField[][] parsePuzzleTable(char[][] board, int sizeX, int sizeY)
	{
		char fieldChar;
		int referenceID = 1;
		PuzzleField[][] puzzle = new PuzzleField[sizeX][sizeY];
		
		for(int x = 0; x < sizeX; x++)
		{
			for(int y = 0; y < sizeY; y++)
			{
				fieldChar = board[x][y];
				if(fieldChar == '*')
				{
					puzzle[x][y] = new PuzzleField(true, -1, false);
				} else if (fieldChar == ' ')
				{
					if(ReferenceUpdater.fieldHasReference(board, x, y))
					{
						puzzle[x][y] = new PuzzleField(false, referenceID, false);
						referenceID++;
					} else {
						puzzle[x][y] = new PuzzleField(false, -1, false);
					}
				} else if (fieldChar == 'i') //i for ignore
				{
					puzzle[x][y] = new PuzzleField(false, -1, true);
				}
			}
		}
		return puzzle;
	}

	
}
