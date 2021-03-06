package client.drawing;

import java.util.ArrayList;

import org.lwjgl.util.Color;

import client.ClientMain;
import client.OperationMode;
import client.gl.NumberDrawer;
import client.gl.Texture;
import client.input.InputHandler;
import client.utils.Point;
import common.FontColour;
import common.puzzle.PuzzleField;
import common.puzzle.PuzzleTable;
import static org.lwjgl.opengl.GL11.*;

public class PuzzleDrawer {
	private TextureLibrary textureLibrary;
	private PuzzleTable puzzleTable;
	public static final int FIELD_SIZE = 1;
	private InputHandler inputHandler;
	private int boardBareBonesDisplayListID = -1;
	private int boardFeaturesDisplayListID = -1;
	private NumberDrawer numberDrawer;
	private Texture nonumTexture;
	
	public PuzzleDrawer(PuzzleTable puzzleTable, InputHandler inputHandler)
	{
		this.puzzleTable = puzzleTable;
		this.inputHandler = inputHandler;
	}
	
	public void updateBareBonesDisplayList() {
		this.boardBareBonesDisplayListID = BoardDrawer.createBoardBareBonesDisplayList(puzzleTable.puzzleTable, this.boardBareBonesDisplayListID, this.nonumTexture);
	}
	
	public void updateFeatureDisplayList()
	{
		this.boardFeaturesDisplayListID = BoardDrawer.createBoardDetailsDisplayList(puzzleTable.puzzleTable, numberDrawer, textureLibrary, boardFeaturesDisplayListID);
	}
	
	public void init()
	{
		this.nonumTexture = new Texture("res/nonum.png");
		this.numberDrawer = new NumberDrawer();
		this.textureLibrary = new TextureLibrary();
		this.boardBareBonesDisplayListID = BoardDrawer.createBoardBareBonesDisplayList(puzzleTable.puzzleTable, this.boardBareBonesDisplayListID, this.nonumTexture);
		this.boardFeaturesDisplayListID = BoardDrawer.createBoardDetailsDisplayList(puzzleTable.puzzleTable, numberDrawer, textureLibrary, this.boardFeaturesDisplayListID);
	}

	public void draw()
	{
		PuzzleField[][] table = puzzleTable.puzzleTable;
		glCallList(this.boardBareBonesDisplayListID);
		this.drawSelection(table);		
		glCallList(this.boardFeaturesDisplayListID);
	}
	
	

	private void drawSelection(PuzzleField[][] table) {
		Point[] selectionList = this.inputHandler.getSelectionArray();
		glBegin(GL_QUADS);
		if(this.inputHandler.isTyping())
		{
			glColor4d(1.0, 0.797, 0.0, 1.0);
		} else {
			glColor4d(1.0, 0.797, 0.0, 0.5);
		}
		
		for(int i = 0; i < selectionList.length; i++)
		{
			Point point = selectionList[i];
			if(this.inputHandler.isTyping())
			{
				glColor4d(1.0, 0.797, 0.0, 1.0);
			} else {
				glColor4d(1.0, 0.797, 0.0, 0.5);
			}
			this.drawVertex(FIELD_SIZE * point.x, FIELD_SIZE * point.y);
			this.drawVertex(FIELD_SIZE * point.x + FIELD_SIZE, FIELD_SIZE * point.y);
			this.drawVertex(FIELD_SIZE * point.x + FIELD_SIZE, FIELD_SIZE * point.y + FIELD_SIZE);
			this.drawVertex(FIELD_SIZE * point.x, FIELD_SIZE * point.y + FIELD_SIZE);
		}
		glEnd();
		
		if(this.inputHandler.isTyping() && this.inputHandler.getSelectionArray().length > 0)
		{
			Point cursorLocation = selectionList[0];
			glColor4d(0, 0, 0, 1);
			glLineWidth(4);
			glBegin(GL_LINES);
			this.drawVertex(FIELD_SIZE * cursorLocation.x, FIELD_SIZE * cursorLocation.y);
			this.drawVertex(FIELD_SIZE * cursorLocation.x + FIELD_SIZE, FIELD_SIZE * cursorLocation.y);
			this.drawVertex(FIELD_SIZE * cursorLocation.x + FIELD_SIZE, FIELD_SIZE * cursorLocation.y);
			this.drawVertex(FIELD_SIZE * cursorLocation.x + FIELD_SIZE, FIELD_SIZE * cursorLocation.y + FIELD_SIZE);
			this.drawVertex(FIELD_SIZE * cursorLocation.x + FIELD_SIZE, FIELD_SIZE * cursorLocation.y + FIELD_SIZE);
			this.drawVertex(FIELD_SIZE * cursorLocation.x, FIELD_SIZE * cursorLocation.y + FIELD_SIZE);
			this.drawVertex(FIELD_SIZE * cursorLocation.x, FIELD_SIZE * cursorLocation.y + FIELD_SIZE);
			this.drawVertex(FIELD_SIZE * cursorLocation.x, FIELD_SIZE * cursorLocation.y);
			glEnd();
		}
	}

	private void drawVertex(double x, double y)
	{
		glVertex2d(x, y);
	}
}
