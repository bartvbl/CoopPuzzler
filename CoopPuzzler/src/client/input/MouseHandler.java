package client.input;

import org.lwjgl.input.Mouse;

import client.InputHandler;

public class MouseHandler {
	private InputHandler inputHandler;
	private static final int MOUSE_WHEEL_LIMITER = 10000;
	
	public MouseHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}
	
	public void handleMouse(float zoomLevel) {
		zoomLevel += ((float)Mouse.getDWheel())/MOUSE_WHEEL_LIMITER;
		if(zoomLevel > 0.5f)
		{
			zoomLevel = 0.5f;
		}
		if(zoomLevel < 0.05f)
		{
			zoomLevel = 0.05f;
		}
		this.inputHandler.zoomLevel = zoomLevel;
	}
}
