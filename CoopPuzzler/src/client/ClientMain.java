package client;


import common.BoardUpdateEvent;
import common.ProtocolConstants;
import common.PuzzleTable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class ClientMain implements ProtocolConstants{
	public final ClientWindow window;
	public final PuzzleTable puzzleTable;
	private PuzzleDrawer puzzleDrawer;
	private InputHandler inputHandler;
	private ClientCommunicator communicator;
	
	private AtomicReference<ArrayList<BoardUpdateEvent>> outputEventQueue = new AtomicReference<ArrayList<BoardUpdateEvent>>();
	private AtomicReference<ArrayList<BoardUpdateEvent>> inputEventQueue = new AtomicReference<ArrayList<BoardUpdateEvent>>();
	
	public ClientMain()
	{
		this.outputEventQueue.set(new ArrayList<BoardUpdateEvent>());
		this.inputEventQueue.set(new ArrayList<BoardUpdateEvent>());
		this.communicator = new ClientCommunicator(this);
		try {
			communicator.init(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Thread commsMonitor = new Thread(communicator);
		commsMonitor.start();
		this.window = new ClientWindow(this);
		this.puzzleTable = new PuzzleTable();
		this.puzzleTable.initialize();
		this.inputHandler = new InputHandler(this, this.puzzleTable.puzzleTable[0].length, this.puzzleTable.puzzleTable.length);
		this.puzzleDrawer = new PuzzleDrawer(this.puzzleTable, this.inputHandler);
		this.window.mainLoop();
	}

	public void doFrame() {
		this.inputHandler.update();
		this.puzzleDrawer.draw();
		this.inputHandler.handleSelection();
	}

	public ArrayList<BoardUpdateEvent> getEventQueueToServer() {
		ArrayList<BoardUpdateEvent> list;
		ArrayList<BoardUpdateEvent> newList = new ArrayList<BoardUpdateEvent>();
		list = this.outputEventQueue.getAndSet(newList);
		return list;
	}
	
	public ArrayList<BoardUpdateEvent> getEventQueueToClient() {
		ArrayList<BoardUpdateEvent> list;
		ArrayList<BoardUpdateEvent> newList = new ArrayList<BoardUpdateEvent>();
		list = this.inputEventQueue.getAndSet(newList);
		return list;
	}
	
	public void sendEventToServer(BoardUpdateEvent event)
	{
		ArrayList<BoardUpdateEvent> outgoing = outputEventQueue.get();
		synchronized (outgoing) {
			outgoing.add(event);
		}
	}
	
	
}
