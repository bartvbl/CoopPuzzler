package client.input.selection;

import java.util.ArrayList;

import client.ClientMain;
import client.utils.Point;

public class PuzzleSelectionHandler extends FieldSelectionHandler {
	private int mapNumRows, mapNumColumns;
	private boolean isTyping = false;
	
	
	public PuzzleSelectionHandler(ClientMain main) {
		super(main);
	}

	public boolean isTyping() {
		return false;
	}

	@Override
	protected ArrayList<Point> getSelectedFields(Point mapCoordinates) {
		return new ArrayList<Point>();
	}

	@Override
	public boolean handleSelection(Point mapCoordinates) {
		return false;
	}

}
