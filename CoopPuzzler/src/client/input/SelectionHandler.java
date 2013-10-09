package client.input;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import client.ClientMain;
import client.ClientWindow;
import client.GameSettings;
import client.OperationMode;
import client.input.selection.EditorSelectionHandler;
import client.input.selection.PuzzleSelectionHandler;
import client.utils.CoordConverter;
import client.utils.Point;

public class SelectionHandler {
	private final ClientMain main;
	private final ClientWindow window;
	
	private PuzzleSelectionHandler puzzleHandler;
	private EditorSelectionHandler editorHandler;

	private boolean wasMouseDown = false;
	private boolean isSelected = false;
	private ArrayList<Point> currentSelection = new ArrayList<Point>();
	
	public SelectionHandler(ClientMain main)
	{
		this.window = main.window;
		this.main = main;

		this.puzzleHandler = new PuzzleSelectionHandler(main);
		this.editorHandler = new EditorSelectionHandler(main);
	}

	public void init()
	{
		int mapSizeX = main.puzzleTable.puzzleTable.length;	
		int mapSizeY = main.puzzleTable.puzzleTable[0].length;
		Point puzzleDimensions = new Point(mapSizeX, mapSizeY);
		this.puzzleHandler.init(puzzleDimensions);
		this.editorHandler.init(puzzleDimensions);
	}
	
	public void handleSelection(double zoomLevel, double mouseX, double mouseY) {
		Point mapCoordinates = getMapCoordinates(mouseX, mouseY, zoomLevel);
		if(isSelected) {
			if(cancelSelectionRequested()) {
				isSelected = false;
			} else {
				handleSelected(mapCoordinates);
			}
		} else {
			if(wasClicked()) {
				isSelected = true;
			} else {
				handleHovering(mapCoordinates);
			}
		}
	}
	
	private void handleHovering(Point mapCoordinates) {
		if(isEditorMode()) {
			currentSelection = editorHandler.getCurrentSelection(mapCoordinates);
		} else {
			currentSelection = puzzleHandler.getCurrentSelection(mapCoordinates);
		}
	}
	
	private void handleSelected(Point mapCoordinates) {
		if(isEditorMode()) {
			isSelected = editorHandler.handleSelection(mapCoordinates);
		} else {
			isSelected = puzzleHandler.handleSelection(mapCoordinates);
		}
	}

	private boolean isEditorMode() {
		return GameSettings.operationMode == OperationMode.EDITOR;
	}

	private boolean wasClicked() {
		if((Mouse.isButtonDown(0) == false) && (wasMouseDown == true)) {
			this.wasMouseDown = false;
			return true;
		}
		if(Mouse.isButtonDown(0)) {
			this.wasMouseDown = true;
		} else {
			this.wasMouseDown = false;
		}
		return false;
	}

	private boolean cancelSelectionRequested() {
		return (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) || (Mouse.isButtonDown(1)) || (Mouse.isButtonDown(0));
	}

	public Point getMapCoordinates(double x, double y, double zoomLevel)
	{
		Point coords = CoordConverter.getMapCoordinates(x, y, zoomLevel, this.window.windowWidth, this.window.windowHeight);
		return coords.displaceBy(new Point(-x, -y));
	}

	public Point[] getSelectionArray() {
		return this.currentSelection.toArray(new Point[currentSelection.size()]);
	}
	
	public boolean isTyping() {
		return this.isSelected && !isEditorMode();
	}
}
