package client.input;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.lwjgl.util.Timer;

import client.ClientMain;
import client.ClientWindow;
import client.GameSettings;
import client.OperationMode;
import client.drawing.TextureLibrary;
import client.utils.CoordConverter;

import common.BoardUpdateEvent;
import common.FontColour;
import common.PuzzleField;
import common.PuzzleTable;
import common.ReferenceUpdater;

public class SelectionHandler {
	private PuzzleTable puzzleTable;
	private boolean finishedClicking = true;
	private ArrayList<Point> selectionUndoList = new ArrayList<Point>();
	private char previousChar = ' ';
	private int mapNumRows, mapNumColumns;
	private ClientMain main;
	private ClientWindow window;
	private int selectionCursor = 0;

	public ArrayList<Point> selectionArray = new ArrayList<Point>();
	private boolean isTyping = false;
	private Point previousPoint;

	public SelectionHandler(ClientMain main)
	{
		this.puzzleTable = main.puzzleTable;
		this.window = main.window;
		this.main = main;
	}

	public void init()
	{
		this.mapNumColumns = main.puzzleTable.puzzleTable[0].length;
		this.mapNumRows = main.puzzleTable.puzzleTable.length;
	}

	public void updateCharacter(BoardUpdateEvent event)
	{
		PuzzleField field = this.puzzleTable.puzzleTable[event.getRow()][event.getColumn()];
		field.setNewCharacterValue(event.getCharacterValue());
		field.setFieldTextColour(event.getColour());
		this.main.puzzleDrawer.updateFeatureDisplayList();
	}

	public void handleSelection(float zoomLevel, float x, float y) {
		char typedKey = KeyboardToCharConverter.getKeyCharValue();
		if((Mouse.isButtonDown(0) == false) && (this.finishedClicking == false)) {
			this.finishedClicking = true;
		}
		if(this.isTyping)
		{
			if((this.cancelSelectionRequested()) && (this.finishedClicking == true))
			{
				this.isTyping = false;
				this.finishedClicking = false;
			}
			if((typedKey == KeyboardToCharConverter.BACKSPACE))
			{
				if(this.selectionUndoList.size() > 0) {
					Point point = this.selectionUndoList.remove(this.selectionUndoList.size()-1);
					this.selectionArray.add(0, point);
					int column = point.getX();
					int row = this.mapNumRows - point.getY() -1;
					this.previousChar = this.puzzleTable.puzzleTable[row][column].getCurrentValueOfField();
					this.puzzleTable.puzzleTable[row][column].setNewCharacterValue(' ');
					this.main.puzzleDrawer.updateFeatureDisplayList();				
				}
			} else if(typedKey != KeyboardToCharConverter.NO_MATCH)
			{
				Point point;
				if((this.previousChar == 'i') && (typedKey == 'j'))
				{
					point = this.previousPoint;
					typedKey = TextureLibrary.IJ;
					this.selectionUndoList.remove(this.selectionUndoList.size() - 1);
				} else {
					if(typedKey != 'i')
					{
						if(this.selectionArray.size() == 0){
							this.isTyping = false;
							return;
						}
						point = this.selectionArray.remove(0);
					} else {
						point = this.selectionArray.get(0);
						this.selectionArray.remove(0);
					}
				}
				this.selectionUndoList.add(point);
				this.previousPoint = point;
				int column = point.getX();
				int row = this.mapNumRows - point.getY() -1;
				BoardUpdateEvent update = new BoardUpdateEvent(row,column,typedKey,this.main.colourPickerUI.getSelectedColour());
				if(GameSettings.operationMode == OperationMode.ONLINE_GAME)
				{
					this.main.sendEventToServer(update);
				} else {
					this.main.sendEventToClient(update);
				}
				this.previousChar = typedKey;
				if((this.selectionArray.size() == 0) && (typedKey!='i'))
				{
					this.isTyping = false;
				}
			}
		} else {
			this.selectionArray.clear();
			float[] coords = this.getMapCoordinates(x, y, zoomLevel);

			if(!((coords[0] < 0) || (coords[1] < 0) || (coords[1] >= this.mapNumRows) || (coords[0] >= this.mapNumColumns)))
			{
				int column = (int)coords[0];
				int row = this.mapNumRows - (int)coords[1] -1;
				if(GameSettings.operationMode == OperationMode.EDITOR) {
					this.selectionArray.add(new Point((int)coords[0],(int)coords[1]));

					if(Mouse.isButtonDown(0) && (finishedClicking == true))
					{
						finishedClicking = false;
						PuzzleField puzzleField = this.puzzleTable.puzzleTable[row][column];
						if((puzzleField.isFilled == false) && (puzzleField.hasIgnoreReference == false)) {
							puzzleField.isFilled = true;
							puzzleField.hasIgnoreReference = false;
						} else if((puzzleField.isFilled == true) && (puzzleField.hasIgnoreReference == false)) {
							puzzleField.isFilled = false;
							puzzleField.hasIgnoreReference = true;
						} else if((puzzleField.isFilled == false) && (puzzleField.hasIgnoreReference == true)) {
							puzzleField.isFilled = false;
							puzzleField.hasIgnoreReference = false;
						}
						ReferenceUpdater.updateReferences(this.puzzleTable.puzzleTable);
						this.main.puzzleDrawer.updateBareBonesDisplayList();
						this.main.puzzleDrawer.updateFeatureDisplayList();
					}
				} else {
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
					if((Mouse.isButtonDown(0)) && (this.finishedClicking == true))
					{
						this.finishedClicking = false;
						this.isTyping = true;
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

	public float[] getMapCoordinates(float x, float y, float zoomLevel)
	{
		float[] coords = CoordConverter.getMapCoordinates(x, y, zoomLevel, this.window.windowWidth, this.window.windowHeight);
		coords[0] -= x;
		coords[1] -= y;
		return coords;
	}

	public ArrayList<Point> getSelectionArray() {
		return this.selectionArray;
	}

	public int getSelectionCursor() {
		return 0;
	}

	public boolean isTyping() {
		return this.isTyping;
	}
}
