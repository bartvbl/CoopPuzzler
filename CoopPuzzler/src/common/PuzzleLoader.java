package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;


public class PuzzleLoader {

	public static synchronized PuzzleField[][] loadTableFromFile(String src) {
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
	
	private static PuzzleField[][] loadFile(String src) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(src));
		String[] dimensionStrings = reader.readLine().split("x");
		int rows = Integer.parseInt(dimensionStrings[0]);
		int columns = Integer.parseInt(dimensionStrings[1]);
		char[][] board = new char[rows][columns];
		
		for(int row = 0; row < rows; row++)
		{
			String currentRow = reader.readLine();
			for(int column = 0; column < columns; column++)
			{
				board[row][column] = currentRow.charAt(column);
			}
		}
		PuzzleField[][] table = parsePuzzleTable(board, rows, columns);
		if(reader.ready())
		{
			if(reader.readLine().startsWith("save"))
			{
				readSaveGame(reader, table);
			}
		}
		return table;
	}
	
	private static void readSaveGame(BufferedReader reader, PuzzleField[][] table) throws IOException {
		String line;
		for(int row = 0; row < table.length; row++)
		{
			line = reader.readLine();
			System.out.println("["+line+"]");
			for(int column = 0; column < table[0].length; column++)
			{
				if(!table[row][column].isFilled)
				{
					if(line.charAt(column*2) != ' ')
					{
						table[row][column].setFieldTextColour(new FontColour(Integer.parseInt(""+line.charAt(column*2))));
						table[row][column].setNewCharacterValue(line.charAt((column*2)+1));
					}
				}
			}
		}
	}

	private static PuzzleField[][] parsePuzzleTable(char[][] board, int rows, int columns)
	{
		char fieldChar;
		int referenceID = 1;
		PuzzleField[][] puzzle = new PuzzleField[rows][columns];
		
		for(int row = 0; row < rows; row++)
		{
			for(int column = 0; column < columns; column++)
			{
				fieldChar = board[row][column];
				if(fieldChar == '*')
				{
					puzzle[row][column] = new PuzzleField(true, -1, false);
				} else if (fieldChar == ' ')
				{
					if(ReferenceUpdater.fieldHasReference(board, row, column))
					{
						puzzle[row][column] = new PuzzleField(false, referenceID, false);
						referenceID++;
					} else {
						puzzle[row][column] = new PuzzleField(false, -1, false);
					}
				} else if (fieldChar == 'i') //i for ignore
				{
					puzzle[row][column] = new PuzzleField(false, -1, true);
				}
			}
		}
		return puzzle;
	}

	
}
