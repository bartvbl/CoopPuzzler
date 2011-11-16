package client.input;

import org.lwjgl.input.Mouse;

public class MouseHandler {
	public static float updateZoomLevel(float zoomLevel)
	{
		zoomLevel += ((float)Mouse.getDWheel())/10000;
		if(zoomLevel > 0.5f)
		{
			zoomLevel = 0.5f;
		}
		if(zoomLevel < 0.05f)
		{
			zoomLevel = 0.05f;
		}
		return zoomLevel;
	}
}
