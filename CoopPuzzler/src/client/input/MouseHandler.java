package client.input;

import org.lwjgl.input.Mouse;

import client.InputHandler;

public class MouseHandler {
	private InputHandler inputHandler;
	private float zoomLevel = 0.2f;
	
	public MouseHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}
	
	public float getZoomLevel()
	{
		return this.zoomLevel;
	}
	
	public void handleMouse() {
		this.zoomLevel += ((float)Mouse.getDWheel())/10000;
		if(this.zoomLevel > 0.5f)
		{
			this.zoomLevel = 0.5f;
		}
		if(this.zoomLevel < 0.05f)
		{
			this.zoomLevel = 0.05f;
		}
	}
}
