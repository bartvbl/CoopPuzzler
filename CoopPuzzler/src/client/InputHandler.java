package client;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

import client.gl.CoordConverter;

import static org.lwjgl.opengl.GL11.*;

public class InputHandler {
	private float zoomLevel = 0.1f;
	private float x = 0.0f;
	private float y = 0.0f;
	private int mapWidth, mapHeight;
	private CoordConverter converter = new CoordConverter();
	private ClientWindow window;
	
	private static final float MOVE_SPEED = 0.03f;
	
	public ArrayList<Point> selectionArray = new ArrayList<Point>();
	public boolean isTyping = false;
	
	public InputHandler(ClientWindow window, int mapHeight, int mapWidth)
	{
		this.window = window;
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
			float rawX = ((Mouse.getX()-(this.x/this.zoomLevel))*this.zoomLevel);
			float rawY = 0;//((this.window.windowHeight - Mouse.getY())*this.zoomLevel)-this.y;
			int xCoord = (int)Math.floor(rawX);
			int yCoord = (int)Math.floor(rawY);
			this.selectionArray.add(new Point(yCoord,xCoord));
			System.out.println("("+this.x+","+this.y+","+Mouse.getX()+","+Mouse.getY()+","+this.zoomLevel+", "+rawX+")");
		}
	}

	private void handleKeyboard() {
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			this.y += MOVE_SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			this.y -= MOVE_SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			this.x += MOVE_SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			this.x -= MOVE_SPEED;
		}
//		if(x < -0.5f)
//		{
//			x = -0.5f;
//		}
//		if(x > 1.5f)
//		{
//			x = 1.5f;
//		}
//		if(y < -0.5f)
//		{
//			y = -0.5f;
//		}
//		if(y > 1.5f)
//		{
//			y = 1.5f;
//		}
	}

	private void handleMouse() {
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
