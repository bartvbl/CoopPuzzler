package client.input.selection;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

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
		ArrayList<Point> selection = new ArrayList<Point>();

		Point puzzleLocation = this.getSelectedField(mapCoordinates);
		int row = (int) puzzleLocation.row;
		int column = (int) puzzleLocation.column;
		
		if(this.main.puzzleTable.fieldIsOccupied(row, column))
		{
			return selection;
		}
		
		selection.add(new Point((int)mapCoordinates.x, (int)mapCoordinates.y));
		double remainderX = mapCoordinates.x % 1;
		double remainderY = mapCoordinates.y % 1;
		//lower quadrant
		if((remainderX > remainderY) && (remainderX < (1 - remainderY)))
		{
			int tracker = this.mapNumRows - (int)mapCoordinates.y;
			int counter = 1;
			while(tracker < this.mapNumRows)
			{
				if(this.main.puzzleTable.fieldIsOccupied(tracker, (int) mapCoordinates.x))
				{
					break;
				}
				selection.add(new Point((int)mapCoordinates.x,(int)mapCoordinates.y-counter));
				tracker++;
				counter++;
			}
		}
		//right quadrant
		if((remainderX > remainderY) && (remainderX > (1 - remainderY)))
		{
			int tracker = (int)mapCoordinates.x + 1;
			int counter = 1;
			while(tracker < this.mapNumColumns)
			{
				if(this.main.puzzleTable.fieldIsOccupied((int) this.mapNumRows - (int)mapCoordinates.y-1,tracker))
				{
					break;
				}
				selection.add(new Point((int)mapCoordinates.x+counter,(int)mapCoordinates.y));
				tracker++;
				counter++;
			}
		}
		return selection;
	}

	@Override
	public boolean handleSelection(Point mapCoordinates) {
		return false;
	}

}
