package client.input;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import common.BoardUpdateEvent;
import common.PuzzleField;

import client.ClientMain;
import client.utils.Point;

import static org.lwjgl.opengl.GL11.*;

public class InputHandler {
	public float zoomLevel = 0.2f;
	public float x = -6.5f;
	public float y = -4.5f;
	
	private final MouseHandler mouseHandler;
	private final KeyboardHandler keyboardHandler;
	private final SelectionHandler selectionHandler;
	private final ClientMain main;
	
	public InputHandler(ClientMain main)
	{
		this.main = main;
		this.mouseHandler = new MouseHandler(this);
		this.keyboardHandler = new KeyboardHandler(main);
		this.selectionHandler = new SelectionHandler(main);
	}
	
	public void update()
	{
		this.mouseHandler.handleMouse(this.zoomLevel);
		this.keyboardHandler.handleKeyboard(this.zoomLevel, this.x, this.y);
	}
	
	public void handleSelection()
	{
		this.selectionHandler.handleSelection(this.zoomLevel, this.x, this.y);
	}
	
	public Point[] getSelectionArray()
	{
		return this.selectionHandler.getSelectionArray();
	}
	
	public boolean isTyping()
	{
		return this.selectionHandler.isTyping();
	}
	
	public void setField(BoardUpdateEvent event)
	{
		PuzzleField field = this.main.puzzleTable.puzzleTable[event.getX()][event.getY()];
		field.setNewCharacterValue(event.getCharacterValue());
		field.setFieldTextColour(event.getColour());
		this.main.puzzleDrawer.updateFeatureDisplayList();
	}

	public void init() {
		try {
			Mouse.create();
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		this.keyboardHandler.init();
		this.selectionHandler.init();
	}
}
