package editor.utils;

import org.lwjgl.input.Mouse;

public class CoordConverter {
	public static float[] getMapCoordinates(float x, float y, float zoomLevel, float windowWidth, float windowHeight)
	{
		float aspect = windowWidth / windowHeight;
		float xCoord = Mouse.getX();
		float yCoord = Mouse.getY();
		xCoord /= windowHeight/2;
		xCoord -= 1;
		xCoord /= zoomLevel;
		xCoord -= (aspect - 1.0f)/zoomLevel;
		
		yCoord /= windowHeight/2;
		yCoord -= 1;
		yCoord /= zoomLevel;
		
		return new float[]{xCoord, yCoord};
	}
}
