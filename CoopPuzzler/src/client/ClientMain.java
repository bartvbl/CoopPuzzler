package client;


import common.BoardUpdateEvent;
import common.ProtocolConstants;
import common.PuzzleTable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class ClientMain implements ProtocolConstants{
	private ClientWindow window;
	private PuzzleTable puzzleTable;
	private PuzzleDrawer puzzleDrawer;
	private InputHandler inputHandler;
	private ClientCommunicator communicator;
	
	private AtomicReference<ArrayList<BoardUpdateEvent>> outputEventQueue = new AtomicReference<ArrayList<BoardUpdateEvent>>();
	private AtomicReference<ArrayList<BoardUpdateEvent>> inputEventQueue = new AtomicReference<ArrayList<BoardUpdateEvent>>();
	
	public ClientMain()
	{
		this.communicator = new ClientCommunicator(this);
		try {
			communicator.init(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Thread comms = new Thread(communicator);
		comms.start();
		this.window = new ClientWindow(this);
		this.puzzleTable = new PuzzleTable();
		this.puzzleTable.initialize();
		this.inputHandler = new InputHandler(this.window, this.puzzleTable.puzzleTable[0].length, this.puzzleTable.puzzleTable.length,this.puzzleTable);
		this.puzzleDrawer = new PuzzleDrawer(this.puzzleTable, this.inputHandler);
		this.window.mainLoop();
	}

	public void doFrame() {
		this.inputHandler.update();
		this.puzzleDrawer.draw();
		this.inputHandler.handleSelection();
	}

	public ArrayList<BoardUpdateEvent> getEventQueueToServer() {
		ArrayList<BoardUpdateEvent> list = new ArrayList<BoardUpdateEvent>();
		ArrayList<BoardUpdateEvent> newList = new ArrayList<BoardUpdateEvent>();
		list = this.outputEventQueue.getAndSet(newList);
		return list;
	}
	
	public ArrayList<BoardUpdateEvent> getEventQueueToClient() {
		ArrayList<BoardUpdateEvent> list = new ArrayList<BoardUpdateEvent>();
		ArrayList<BoardUpdateEvent> newList = new ArrayList<BoardUpdateEvent>();
		list = this.inputEventQueue.getAndSet(newList);
		return list;
	}
	
	public void sendEventToServer(BoardUpdateEvent event)
	{
		
	}
	
	
}
