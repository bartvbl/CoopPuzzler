package client;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.lwjgl.util.Timer;

import common.PuzzleField;
import common.PuzzleTable;

import client.gl.CoordConverter;

import static org.lwjgl.opengl.GL11.*;

public class InputHandler {
	private float zoomLevel = 0.20f;
	private float x = -6.5f;
	private float y = -4.5f;
	private int mapWidth, mapHeight;
	private CoordConverter converter = new CoordConverter();
	private ClientWindow window;
	private static final float X_FIELD_SIZE = 0.1427367308f;
	private static final float Y_FIELD_SIZE = 0.2020211444f;
	private static final float MOVE_SPEED = 0.3f;
	public ArrayList<Point> selectionArray = new ArrayList<Point>();
	public boolean isTyping = false;
	private PuzzleTable puzzleTable;
	private Timer selectionActionTimer = new Timer();
	private float selectionActionStartTime;
	private boolean isWaiting = false;
	private ArrayList<Point> selectionUndoList = new ArrayList<Point>();
	private char previousChar = ' ';
	
	public InputHandler(ClientWindow window, int mapHeight, int mapWidth, PuzzleTable table)
	{
		this.puzzleTable = table;
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
		Timer.tick();
		if(isWaiting)
		{
			if((this.selectionActionTimer.getTime() - this.selectionActionStartTime) > 0.2)
			{
				this.isWaiting = false;
			}
		} else {
			if(this.isTyping)
			{
				if((Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) || (Mouse.isButtonDown(1)) || (Mouse.isButtonDown(0)))
				{
					this.isTyping = false;
					this.isWaiting = true;
					this.selectionActionStartTime = this.selectionActionTimer.getTime();
				}
				char typedKey = KeyboardToCharConverter.getKeyCharValue();
				if(typedKey != ' ')
				{
					Point point;
					if((this.previousChar == 'i') && (typedKey == 'j'))
					{
						point = this.selectionUndoList.get(this.selectionUndoList.size()-1);
						typedKey = TextureLibrary.IJ;
					} else {
						point = this.selectionArray.remove(0);
						this.selectionUndoList.add(point);
					}
					int column = point.getX();
					int row = this.mapWidth - point.getY() -1;
					this.isWaiting = true;
					this.selectionActionStartTime = this.selectionActionTimer.getTime();
					this.previousChar = typedKey;
					this.puzzleTable.puzzleTable[row][column].setNewCharacterValue(typedKey);
					if(this.selectionArray.size() == 0)
					{
						this.isTyping = false;
						this.isWaiting = true;
						this.selectionActionStartTime = this.selectionActionTimer.getTime();
					}
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_BACK))
				{
					Point point = this.selectionUndoList.remove(this.selectionUndoList.size()-1);
					this.selectionArray.add(0, point);
					int column = point.getX();
					int row = this.mapWidth - point.getY() -1;
					this.previousChar = this.puzzleTable.puzzleTable[row][column].getCurrentValueOfField();
					this.puzzleTable.puzzleTable[row][column].setNewCharacterValue(' ');
					this.isWaiting = true;
					this.selectionActionStartTime = this.selectionActionTimer.getTime();
					if(this.selectionUndoList.size() == 0)
					{
						this.isTyping = false;
						this.isWaiting = true;
						this.selectionActionStartTime = this.selectionActionTimer.getTime();
					}
				}
			} else {
				this.selectionArray.clear();
				float[] coords = this.getMapCoordinates(Mouse.getX(), Mouse.getY());
				
				if(!((coords[0] < 0) || (coords[1] < 0) || (coords[1] >= this.mapWidth) || (coords[0] >= this.mapHeight)))
				{
					int column = (int)coords[0];
					int row = this.mapWidth - (int)coords[1] -1;
					if(this.puzzleTable.fieldIsOccupied(row, column))
					{
						return;
					}
					this.selectionArray.add(new Point((int)coords[0],(int)coords[1]));
					float remX = coords[0] % 1;
					float remY = coords[1] % 1;
					//lower quadrant
					if((remX > remY) && (remX < (1 - remY)))
					{
						int tracker = this.mapWidth - (int)coords[1];
						int counter = 1;
						while(tracker < this.mapWidth)
						{
							if(this.puzzleTable.fieldIsOccupied(tracker, (int) coords[0]))
							{
								break;
							}
							this.selectionArray.add(new Point((int)coords[0],(int)coords[1]-counter));
							tracker++;
							counter++;
						}
					}
					//right quadrant
					if((remX > remY) && (remX > (1 - remY)))
					{
						int tracker = (int)coords[0] + 1;
						int counter = 1;
						while(tracker < this.mapHeight)
						{
							if(this.puzzleTable.fieldIsOccupied((int) this.mapWidth - (int)coords[1]-1,tracker))
							{
								break;
							}
							this.selectionArray.add(new Point((int)coords[0]+counter,(int)coords[1]));
							tracker++;
							counter++;
						}
					}
					if(Mouse.isButtonDown(0))
					{
						this.isTyping = true;
						this.selectionActionStartTime = this.selectionActionTimer.getTime();
						this.isWaiting = true;
						this.previousChar = ' ';
						this.selectionUndoList = new ArrayList<Point>();
					}
				}
				
				//System.out.println("("+this.x+", "+this.y+", "+Mouse.getX()+", "+Mouse.getY()+", "+this.zoomLevel+", "+coords[0]+", "+coords[1]+", "+this.x/this.zoomLevel+")");
			}
		}
	}
	
	private float transformX(float x)
	{
		float aspect = this.window.windowWidth / this.window.windowHeight;
		return ((x-0.1f)*this.zoomLevel)/aspect;
	}
	private float transformY(float y)
	{
		return (y-0.1f)*this.zoomLevel;
	}
	
	private float[] getMapCoordinates(float mouseX, float mouseY)
	{
		float xMouse = 2*(mouseX / this.window.windowWidth) - 1;
		float yMouse = 2*(mouseY / this.window.windowHeight) - 1;
		float xCoord = xMouse - this.transformX(this.x);
		float yCoord = yMouse - this.transformY(this.y);
	//	System.out.println("("+xCoord+", "+yCoord+")");
		xCoord = (xCoord*(this.window.windowWidth/640))/(InputHandler.X_FIELD_SIZE);
		yCoord = (yCoord*(this.window.windowHeight/480))/InputHandler.Y_FIELD_SIZE;
		glColor3f(1.0f, 0.0f, 0.0f);
		glBegin(GL_QUADS);
		glVertex2f(xCoord - 0.1f, yCoord - 0.1f);
		glVertex2f(xCoord + 0.1f, yCoord - 0.1f);
		glVertex2f(xCoord + 0.1f, yCoord + 0.1f);
		glVertex2f(xCoord - 0.1f, yCoord + 0.1f);
		glEnd();
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
