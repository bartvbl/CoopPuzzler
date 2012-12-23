package editor.gui;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.Color;

import editor.common.FontColour;

public class ColourPickerButton extends Button{
	private final Color colour;
	private final ColourPickerUI uiMain;
	private final int fontColourID;
	
	private static final float LINE_WIDTH_SELECTED = 4.0f;
	private static final float LINE_WIDTH_DESELECTED = 1.0f;
	
	private boolean isSelected = false;
	
	public ColourPickerButton(ColourPickerUI uiMain, int fontColourID, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.colour = FontColour.colours[fontColourID];
		this.uiMain = uiMain;
		this.fontColourID = fontColourID;
	}
	
	public int getFontColourID()
	{
		return this.fontColourID;
	}
	
	public void setSelected(boolean selected)
	{
		this.isSelected = selected;
	}
	
	protected void drawMouseDown()
	{
		this.isSelected = true;
		this.uiMain.notifySelected(fontColourID);
		this.doDrawing(this.getRGB(-0.1f, -0.1f, -0.1f));
	}

	protected void drawMouseUp()
	{
		this.doDrawing(this.getRGB(0.0f, 0.0f, 0.0f));
	}
	
	protected void drawMouseOver()
	{
		this.doDrawing(this.getRGB(0.1f, 0.1f, 0.1f));
	}
	
	private float[] getRGB(float offsetRed, float offsetGreen, float offsetBlue)
	{
		float red = ((float)this.colour.getRed()) / 255f; 
		float green = ((float)this.colour.getGreen()) / 255f; 
		float blue = ((float)this.colour.getBlue()) / 255f; 
		float[] colours = new float[3];
		colours[0] = this.clamp(red + offsetRed);
		colours[1] = this.clamp(green + offsetGreen);
		colours[2] = this.clamp(blue + offsetBlue);
		return colours;
	}
	
	private float clamp(float value)
	{
		if(value < 0.0f)
		{
			return 0.0f;
		} else if(value > 1.0f)
		{
			return 1.0f;
		} else {
			return value;
		}
	}
	
	private void doDrawing(float[] fieldColour)
	{
		glColor4f(fieldColour[0], fieldColour[1], fieldColour[2], 1.0f);
		glBegin(GL_QUADS);
		glVertex2f(x, y);
		glVertex2f(x+width, y);
		glVertex2f(x+width, y+height);
		glVertex2f(x, y+height);
		glEnd();
		glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
		if(this.isSelected)
		{
			glLineWidth(LINE_WIDTH_SELECTED);
		} else {
			glLineWidth(LINE_WIDTH_DESELECTED);
		}
		
		glBegin(GL_LINES);
		glVertex2f(x, y);
		glVertex2f(x+width, y);
		
		glVertex2f(x+width, y);
		glVertex2f(x+width, y+height);
		
		glVertex2f(x+width, y+height);
		glVertex2f(x, y+height);
		
		glVertex2f(x, y+height);
		glVertex2f(x, y);
		glEnd();
	}
}
