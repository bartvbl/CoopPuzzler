package client.input.selection;

import java.util.ArrayList;

import client.ClientMain;
import client.utils.Point;

public abstract class FieldSelectionHandler {
	protected final ClientMain main;
	private Point mapBounds;

	public FieldSelectionHandler(ClientMain main) {
		this.main = main;
	}
	
	public void init(Point mapBounds) {
		this.mapBounds = mapBounds;
	}
	
	public ArrayList<Point> getCurrentSelection(Point mapCoordinates) {
		if((mapCoordinates.x >= 0) && (mapCoordinates.x <= mapBounds.x)
		&& (mapCoordinates.y >= 0) && (mapCoordinates.y <= mapBounds.y)) {
			return getSelectedFields(mapCoordinates);
		} else {
			return new ArrayList<Point>();
		}
	}
	
	protected Point getSelectedField(Point mouseLocation) {
		int row = (int) (this.mapBounds.row - mouseLocation.y);
		int column = (int) mouseLocation.x;
		return new Point(row, column);
	}

	protected abstract ArrayList<Point> getSelectedFields(Point mapCoordinates);
	public abstract boolean handleSelection(Point mapCoordinates);
}
