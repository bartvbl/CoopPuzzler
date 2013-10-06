package client.input.selection;

import java.util.ArrayList;

import common.PuzzleField;
import common.ReferenceUpdater;

import client.ClientMain;
import client.utils.Point;

public class EditorSelectionHandler extends FieldSelectionHandler {

	public EditorSelectionHandler(ClientMain main) {
		super(main);
	}

	@Override
	public ArrayList<Point> getSelectedFields(Point mapCoordinates) {
		ArrayList<Point> selection = new ArrayList<Point>();
		selection.add(new Point(Math.floor(mapCoordinates.x),Math.floor(mapCoordinates.y)));
		return selection;
	}

	@Override
	public boolean handleSelection(Point mapCoordinates) {
		Point puzzleLocation = this.getSelectedField(mapCoordinates);
		int row = (int) puzzleLocation.row;
		int column = (int) puzzleLocation.column;
		
		PuzzleField puzzleField = this.main.puzzleTable.puzzleTable[row][column];
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
		ReferenceUpdater.updateReferences(this.main.puzzleTable.puzzleTable);
		this.main.puzzleDrawer.updateBareBonesDisplayList();
		this.main.puzzleDrawer.updateFeatureDisplayList();
		
		return false;
	}

}
