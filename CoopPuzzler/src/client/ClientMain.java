package client;

import static org.lwjgl.opengl.GL11.*;
import client.drawing.PuzzleDrawer;
import client.gui.FeedbackProvider;
import client.gui.ingame.ColourPickerUI;
import client.gui.ingame.PlayButton;
import client.input.InputHandler;
import client.net.BoardEventHandler;
import client.net.ClientCommunicator;
import common.AutoSaver;
import common.BoardUpdateEvent;
import common.ManualSaver;
import common.ProtocolConstants;
import common.PuzzleTable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class ClientMain implements ProtocolConstants {
	public final ClientWindow window;
	public final PuzzleTable puzzleTable;
	public final PuzzleDrawer puzzleDrawer;
	public final InputHandler inputHandler;
	public final ClientCommunicator communicator;
	public final BoardEventHandler boardEventHandler;
	public final ColourPickerUI colourPickerUI;
	
	private AtomicReference<ArrayList<BoardUpdateEvent>> outputEventQueue = new AtomicReference<ArrayList<BoardUpdateEvent>>();
	private AtomicReference<ArrayList<BoardUpdateEvent>> inputEventQueue = new AtomicReference<ArrayList<BoardUpdateEvent>>();
	private GameStartSettings gameSettings;
	private PlayButton playButton;
	
	public ClientMain()
	{
		this.puzzleTable = new PuzzleTable();
		this.window = new ClientWindow(this);
		this.communicator = new ClientCommunicator(this);
		this.inputHandler = new InputHandler(this);
		this.puzzleDrawer = new PuzzleDrawer(this.puzzleTable, this.inputHandler);
		this.boardEventHandler = new BoardEventHandler(this);
		this.colourPickerUI = new ColourPickerUI(this);
		this.outputEventQueue.set(new ArrayList<BoardUpdateEvent>());
		this.inputEventQueue.set(new ArrayList<BoardUpdateEvent>());
		
		this.window.enableMainMenu();
	}
	
	public void runGame(GameStartSettings gameSettings)
	{
		new GameSettings(gameSettings);
		if(gameSettings.operationMode == OperationMode.ONLINE_GAME)
		{
			try {
				communicator.init(InetAddress.getByName(gameSettings.serverHostName));
			} catch (UnknownHostException e) {
				FeedbackProvider.showFailedToFindServerMessage();
				System.exit(0);
			}
			Thread commsMonitor = new Thread(communicator);
			commsMonitor.start();
			AutoSaver.setEnabled(false);
		} else if(gameSettings.operationMode == OperationMode.LOCAL_GAME){
			this.puzzleTable.loadMapFromLocalFile(gameSettings.puzzleFileSrc);
		} else if(gameSettings.operationMode == OperationMode.EDITOR) {
			if(gameSettings.startWithEmptyEditor) {
				this.puzzleTable.generateEmptyMap(gameSettings.rows, gameSettings.columns);
			} else {
				this.puzzleTable.loadMapFromLocalFile(gameSettings.puzzleFileSrc);
			}
		}
		Thread mainThread = new Thread(new MainLoopThread(this));
		mainThread.start();
	}
	
	public void initialize()
	{
		this.window.createOpenGLContext();
		this.inputHandler.init();
		this.puzzleDrawer.init();
		this.playButton = new PlayButton(this);
		new AutoSaver(puzzleTable.puzzleTable, GameSettings.puzzleFileSrc);
		new ManualSaver(puzzleTable.puzzleTable, GameSettings.puzzleFileSrc);
	}

	public void doFrame() {
		this.boardEventHandler.handleEvents();
		this.inputHandler.update();
		glScalef(inputHandler.zoomLevel, inputHandler.zoomLevel, 0.0f);
		glTranslatef(inputHandler.x, inputHandler.y, 0.0f);
		this.puzzleDrawer.draw();
		glLoadIdentity();
	}
	
	public void handleUI()
	{
		boolean hasHandledMouse = false;
		if(GameSettings.operationMode != OperationMode.EDITOR) {			
			hasHandledMouse = this.colourPickerUI.draw();
		} else {
			hasHandledMouse = this.playButton.draw(1);
		}
		if(!(hasHandledMouse && Mouse.isButtonDown(0)))
		{
			this.inputHandler.handleSelection();
		}
		ManualSaver.draw();
	}

	public ArrayList<BoardUpdateEvent> getEventQueueToServer() {
		ArrayList<BoardUpdateEvent> newList = new ArrayList<BoardUpdateEvent>();
		return this.outputEventQueue.getAndSet(newList);
	}
	
	public ArrayList<BoardUpdateEvent> getEventQueueToClient() {
		ArrayList<BoardUpdateEvent> newList = new ArrayList<BoardUpdateEvent>();
		return this.inputEventQueue.getAndSet(newList);
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
	
	public void serverRequestsShutDown(){
		FeedbackProvider.showServerShutdownMessage();
		this.gameSettings.operationMode = OperationMode.LOCAL_GAME;
		AutoSaver.setEnabled(true);
	}
	
	public void exitEditorMode() {
		GameSettings.operationMode = OperationMode.LOCAL_GAME;
		this.puzzleDrawer.updateBareBonesDisplayList();
		this.puzzleDrawer.updateFeatureDisplayList();
	}
	
	public boolean gameIsOnline() {
		return GameSettings.operationMode == OperationMode.ONLINE_GAME;
	}
}
