package client.drawing;

import org.lwjgl.util.Color;

import client.GameSettings;
import client.OperationMode;
import client.gl.NumberDrawer;
import client.gl.Texture;
import common.puzzle.PuzzleField;
import static org.lwjgl.opengl.GL11.*;

public class BoardDrawer {
	private static final int FIELD_SIZE = PuzzleDrawer.FIELD_SIZE;
	
	public static int createBoardBareBonesDisplayList(PuzzleField[][] table, int boardBareBonesDisplayListID, Texture nonumTexture)
	{
		if(boardBareBonesDisplayListID != -1)
		{
			glDeleteLists(boardBareBonesDisplayListID, 1);
		}
		int listID = glGenLists(1);
		glNewList(listID, GL_COMPILE);
		drawBackground(table);
		drawFilledSquares(table);
		if(GameSettings.operationMode == OperationMode.EDITOR) {			
			drawNoReferenceTextures(table,nonumTexture);
		}
		glEndList();
		return listID;
	}
	
	public static int createBoardDetailsDisplayList(PuzzleField[][] table, NumberDrawer numberDrawer, TextureLibrary textureLibrary, int boardFeaturesDisplayListID)
	{
		if(boardFeaturesDisplayListID != -1)
		{
			glDeleteLists(boardFeaturesDisplayListID, 1);
		}
		int listID = glGenLists(1);
		glNewList(listID, GL_COMPILE);
		drawPuzzleGrid(table);
		drawReferences(table, numberDrawer);
		if(GameSettings.operationMode != OperationMode.EDITOR) {
			drawLetters(table, textureLibrary);			
		}
		glEndList();
		return listID;
	}
	
	private static void drawNoReferenceTextures(PuzzleField[][] table, Texture nonumTexture) {
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, nonumTexture.texRef);
		glBegin(GL_QUADS);
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				if(table[i][j].hasIgnoreReference)
				{
					glTexCoord2f(0,0);
					drawVertex(FIELD_SIZE * j, FIELD_SIZE * i);
					glTexCoord2f(1,0);
					drawVertex(FIELD_SIZE * j + FIELD_SIZE, FIELD_SIZE * i);
					glTexCoord2f(1,1);
					drawVertex(FIELD_SIZE * j + FIELD_SIZE, FIELD_SIZE * i + FIELD_SIZE);
					glTexCoord2f(0,1);
					drawVertex(FIELD_SIZE * j, FIELD_SIZE * i + FIELD_SIZE);
				}
			}
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);
	}
	
	private static void drawFilledSquares(PuzzleField[][] table)
	{
		glBegin(GL_QUADS);
		glColor3f(0.0f, 0.0f, 0.0f);
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				if(table[i][j].isFilled)
				{
					drawVertex(FIELD_SIZE * j, FIELD_SIZE * i);
					drawVertex(FIELD_SIZE * j + FIELD_SIZE, FIELD_SIZE * i);
					drawVertex(FIELD_SIZE * j + FIELD_SIZE, FIELD_SIZE * i + FIELD_SIZE);
					drawVertex(FIELD_SIZE * j, FIELD_SIZE * i + FIELD_SIZE);
				}
			}
		}
		glEnd();
	}
	
	private static void drawBackground(PuzzleField[][] table)
	{
		glBegin(GL_QUADS);
		//draw background
		glColor3f(1.0f, 1.0f, 1.0f);
		drawVertex(0, 0);
		drawVertex(FIELD_SIZE * table[0].length, 0);
		drawVertex(FIELD_SIZE * table[0].length, FIELD_SIZE * table.length);
		drawVertex(0, FIELD_SIZE * table.length);
		glEnd();
	}
	
	private static void drawReferences(PuzzleField[][] table, NumberDrawer numberDrawer)
	{
		glEnable(GL_TEXTURE_2D);
		
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				if(table[i][j].questionReference != -1)
				{
					numberDrawer.drawNumber(table[i][j].questionReference, i, j, table);
				}
			}
		}
		glDisable(GL_TEXTURE_2D);
	}
	
	private static void drawPuzzleGrid(PuzzleField[][] table)
	{
		glLineWidth(1.0f);
		glBegin(GL_LINES);
		glColor3f(0.0f, 0.0f, 0.0f);
		for(int i = 0; i <= table.length; i++)
		{
			drawVertex(0, i*FIELD_SIZE);
			drawVertex(FIELD_SIZE * table[0].length, i*FIELD_SIZE);
		}
		for(int j = 0; j <= table[0].length; j++)
		{
			drawVertex(j*FIELD_SIZE, 0);
			drawVertex(j*FIELD_SIZE, FIELD_SIZE * table.length);
		}
		glEnd();
	}
	
	private static void drawLetters(PuzzleField[][] table, TextureLibrary textureLibrary) {
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
					Texture tex = textureLibrary.getTextTexture(table[i][j].getCurrentValueOfField());
					glBindTexture(GL_TEXTURE_2D, tex.texRef);
					Color colour = table[i][j].getFieldTextColour().getColour();
					
					float red = ((float)colour.getRed())/255;
					float green = ((float)colour.getGreen())/255;
					float blue = ((float)colour.getBlue())/255;
					
					glColor4f(red, green, blue, 1.0f);
					glBegin(GL_QUADS);
					glTexCoord2f(1,1);
					drawVertex(j * FIELD_SIZE + FIELD_SIZE - OFFSET + XOFFSET, i * FIELD_SIZE + OFFSET + YOFFSET);
					glTexCoord2f(0,1);
					drawVertex(j * FIELD_SIZE + OFFSET + XOFFSET, i * FIELD_SIZE + OFFSET + YOFFSET);
					glTexCoord2f(0,0);
					drawVertex(j * FIELD_SIZE + OFFSET + XOFFSET, i * FIELD_SIZE + FIELD_SIZE - OFFSET + YOFFSET);
					glTexCoord2f(1,0);
					drawVertex(j * FIELD_SIZE + FIELD_SIZE - OFFSET + XOFFSET, i * FIELD_SIZE + FIELD_SIZE - OFFSET + YOFFSET);
					glEnd();
					
				}
			}
		}
		glDisable(GL_TEXTURE_2D);
	}
	
	private static void drawVertex(float x, float y)
	{
		glVertex2f(x, y);
	}
}
