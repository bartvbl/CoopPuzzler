package client;

import java.util.ArrayList;

import org.lwjgl.util.Point;

import client.gl.Texture;
import common.PuzzleField;
import common.PuzzleTable;
import static org.lwjgl.opengl.GL11.*;

public class PuzzleDrawer {
	private TextureLibrary textureLibrary = new TextureLibrary();
	private PuzzleTable puzzleTable;
	public static final int FIELD_SIZE = 20;
	private InputHandler inputHandler;
	
	public PuzzleDrawer(PuzzleTable puzzleTable, InputHandler inputHandler)
	{
		this.puzzleTable = puzzleTable;
		this.inputHandler = inputHandler;
		this.parseFieldReferenceTextures();
	}
	
	private void parseFieldReferenceTextures() {
		PuzzleField[][] table = puzzleTable.puzzleTable;
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				if(table[i][j].questionReference != -1)
				{
					this.textureLibrary.addReferenceTexture(table[i][j].questionReference);
				}
			}
		}
	}

	public void draw()
	{
		PuzzleField[][] table = puzzleTable.puzzleTable;
		this.drawBackground(table);
		this.drawFilledSquares(table);
		this.drawSelection(table);
		this.drawPuzzleGrid(table);
		this.drawReferences(table);
	}
	
	private void drawSelection(PuzzleField[][] table) {
		ArrayList<Point> selectionList = this.inputHandler.selectionArray;
		glBegin(GL_QUADS);
		if(this.inputHandler.isTyping)
		{
			glColor4f(1.0f, 0.797f, 0.0f, 0.8f);
		} else {
			glColor4f(1.0f, 0.797f, 0.0f, 0.7f);
		}
		
		for(Point point : selectionList)
		{
//			this.drawVertex(0, 0);
//			this.drawVertex(0, FIELD_SIZE);
//			this.drawVertex(FIELD_SIZE, FIELD_SIZE);
//			this.drawVertex(FIELD_SIZE, 0);
			this.drawVertex(FIELD_SIZE * point.getY(), FIELD_SIZE * point.getX());
			this.drawVertex(FIELD_SIZE * point.getY() + FIELD_SIZE, FIELD_SIZE * point.getX());
			this.drawVertex(FIELD_SIZE * point.getY() + FIELD_SIZE, FIELD_SIZE * point.getX() + FIELD_SIZE);
			this.drawVertex(FIELD_SIZE * point.getY(), FIELD_SIZE * point.getX() + FIELD_SIZE);
		}
		glEnd();
	}

	private void drawReferences(PuzzleField[][] table)
	{
		glEnable(GL_TEXTURE_2D);
		glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				if(table[i][j].questionReference != -1)
				{
					Texture tex = this.textureLibrary.getReferenceTexture(table[i][j].questionReference);
					glBindTexture(GL_TEXTURE_2D, tex.texRef);
					glBegin(GL_QUADS);
					glTexCoord2f(1,0);
					this.drawVertex(j * FIELD_SIZE + FIELD_SIZE - 1, i * FIELD_SIZE + 1);
					glTexCoord2f(0,0);
					this.drawVertex(j * FIELD_SIZE + 1, i * FIELD_SIZE + 1);
					glTexCoord2f(0,1);
					this.drawVertex(j * FIELD_SIZE + 1, i * FIELD_SIZE + FIELD_SIZE - 1);
					glTexCoord2f(1,1);
					this.drawVertex(j * FIELD_SIZE + FIELD_SIZE - 1, i * FIELD_SIZE + FIELD_SIZE - 1);
					glEnd();
					
				}
			}
		}
		glDisable(GL_TEXTURE_2D);
	}
	
	private void drawPuzzleGrid(PuzzleField[][] table)
	{
		glLineWidth(1.0f);
		glBegin(GL_LINES);
		glColor3f(0.0f, 0.0f, 0.0f);
		for(int i = 0; i <= table.length; i++)
		{
			this.drawVertex(0, i*FIELD_SIZE);
			this.drawVertex(FIELD_SIZE * table[0].length, i*FIELD_SIZE);
		}
		for(int j = 0; j <= table[0].length; j++)
		{
			this.drawVertex(j*FIELD_SIZE, 0);
			this.drawVertex(j*FIELD_SIZE, FIELD_SIZE * table.length);
		}
		glEnd();
	}
	
	private void drawFilledSquares(PuzzleField[][] table)
	{
		glBegin(GL_QUADS);
		glColor3f(0.0f, 0.0f, 0.0f);
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				if(table[i][j].isFilled)
				{
					this.drawVertex(FIELD_SIZE * j, FIELD_SIZE * i);
					this.drawVertex(FIELD_SIZE * j + FIELD_SIZE, FIELD_SIZE * i);
					this.drawVertex(FIELD_SIZE * j + FIELD_SIZE, FIELD_SIZE * i + FIELD_SIZE);
					this.drawVertex(FIELD_SIZE * j, FIELD_SIZE * i + FIELD_SIZE);
				}
			}
		}
		glEnd();
	}
	
	private void drawBackground(PuzzleField[][] table)
	{
		glBegin(GL_QUADS);
		//draw background
		glColor3f(1.0f, 1.0f, 1.0f);
		this.drawVertex(0, 0);
		this.drawVertex(FIELD_SIZE * table[0].length, 0);
		this.drawVertex(FIELD_SIZE * table[0].length, FIELD_SIZE * table.length);
		this.drawVertex(0, FIELD_SIZE * table.length);
		glEnd();
	}
	
	private void drawVertex(float x, float y)
	{
		glVertex2f(x - 100, 100 - y);
	}
}
