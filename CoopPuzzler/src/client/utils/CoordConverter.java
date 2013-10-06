package client.utils;

import org.lwjgl.input.Mouse;

public class CoordConverter {
	public static Point getMapCoordinates(double x, double y, double zoomLevel, double windowWidth, double windowHeight)
	{
		double aspect = windowWidth / windowHeight;
		double xCoord = Mouse.getX();
		double yCoord = Mouse.getY();
		xCoord /= windowHeight/2;
		xCoord -= 1;
		xCoord /= zoomLevel;
		xCoord -= (aspect - 1.0f)/zoomLevel;
		
		yCoord /= windowHeight/2;
		yCoord -= 1;
		yCoord /= zoomLevel;
		
		return new Point(xCoord, yCoord);
	}
}
