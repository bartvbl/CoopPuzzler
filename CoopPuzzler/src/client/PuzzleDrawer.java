package client;

import java.util.ArrayList;

import org.lwjgl.util.Color;
import org.lwjgl.util.Point;

import client.gl.Texture;
import common.FontColour;
import common.PuzzleField;
import common.PuzzleTable;
import static org.lwjgl.opengl.GL11.*;

public class PuzzleDrawer {
	private TextureLibrary textureLibrary;
	private PuzzleTable puzzleTable;
	public static final int FIELD_SIZE = 1;
	private InputHandler inputHandler;
	
	public PuzzleDrawer(PuzzleTable puzzleTable, InputHandler inputHandler)
	{
		this.puzzleTable = puzzleTable;
		this.inputHandler = inputHandler;
	}
	
	public void init()
	{
		this.textureLibrary = new TextureLibrary();
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
		this.drawLetters(table);
	}
	
	private void drawLetters(PuzzleField[][] table) {
		glEnable(GL_TEXTURE_2D);
		
		final float OFFSET = -0.5f;
		final float YOFFSET = -0.65f;
		final float XOFFSET = 0.90f;
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				if(table[i][j].getCurrentValueOfField() != ' ')
				{
					Texture tex = this.textureLibrary.getTextTexture(table[i][j].getCurrentValueOfField());
					glBindTexture(GL_TEXTURE_2D, tex.texRef);
					Color colour = table[i][j].getFieldTextColour().getColour();
					float red = ((float)colour.getRed())/255;
					float green = ((float)colour.getGreen())/255;
					float blue = ((float)colour.getBlue())/255;
					glColor4f(red, green, blue, 1.0f);
					glBegin(GL_QUADS);
					glTexCoord2f(1,1);
					this.drawVertex(j * FIELD_SIZE + FIELD_SIZE - OFFSET + XOFFSET, (table.length - i-1) * FIELD_SIZE + OFFSET + YOFFSET);
					glTexCoord2f(0,1);
					this.drawVertex(j * FIELD_SIZE + OFFSET + XOFFSET, (table.length - i-1) * FIELD_SIZE + OFFSET + YOFFSET);
					glTexCoord2f(0,0);
					this.drawVertex(j * FIELD_SIZE + OFFSET + XOFFSET, (table.length - i-1) * FIELD_SIZE + FIELD_SIZE - OFFSET + YOFFSET);
					glTexCoord2f(1,0);
					this.drawVertex(j * FIELD_SIZE + FIELD_SIZE - OFFSET + XOFFSET, (table.length - i-1) * FIELD_SIZE + FIELD_SIZE - OFFSET + YOFFSET);
					glEnd();
					
				}
			}
		}
		glDisable(GL_TEXTURE_2D);
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

	private void drawReferences(PuzzleField[][] table)
	{
		glEnable(GL_TEXTURE_2D);
		final float OFFSET = 0.03f;
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				if(table[i][j].questionReference != -1)
				{
					Texture tex = this.textureLibrary.getReferenceTexture(table[i][j].questionReference);
					glBindTexture(GL_TEXTURE_2D, tex.texRef);
					glBegin(GL_QUADS);
					glTexCoord2f(1,1);
					this.drawVertex(j * FIELD_SIZE + FIELD_SIZE - OFFSET, (table.length - i-1) * FIELD_SIZE + OFFSET);
					glTexCoord2f(0,1);
					this.drawVertex(j * FIELD_SIZE + OFFSET, (table.length - i-1) * FIELD_SIZE + OFFSET);
					glTexCoord2f(0,0);
					this.drawVertex(j * FIELD_SIZE + OFFSET, (table.length - i-1) * FIELD_SIZE + FIELD_SIZE - OFFSET);
					glTexCoord2f(1,0);
					this.drawVertex(j * FIELD_SIZE + FIELD_SIZE - OFFSET, (table.length - i-1) * FIELD_SIZE + FIELD_SIZE - OFFSET);
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
			this.drawVertex(0, (table.length - i)*FIELD_SIZE);
			this.drawVertex(FIELD_SIZE * table[0].length, (table.length - i)*FIELD_SIZE);
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
					this.drawVertex(FIELD_SIZE * j, FIELD_SIZE * (table.length - i-1));
					this.drawVertex(FIELD_SIZE * j + FIELD_SIZE, FIELD_SIZE * (table.length - i-1));
					this.drawVertex(FIELD_SIZE * j + FIELD_SIZE, FIELD_SIZE * (table.length - i-1) + FIELD_SIZE);
					this.drawVertex(FIELD_SIZE * j, FIELD_SIZE * (table.length - i-1) + FIELD_SIZE);
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
		glVertex2f(x, y);
	}
}
