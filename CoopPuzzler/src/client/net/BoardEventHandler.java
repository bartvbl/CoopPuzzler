package client.net;

import java.util.ArrayList;

import client.ClientMain;

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
