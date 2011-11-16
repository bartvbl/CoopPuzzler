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
	private int mapNumRows, mapNumColumns;
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
	
	private final float initAspect;
	
	public InputHandler(ClientWindow window, int mapHeight, int mapWidth, PuzzleTable table)
	{
		this.puzzleTable = table;
		this.window = window;
		this.mapNumColumns = mapHeight;
		this.mapNumRows = mapWidth;
		this.initAspect = this.window.windowWidth/this.window.windowHeight;
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
					int row = this.mapNumRows - point.getY() -1;
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
					int row = this.mapNumRows - point.getY() -1;
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
				
				if(!((coords[0] < 0) || (coords[1] < 0) || (coords[1] >= this.mapNumRows) || (coords[0] >= this.mapNumColumns)))
				{
					int column = (int)coords[0];
					int row = this.mapNumRows - (int)coords[1] -1;
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
						int tracker = this.mapNumRows - (int)coords[1];
						int counter = 1;
						while(tracker < this.mapNumRows)
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
						while(tracker < this.mapNumColumns)
						{
							if(this.puzzleTable.fieldIsOccupied((int) this.mapNumRows - (int)coords[1]-1,tracker))
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
	
	private float[] getMapCoordinates(float mouseX, float mouseY)
	{
		float aspect = this.window.windowWidth / this.window.windowHeight;
		float xCoord = mouseX;
		float yCoord = mouseY;
		xCoord /= this.window.windowHeight/2;
		xCoord -= 1;
		xCoord /= this.zoomLevel;
		xCoord -= this.x;
		xCoord -= (aspect - 1.0f)/this.zoomLevel;
		
		yCoord /= this.window.windowHeight/2;
		yCoord -= 1;
		yCoord /= this.zoomLevel;
		yCoord -= this.y;
		
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
		float aspect = this.window.windowWidth / this.window.windowHeight;
		float bound = 1/this.zoomLevel;
		if(x < -1*bound*aspect - this.mapNumColumns/2)
		{
			x = -1*bound*aspect - this.mapNumColumns/2;
		}
		if(x > bound*aspect - this.mapNumColumns/2)
		{
			x = bound*aspect - this.mapNumColumns/2;
		}
		if(y < -1*bound - this.mapNumRows/2)
		{
			y = -1*bound - this.mapNumRows/2;
		}
		if(y > bound - this.mapNumRows/2)
		{
			y = bound - this.mapNumRows/2;
		}
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
