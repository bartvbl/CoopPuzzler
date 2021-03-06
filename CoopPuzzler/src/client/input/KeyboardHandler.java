package client.input;

import org.lwjgl.input.Keyboard;

import common.AutoSaver;
import common.ManualSaver;

import client.ClientMain;
import client.ClientWindow;

public class KeyboardHandler {
	private static final float MOVE_SPEED = 0.3f;
	private static final int MOVE_BOUNDARY_BORDER_INSET = 2;
	
	private final ClientMain main;
	private ClientWindow window;
	
	private int mapSizeX;
	private int mapSizeY;
	
	public KeyboardHandler(ClientMain main) {
		this.main = main;
		this.window = main.window;
	}
	
	public void init()
	{
		this.mapSizeX = main.puzzleTable.puzzleTable.length;
		this.mapSizeY = main.puzzleTable.puzzleTable[0].length;
	}

	public void handleKeyboard(float zoomLevel, float x, float y)
	{
		x = this.handleKeyboardInputX(x);
		y = this.handleKeyboardInputY(y);
		float aspect = this.window.windowWidth / this.window.windowHeight;
		float bound = 1/zoomLevel;
		x = this.checkBoundsX(x, bound, aspect);
		y = this.checkBoundsY(y, bound);
		this.main.inputHandler.x = x;
		this.main.inputHandler.y = y;
		if((Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LMETA)) && Keyboard.isKeyDown(Keyboard.KEY_S)) {
			ManualSaver.doManualSave();
		}
	}
	private float handleKeyboardInputY(float y)
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			y += MOVE_SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			y -= MOVE_SPEED;
		}
		return y;
	}
	
	private float handleKeyboardInputX(float x)
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			x += MOVE_SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			x -= MOVE_SPEED;
		}
		return x;
	}
	
	private float checkBoundsX(float x, float bound, float aspect)
	{
		if(x < -1*bound*aspect - this.mapSizeY + MOVE_BOUNDARY_BORDER_INSET)
		{
			x = -1*bound*aspect - this.mapSizeY + MOVE_BOUNDARY_BORDER_INSET;
		}
		if(x > bound*aspect - MOVE_BOUNDARY_BORDER_INSET)
		{
			x = bound*aspect - MOVE_BOUNDARY_BORDER_INSET;
		}
		return x;
	}
	
	private float checkBoundsY(float y, float bound)
	{
		if(y < -1*bound - this.mapSizeX + MOVE_BOUNDARY_BORDER_INSET)
		{
			y = -1*bound - this.mapSizeX + MOVE_BOUNDARY_BORDER_INSET;
		}
		if(y > bound - MOVE_BOUNDARY_BORDER_INSET)
		{
			y = bound - MOVE_BOUNDARY_BORDER_INSET;
		}
		return y;
	}
}