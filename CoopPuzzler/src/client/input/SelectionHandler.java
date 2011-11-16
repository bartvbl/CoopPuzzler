package client.input;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.lwjgl.util.Timer;

import client.ClientMain;
import client.ClientWindow;
import client.KeyboardToCharConverter;
import client.TextureLibrary;

import common.BoardUpdateEvent;
import common.FontColour;
import common.PuzzleTable;

public class SelectionHandler {
	private static final float WAITING_TIME = 0.2f;
	
	private PuzzleTable puzzleTable;
	private Timer selectionActionTimer = new Timer();
	private float selectionActionStartTime;
	private boolean isWaiting = false;
	private ArrayList<Point> selectionUndoList = new ArrayList<Point>();
	private char previousChar = ' ';
	private int mapNumRows, mapNumColumns;
	private ClientMain main;
	private ClientWindow window;
	
	public ArrayList<Point> selectionArray = new ArrayList<Point>();
	public boolean isTyping = false;
	
	public SelectionHandler(ClientMain main)
	{
		this.puzzleTable = main.puzzleTable;
		this.mapNumColumns = main.puzzleTable.puzzleTable[0].length;
		this.mapNumRows = main.puzzleTable.puzzleTable.length;
		this.window = main.window;
		this.main = main;
	}
	
	public void handleSelection(float zoomLevel, float x, float y) {
		Timer.tick();
		if(isWaiting)
		{
			if((this.selectionActionTimer.getTime() - this.selectionActionStartTime) > WAITING_TIME)
			{
				this.isWaiting = false;
			}
		} else {
			if(this.isTyping)
			{
				if(this.cancelSelectionRequested())
				{
					this.isTyping = false;
					this.waitForNextInput();
				}
				char typedKey = KeyboardToCharConverter.getKeyCharValue();
				if(typedKey != ' ')
				{
					
					this.isWaiting = true;
					this.selectionActionStartTime = this.selectionActionTimer.getTime();
//					this.previousChar = typedKey;
					
					Point point;
					if((this.previousChar == 'i') && (typedKey == 'j'))
					{
						point = this.selectionUndoList.get(this.selectionUndoList.size()-1);
						typedKey = TextureLibrary.IJ;
					} else {
						point = this.selectionArray.remove(0);
//						this.selectionUndoList.add(point);
					}
					int column = point.getX();
					int row = this.mapNumRows - point.getY() -1;
					BoardUpdateEvent update = new BoardUpdateEvent(row,column,typedKey,new FontColour(FontColour.DARK_BLUE));
					this.main.sendEventToServer(update);
//					this.puzzleTable.puzzleTable[row][column].setNewCharacterValue(typedKey);
					
					if(this.selectionArray.size() == 0)
					{
						this.isTyping = false;
						this.waitForNextInput();
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
					if(this.selectionUndoList.size() == 0)
					{
						this.isTyping = false;
					}
					this.waitForNextInput();
				}
			} else {
				this.selectionArray.clear();
				float[] coords = this.getMapCoordinates(x, y, zoomLevel);
				
				if(!((coords[0] < 0) || (coords[1] < 0) || (coords[1] >= this.mapNumRows) || (coords[0] >= this.mapNumColumns)))
				{
					int column = (int)coords[0];
					int row = this.mapNumRows - (int)coords[1] -1;
					if(this.puzzleTable.fieldIsOccupied(row, column))
					{
						return;
					}
					this.selectionArray.add(new Point((int)coords[0],(int)coords[1]));
					float remainderX = coords[0] % 1;
					float remainderY = coords[1] % 1;
					//lower quadrant
					if((remainderX > remainderY) && (remainderX < (1 - remainderY)))
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
					if((remainderX > remainderY) && (remainderX > (1 - remainderY)))
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
						this.waitForNextInput();
						this.previousChar = ' ';
						this.selectionUndoList = new ArrayList<Point>();
					}
				}
			}
		}
	}
	
	private boolean cancelSelectionRequested() {
		return (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) || (Mouse.isButtonDown(1)) || (Mouse.isButtonDown(0));
	}

	private void waitForNextInput()
	{
		this.isWaiting = true;
		this.selectionActionStartTime = this.selectionActionTimer.getTime();
	}
	
	private float[] getMapCoordinates(float x, float y, float zoomLevel)
	{
		float aspect = this.window.windowWidth / this.window.windowHeight;
		float xCoord = Mouse.getX();
		float yCoord = Mouse.getY();
		xCoord /= this.window.windowHeight/2;
		xCoord -= 1;
		xCoord /= zoomLevel;
		xCoord -= x;
		xCoord -= (aspect - 1.0f)/zoomLevel;
		
		yCoord /= this.window.windowHeight/2;
		yCoord -= 1;
		yCoord /= zoomLevel;
		yCoord -= y;
		
		return new float[]{xCoord, yCoord};
	}

	public ArrayList<Point> getSelectionArray() {
		return this.selectionArray;
	}

	public boolean isTyping() {
		return this.isTyping;
	}
}
