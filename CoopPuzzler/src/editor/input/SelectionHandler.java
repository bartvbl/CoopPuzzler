package editor.input;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.lwjgl.util.Timer;

import client.ClientWindow;


import editor.EditorMain;
import editor.EditorWindow;
import editor.common.FontColour;
import editor.common.PuzzleField;
import editor.common.PuzzleTable;
import editor.utils.CoordConverter;

public class SelectionHandler {
	private static final float WAITING_TIME = 0.2f;

	private PuzzleTable puzzleTable;
	private Timer selectionActionTimer = new Timer();
	private float selectionActionStartTime;
	private boolean isWaiting = false;
	private int mapNumRows, mapNumColumns;
	private EditorMain main;
	private ClientWindow window;

	public ArrayList<Point> selectionArray = new ArrayList<Point>();
	public boolean isTyping = false;

	public SelectionHandler(EditorMain main)
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

	public void handleSelection(float zoomLevel, float x, float y) {
		Timer.tick();
		if(isWaiting)
		{
			if((this.selectionActionTimer.getTime() - this.selectionActionStartTime) > WAITING_TIME)
			{
				this.isWaiting = false;
			}
		} else {
			this.selectionArray.clear();
			float[] coords = this.getMapCoordinates(x, y, zoomLevel);

			if(!((coords[0] < 0) || (coords[1] < 0) || (coords[1] >= this.mapNumRows) || (coords[0] >= this.mapNumColumns)))
			{
				int column = (int)coords[0];
				int row = this.mapNumRows - (int)coords[1] -1;
				
				this.selectionArray.add(new Point((int)coords[0],(int)coords[1]));
				
				if(Mouse.isButtonDown(0))
				{
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
					this.main.puzzleDrawer.updateBareBonesDisplayList();
					this.waitForNextInput();
				}
			}
		}
	}

	private void waitForNextInput()
	{
		this.isWaiting = true;
		this.selectionActionStartTime = this.selectionActionTimer.getTime();
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

	public boolean isTyping() {
		return this.isTyping;
	}
}
