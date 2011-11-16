package client;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

import client.input.KeyboardHandler;
import client.input.MouseHandler;
import client.input.SelectionHandler;

import static org.lwjgl.opengl.GL11.*;

public class InputHandler {
	private float zoomLevel = 0.2f;
	private float x = -6.5f;
	private float y = -4.5f;
	
	private MouseHandler mouseHandler;
	private KeyboardHandler keyboardHandler;
	private SelectionHandler selectionHandler;
	
	public InputHandler(ClientMain main)
	{
		this.mouseHandler = new MouseHandler(this);
		this.keyboardHandler = new KeyboardHandler(main);
		this.selectionHandler = new SelectionHandler(main);
		try {
			Mouse.create();
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public void update()
	{
		this.mouseHandler.handleMouse(this.zoomLevel);
		this.keyboardHandler.handleKeyboard(this.zoomLevel, this.x, this.y);
		glScalef(this.zoomLevel, this.zoomLevel, 0.0f);
		glTranslatef(this.x, this.y, 0.0f);
	}
	
	public void handleSelection()
	{
		this.selectionHandler.handleSelection(this.zoomLevel, this.x, this.y);
	}
	
	public void setZoomLevel(float level)
	{
		this.zoomLevel = level;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public ArrayList<Point> getSelectionArray()
	{
		return this.selectionHandler.getSelectionArray();
	}
	
	public boolean isTyping()
	{
		return this.selectionHandler.isTyping();
	}
}
