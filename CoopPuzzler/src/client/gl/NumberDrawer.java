package client.gl;

import static org.lwjgl.opengl.GL11.*;
import client.drawing.PuzzleDrawer;

import common.PuzzleField;

public class NumberDrawer {
	private static final int FIELD_SIZE = PuzzleDrawer.FIELD_SIZE;
	
	private Texture numberTexture;
	
	public NumberDrawer()
	{
		this.numberTexture = new Texture("res/numbers.png");
	}
	
	public void drawNumber(int number, int row, int column, PuzzleField[][] table)
	{
		final float OFFSETX = 0.04f;
		final float OFFSETY = -0.03f;
		final float SEPARATION = 0.21f;
		final float CHARWIDTH = 0.2f;
		final float MAPCHARWIDTH = 0.23f;
		final float CHARHEIGHT = 0.3f;
		final float MAPCHARHEIGHT = 0.28f;
		
		final float LOX = -0.01f; //layerOffsetX
		final float LOY = 0.01f;; //layerOffsetY
		String num = Integer.toString(number);
		glBindTexture(GL_TEXTURE_2D, numberTexture.texRef);
		glBegin(GL_QUADS);
		for(int i = 0; i < num.length(); i++)
		{
			char currentChar = num.charAt(i);
			int currentValue = (int)currentChar - 48;
			float baseY = ((table.length - row-1) * FIELD_SIZE) + 1 + OFFSETY;
			float baseX = column * FIELD_SIZE + OFFSETX + i*SEPARATION;
			if(currentValue <= 4)
			{
				glTexCoord2f(currentValue*CHARWIDTH			,1);
				drawVertex(baseX							, baseY);
				glTexCoord2f((currentValue + 1)*CHARWIDTH - 0.005f	,1);
				drawVertex(baseX + MAPCHARWIDTH				, baseY);
				glTexCoord2f((currentValue + 1)*CHARWIDTH - 0.005f	,1 - CHARHEIGHT);
				drawVertex(baseX + MAPCHARWIDTH				, baseY - MAPCHARHEIGHT);
				glTexCoord2f(currentValue*CHARWIDTH			,1 - CHARHEIGHT);
				drawVertex(baseX							, baseY - MAPCHARHEIGHT);
			} else {
				glTexCoord2f(currentValue*CHARWIDTH	+ LOX		,1 - CHARHEIGHT + LOY);
				drawVertex(baseX - 0.01f								, baseY);
				glTexCoord2f((currentValue + 1)*CHARWIDTH + LOX	,1 - CHARHEIGHT + LOY);
				drawVertex(baseX + MAPCHARWIDTH	 - 0.01f		, baseY);
				glTexCoord2f((currentValue + 1)*CHARWIDTH + LOX	,1 - 1.98f*CHARHEIGHT + LOY);
				drawVertex(baseX + MAPCHARWIDTH	 - 0.01f		, baseY - MAPCHARHEIGHT);
				glTexCoord2f(currentValue*CHARWIDTH	+ LOX		,1 - 1.98f*CHARHEIGHT + LOY);
				drawVertex(baseX - 0.01f	 					, baseY - MAPCHARHEIGHT);
			}
			
		}
		
		glEnd();
	}
	
	private static void drawVertex(float x, float y)
	{
		glVertex2f(x, y);
	}
	
	private int getorderOfMagnitude(int number)
	{
		return (int) Math.floor(Math.log10(number));
	}
}
