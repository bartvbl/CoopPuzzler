package client;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

import client.gl.CoordConverter;

import static org.lwjgl.opengl.GL11.*;

public class InputHandler {
	private float zoomLevel = 0.01f;
	private float x = 0.0f;
	private float y = 0.0f;
	private int mapWidth, mapHeight;
	private CoordConverter converter = new CoordConverter();
	
	public ArrayList<Point> selectionArray = new ArrayList<Point>();
	public boolean isTyping = false;
	
	public InputHandler(int mapHeight, int mapWidth)
	{
		this.mapHeight = mapHeight;
		this.mapWidth = mapWidth;
		try {
			Mouse.create();
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public void update()
	{
		this.handleMouse();
		this.handleKeyboard();
		
		glScalef(zoomLevel, zoomLevel, 0.0f);
		glTranslatef(x, y, 0.0f);
	}

	public void handleSelection() {
		this.selectionArray.clear();
		
		if(this.isTyping)
		{
			
		} else {
			float[] coords = converter.getMapCoords(Mouse.getX(), Mouse.getY());
			int xCoord = (int)Math.floor(-100 + zoomLevel*PuzzleDrawer.FIELD_SIZE*Mouse.getY()*-1);//(int)Math.floor(coords[0]/100);
			int yCoord = (int)Math.floor(-100 + zoomLevel*PuzzleDrawer.FIELD_SIZE*Mouse.getX());//(int)Math.floor(coords[0]/100);
			this.selectionArray.add(new Point(xCoord,yCoord));
		}
	}

	private void handleKeyboard() {
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			this.y += 7.0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			this.y -= 7.0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			this.x += 7.0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			this.x -= 7.0;
		}
		if(x < -100)
		{
			x = -100;
		}
		if(x > PuzzleDrawer.FIELD_SIZE * mapWidth + -100 * zoomLevel)
		{
			x = PuzzleDrawer.FIELD_SIZE * mapWidth + -100 * zoomLevel;
		}
		if(y < -100)
		{
			y = -100;
		}
		if(y > PuzzleDrawer.FIELD_SIZE * mapHeight + -130)
		{
			y = PuzzleDrawer.FIELD_SIZE * mapHeight + -130;
		}
	}

	private void handleMouse() {
		this.zoomLevel += ((float)Mouse.getDWheel())/100000;
		if(this.zoomLevel > 0.03f)
		{
			this.zoomLevel = 0.03f;
		}
		if(this.zoomLevel < 0.002)
		{
			this.zoomLevel = 0.002f;
		}
	}
}
