package client;

import java.util.ArrayList;

import common.BoardUpdateEvent;
import common.PuzzleField;

public class BoardEventHandler {
	private PuzzleField[][] table;
	private ClientMain main;
	
	public BoardEventHandler(ClientMain main, PuzzleField[][] board)
	{
		this.table = board;
		this.main = main;
	}
	
	public void handleEvents()
	{
		ArrayList<BoardUpdateEvent> eventQueue = this.main.getEventQueueToClient();
		for(BoardUpdateEvent event : eventQueue)
		{
			PuzzleField field = this.table[event.getRow()][event.getColumn()];
			field.setNewCharacterValue(event.getCharacterValue());
			field.setFieldTextColour(event.getColour());
		}
	}
}
