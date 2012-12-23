package client.gui.ingame;

import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;

import client.ClientMain;

import common.FontColour;

public class ColourPickerUI {
	private ArrayList<ColourPickerButton> colourPickerButtons;
	private FontColour currentSelectedColour;
	private ClientMain main;
	
	public ColourPickerUI(ClientMain main)
	{
		this.colourPickerButtons = new ArrayList<ColourPickerButton>();
		for(int i = 0; i < FontColour.colours.length; i++)
		{
			this.colourPickerButtons.add(new ColourPickerButton(this, i, 43*i + 3, 3, 40, 40));
		}
		this.colourPickerButtons.get(0).setSelected(true);
		this.currentSelectedColour = new FontColour(0);
		this.main = main;
	}
	
	public boolean draw()
	{
		float scaleLevel = this.main.window.windowHeight / 480f;
		glScalef(scaleLevel, scaleLevel, 0.0f);
		boolean hasHandledMouse = false;
		boolean lastDrawHandledMouse = false;
		for(ColourPickerButton button : colourPickerButtons)
		{
			lastDrawHandledMouse = button.draw(scaleLevel);
			if(lastDrawHandledMouse == true)
			{
				hasHandledMouse = true;
			}
		}
		return hasHandledMouse;
	}
	
	public FontColour getSelectedColour()
	{
		return this.currentSelectedColour;
	}
	
	public void notifySelected(int fontColourID) {
		this.currentSelectedColour = new FontColour(fontColourID);
		ColourPickerButton button;
		for(int i = 0; i < this.colourPickerButtons.size(); i++)
		{
			button = this.colourPickerButtons.get(i);
			if(button.getFontColourID() != fontColourID)
			{
				button.setSelected(false);
			}
		}
	}
}
