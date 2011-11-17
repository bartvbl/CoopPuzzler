package client;

import java.util.ArrayList;

import common.BoardUpdateEvent;

public class BoardEventHandler {
	private ClientMain main;
	
	public BoardEventHandler(ClientMain main)
	{
		this.main = main;
	}
	
	public void handleEvents()
	{
		ArrayList<BoardUpdateEvent> eventQueue = this.main.getEventQueueToClient();
		synchronized(eventQueue)
		{
			for(BoardUpdateEvent event : eventQueue)
			{
				this.main.inputHandler.setField(event);
			}
		}
	}
}
