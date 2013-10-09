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

		int x = (int) mapCoordinates.x;
		int y = (int) mapCoordinates.y;
		
		if(this.main.puzzleTable.fieldIsOccupied(x, y))
		{
			return selection;
		}
		
		selection.add(new Point(x, y));
		double remainderX = mapCoordinates.x % 1;
		double remainderY = mapCoordinates.y % 1;
		//lower quadrant
		if((remainderX > remainderY) && (remainderX < (1 - remainderY)))
		{
			int tracker = y;
			int counter = 1;
			while(tracker < this.mapNumRows)
			{
				if(this.main.puzzleTable.fieldIsOccupied(tracker, y))
				{
					break;
				}
				selection.add(new Point(x, y - counter));
				tracker++;
				counter++;
			}
		}
		//right quadrant
		if((remainderX > remainderY) && (remainderX > (1 - remainderY)))
		{
			int tracker = x + 1;
			int counter = 1;
			while(tracker < this.mapNumColumns)
			{
				if(this.main.puzzleTable.fieldIsOccupied(x,tracker))
				{
					break;
				}
				selection.add(new Point(x + counter, y));
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
