package editor;

import static org.lwjgl.opengl.GL11.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import client.ClientWindow;

import editor.common.AutoSaver;
import editor.common.PuzzleTable;
import editor.gui.ColourPickerUI;
import editor.gui.FeedbackProvider;

public class EditorMain {
	public final ClientWindow window;
	public final PuzzleTable puzzleTable;
	public final PuzzleDrawer puzzleDrawer;
	public final InputHandler inputHandler;
	
	public boolean gameIsOnline = false;
	
	public EditorMain(ClientWindow window)
	{
		this.puzzleTable = new PuzzleTable();
		this.window = window;
		this.inputHandler = new InputHandler(this);
		this.puzzleDrawer = new PuzzleDrawer(this.puzzleTable, this.inputHandler);
		
		this.window.enableMainMenu();
	}
	
	public void runGame(boolean editCurrent, int rows, int columns, String src)
	{
		if(editCurrent) {			
			this.puzzleTable.loadMapFromFile(src);
		} else {
			this.puzzleTable.generateEmptyMap(rows, columns);
		}
		
		Thread mainThread = new Thread(new MainLoopThread(this));
		mainThread.start();
	}
	
	public void doMainLoop()
	{
		this.window.createOpenGLContext();
		this.inputHandler.init();
		this.puzzleDrawer.init();
		new AutoSaver(this.puzzleTable.puzzleTable);
		this.window.mainLoop();
		Display.destroy();
	}

	public void doFrame() {
		this.inputHandler.update();
		glScalef(inputHandler.zoomLevel, inputHandler.zoomLevel, 0.0f);
		glTranslatef(inputHandler.x, inputHandler.y, 0.0f);
		this.puzzleDrawer.draw();
		glLoadIdentity();
	}
	
	public void handleUI()
	{
		
		this.inputHandler.handleSelection();
		
	}
	
	public void serverRequestsShutDown(){
		FeedbackProvider.showServerShutdownMessage();
		this.gameIsOnline = false;
	}
	
}
