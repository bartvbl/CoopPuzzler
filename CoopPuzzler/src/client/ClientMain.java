package client;


import static org.lwjgl.opengl.GL11.*;
import client.gui.ColourPickerUI;
import common.BoardUpdateEvent;
import common.ProtocolConstants;
import common.PuzzleTable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JOptionPane;

import org.lwjgl.input.Mouse;

public class ClientMain implements ProtocolConstants{
	public final ClientWindow window;
	public final PuzzleTable puzzleTable;
	public final PuzzleDrawer puzzleDrawer;
	public final InputHandler inputHandler;
	public final ClientCommunicator communicator;
	public final BoardEventHandler boardEventHandler;
	public final ColourPickerUI colourPickerUI;
	
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
		this.inputHandler = new InputHandler(this);
		this.puzzleDrawer = new PuzzleDrawer(this.puzzleTable, this.inputHandler);
		this.boardEventHandler = new BoardEventHandler(this);
		this.colourPickerUI = new ColourPickerUI(this);
		this.window.mainLoop();
	}

	public void doFrame() {
		this.boardEventHandler.handleEvents();
		this.inputHandler.update();
		glScalef(inputHandler.zoomLevel, inputHandler.zoomLevel, 0.0f);
		glTranslatef(inputHandler.x, inputHandler.y, 0.0f);
		this.puzzleDrawer.draw();
		glLoadIdentity();
		glOrtho(0.0f, this.window.windowWidth, 0.0f, this.window.windowHeight, -1.0f, 1.0f);
		boolean hasHandledMouse = this.colourPickerUI.draw();
		if(!(hasHandledMouse && Mouse.isButtonDown(0)))
		{
			this.inputHandler.handleSelection();
		}
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
	
	public void sendEventToClient(BoardUpdateEvent event)
	{
		ArrayList<BoardUpdateEvent> incoming = this.inputEventQueue.get();
		synchronized (incoming) {
			incoming.add(event);
		}
	}
	
	
}
