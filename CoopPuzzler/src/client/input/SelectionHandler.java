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
	private final PuzzleTable puzzleTable;
	private final ClientMain main;
	private final ClientWindow window;
	private final ArrayList<Point> currentSelection = new ArrayList<Point>();
	
	private int mapNumRows, mapNumColumns;
	private boolean isTyping = false;
	
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

	public Point[] getSelectionArray() {
		return this.currentSelection.toArray(new Point[currentSelection.size()]);
	}
	
	public boolean isTyping() {
		return this.isTyping;
	}
}
