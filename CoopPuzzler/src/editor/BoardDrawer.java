package editor;

import org.lwjgl.util.Color;

import editor.common.PuzzleField;
import editor.gl.Texture;
import static org.lwjgl.opengl.GL11.*;

public class BoardDrawer {
	private static final int FIELD_SIZE = PuzzleDrawer.FIELD_SIZE;

	public static int createBoardBareBonesDisplayList(PuzzleField[][] table, Texture nonumTexture, int boardBareBonesDisplayListID)
	{
		if(boardBareBonesDisplayListID != -1)
		{
			glDeleteLists(boardBareBonesDisplayListID, 1);
		}
		int listID = glGenLists(1);
		glNewList(listID, GL_COMPILE);
		drawBackground(table);
		drawFilledSquares(table);
		drawNoReferenceTextures(table,nonumTexture);
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
					drawVertex(FIELD_SIZE * j, FIELD_SIZE * (table.length - i-1));
					glTexCoord2f(1,0);
					drawVertex(FIELD_SIZE * j + FIELD_SIZE, FIELD_SIZE * (table.length - i-1));
					glTexCoord2f(1,1);
					drawVertex(FIELD_SIZE * j + FIELD_SIZE, FIELD_SIZE * (table.length - i-1) + FIELD_SIZE);
					glTexCoord2f(0,1);
					drawVertex(FIELD_SIZE * j, FIELD_SIZE * (table.length - i-1) + FIELD_SIZE);
				}
			}
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);
	}

	public static int createBoardDetailsDisplayList(PuzzleField[][] table, int boardFeaturesDisplayListID)
	{
		if(boardFeaturesDisplayListID != -1)
		{
			glDeleteLists(boardFeaturesDisplayListID, 1);
		}
		int listID = glGenLists(1);
		glNewList(listID, GL_COMPILE);
		drawPuzzleGrid(table);
		glEndList();
		return listID;
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
					drawVertex(FIELD_SIZE * j, FIELD_SIZE * (table.length - i-1));
					drawVertex(FIELD_SIZE * j + FIELD_SIZE, FIELD_SIZE * (table.length - i-1));
					drawVertex(FIELD_SIZE * j + FIELD_SIZE, FIELD_SIZE * (table.length - i-1) + FIELD_SIZE);
					drawVertex(FIELD_SIZE * j, FIELD_SIZE * (table.length - i-1) + FIELD_SIZE);
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

	private static void drawPuzzleGrid(PuzzleField[][] table)
	{
		glLineWidth(1.0f);
		glBegin(GL_LINES);
		glColor3f(0.0f, 0.0f, 0.0f);
		for(int i = 0; i <= table.length; i++)
		{
			drawVertex(0, (table.length - i)*FIELD_SIZE);
			drawVertex(FIELD_SIZE * table[0].length, (table.length - i)*FIELD_SIZE);
		}
		for(int j = 0; j <= table[0].length; j++)
		{
			drawVertex(j*FIELD_SIZE, 0);
			drawVertex(j*FIELD_SIZE, FIELD_SIZE * table.length);
		}
		glEnd();
	}

	private static void drawVertex(float x, float y)
	{
		glVertex2f(x, y);
	}
}
