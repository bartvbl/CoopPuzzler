package editor.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.Rectangle;

public class Button {
	protected final int x;
	protected final int y;
	protected final int width;
	protected final int height;
	
	public Button(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean isHovering(float scaleLevel)
	{
		int mouseX = (int)(((float)Mouse.getX())/scaleLevel);
		int mouseY = (int)(((float)Mouse.getY())/scaleLevel);
		Rectangle rect = new Rectangle(this.x, this.y, this.width, this.height);
		if(rect.contains(mouseX, mouseY))
		{
			return true;
		} else {
			return false;
		}
	}
	
	public boolean draw(float scaleLevel)
	{
		boolean isHovering = this.isHovering(scaleLevel);
		if(isHovering && Mouse.isButtonDown(0))
		{
			this.drawMouseDown();
			return true;
		} else if(isHovering)
		{
			this.drawMouseOver();
			return true;
		} else {
			this.drawMouseUp();
			return false;
		}
	}
	
	protected void drawMouseDown(){}
	protected void drawMouseOver(){}
	protected void drawMouseUp(){}
}
