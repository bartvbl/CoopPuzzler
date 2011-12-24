package client;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

import common.BoardUpdateEvent;

import client.input.KeyboardHandler;
import client.input.MouseHandler;
import client.input.SelectionHandler;

import static org.lwjgl.opengl.GL11.*;

public class InputHandler {
	public float zoomLevel = 0.2f;
	public float x = -6.5f;
	public float y = -4.5f;
	
	private MouseHandler mouseHandler;
	private KeyboardHandler keyboardHandler;
	private SelectionHandler selectionHandler;
	
	private int previousMouseX, previousMouseY;
	
	public InputHandler(ClientMain main)
	{
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
	
	public ArrayList<Point> getSelectionArray()
	{
		return this.selectionHandler.getSelectionArray();
	}
	
	public boolean isTyping()
	{
		return this.selectionHandler.isTyping();
	}
	
	public void setField(BoardUpdateEvent event)
	{
		this.selectionHandler.updateCharacter(event);
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
