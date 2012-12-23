package editor;

import java.util.ArrayList;

import org.lwjgl.util.Color;
import org.lwjgl.util.Point;

import editor.common.FontColour;
import editor.common.PuzzleField;
import editor.common.PuzzleTable;
import editor.gl.Texture;
import static org.lwjgl.opengl.GL11.*;

public class PuzzleDrawer {
	private PuzzleTable puzzleTable;
	public static final int FIELD_SIZE = 1;
	private InputHandler inputHandler;
	private int boardBareBonesDisplayListID = -1;
	private int boardFeaturesDisplayListID = -1;
	private Texture nonumTexture;
	
	public PuzzleDrawer(PuzzleTable puzzleTable, InputHandler inputHandler)
	{
		this.puzzleTable = puzzleTable;
		this.inputHandler = inputHandler;
		
	}
	
	public void updateFeatureDisplayList()
	{
		this.boardFeaturesDisplayListID = BoardDrawer.createBoardDetailsDisplayList(puzzleTable.puzzleTable, boardFeaturesDisplayListID);
	}
	
	public void updateBareBonesDisplayList() {
		this.boardBareBonesDisplayListID = BoardDrawer.createBoardBareBonesDisplayList(puzzleTable.puzzleTable, nonumTexture, this.boardBareBonesDisplayListID);
	}
	
	public void init()
	{
		this.nonumTexture = new Texture("res/nonum.png");
		this.boardBareBonesDisplayListID = BoardDrawer.createBoardBareBonesDisplayList(puzzleTable.puzzleTable,nonumTexture, this.boardBareBonesDisplayListID);
		this.boardFeaturesDisplayListID = BoardDrawer.createBoardDetailsDisplayList(puzzleTable.puzzleTable, this.boardFeaturesDisplayListID);
	}

	public void draw()
	{
		PuzzleField[][] table = puzzleTable.puzzleTable;
		glCallList(this.boardBareBonesDisplayListID);
		this.drawSelection(table);
		glCallList(this.boardFeaturesDisplayListID);
	}
	
	

	private void drawSelection(PuzzleField[][] table) {
		ArrayList<Point> selectionList = this.inputHandler.getSelectionArray();
		glBegin(GL_QUADS);
		if(this.inputHandler.isTyping())
		{
			glColor4f(1.0f, 0.797f, 0.0f, 1.0f);
		} else {
			glColor4f(1.0f, 0.797f, 0.0f, 0.7f);
		}
		
		for(Point point : selectionList)
		{
			this.drawVertex(FIELD_SIZE * point.getX(), FIELD_SIZE * point.getY());
			this.drawVertex(FIELD_SIZE * point.getX() + FIELD_SIZE, FIELD_SIZE * point.getY());
			this.drawVertex(FIELD_SIZE * point.getX() + FIELD_SIZE, FIELD_SIZE * point.getY() + FIELD_SIZE);
			this.drawVertex(FIELD_SIZE * point.getX(), FIELD_SIZE * point.getY() + FIELD_SIZE);
		}
		glEnd();
	}

	private void drawVertex(float x, float y)
	{
		glVertex2f(x, y);
	}
}
