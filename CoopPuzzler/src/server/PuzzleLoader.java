package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

public class PuzzleLoader {

	public static PuzzleField[][] loadTableFromFile(String src) {
		try
		{
			return loadFile(src);
		} catch(IOException e)
		{
			JOptionPane.showMessageDialog(null, "fatal: failed to load puzzle file", "Error", JOptionPane.ERROR_MESSAGE);
		} catch(Exception e)
		{
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
		
		return parsePuzzleTable(board, rows, columns);
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
					puzzle[row][column] = new PuzzleField(true, -1);
				} else if (fieldChar == ' ')
				{
					if(fieldHasReference(board, row, column))
					{
						puzzle[row][column] = new PuzzleField(false, referenceID);
						referenceID++;
					} else {
						puzzle[row][column] = new PuzzleField(false, -1);
					}
				}
			}
		}
		return puzzle;
	}
	private static boolean fieldHasReference(char[][] board, int row, int column) {
		boolean north = fieldIsInBounds(board, row-1, column);
		boolean south = fieldIsInBounds(board, row+1, column);
		boolean east = fieldIsInBounds(board, row, column+1);
		boolean west = fieldIsInBounds(board, row, column-1);
		return ((north && !south) || (west && !east));
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
			return true;
		}
	}
}
