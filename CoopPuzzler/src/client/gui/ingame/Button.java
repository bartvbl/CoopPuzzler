package client.gui.ingame;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.Rectangle;

public abstract class Button {
	protected final int x;
	protected final int y;
	protected final int width;
	protected final int height;
	private boolean wasMouseDown = false;
	
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
			if(!this.wasMouseDown) {
				this.onClick();
			}
			this.wasMouseDown = true;
			return true;
		} else if(isHovering)
		{
			this.drawMouseOver();
			this.wasMouseDown = false;
			return true;
		} else {
			this.drawMouseUp();
			this.wasMouseDown = false;
			return false;
		}
	}
	
	protected void onClick() {}

	protected abstract void drawMouseDown();
	protected abstract void drawMouseOver();
	protected abstract void drawMouseUp();
}
