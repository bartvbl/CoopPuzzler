package client;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

import client.gl.CoordConverter;

import static org.lwjgl.opengl.GL11.*;

public class InputHandler {
	private float zoomLevel = 0.20f;
	private float x = -6.5f;
	private float y = -4.5f;
	private int mapWidth, mapHeight;
	private CoordConverter converter = new CoordConverter();
	private ClientWindow window;
	
	private static final float MOVE_SPEED = 0.3f;
	
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
			float[] coords = this.getMapCoordinates(Mouse.getX(), Mouse.getY());
			coords = this.converter.getScreenCoords(this.x, this.y);
			coords[0] += this.window.windowWidth/2;
			coords[1] += this.window.windowHeight/2;
			this.selectionArray.add(new Point((int)coords[0],(int)coords[1]));
			System.out.println("("+this.x+", "+this.y+", "+Mouse.getX()+", "+Mouse.getY()+", "+this.zoomLevel+", "+coords[0]+", "+coords[1]+", "+this.x/this.zoomLevel+")");
		}
	}
	
	private float[] getMapCoordinates(float mouseX, float mouseY)
	{
		float windowWidth = 640;
		float windowHeight = 480;
		float xCoord = ((float)mouseX / this.window.windowWidth)*windowWidth;
		float yCoord = ((float)(this.window.windowHeight - mouseY) / this.window.windowHeight)*windowHeight;
		xCoord = xCoord - (windowWidth / 2);
		yCoord = yCoord - (windowHeight / 2);
		xCoord = xCoord * this.zoomLevel;
		yCoord = yCoord * this.zoomLevel;
		//System.out.println(xCoord + ", " +yCoord +", "+ this.x);
		//xCoord -= (this.x/this.zoomLevel);
		
		return new float[]{xCoord, yCoord};
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
