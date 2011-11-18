package client.gui;

import java.util.ArrayList;

import common.FontColour;

public class ColourPickerUI {
	private ArrayList<ColourPickerButton> colourPickerButtons;
	private FontColour currentSelectedColour;
	public ColourPickerUI()
	{
		this.colourPickerButtons = new ArrayList<ColourPickerButton>();
		for(int i = 0; i < FontColour.colours.length; i++)
		{
			this.colourPickerButtons.add(new ColourPickerButton(this, i, 43*i + 3, 3, 40, 40));
		}
		this.colourPickerButtons.get(0).setSelected(true);
		this.currentSelectedColour = new FontColour(0);
	}
	public boolean draw()
	{
		boolean hasHandledMouse = false;
		for(ColourPickerButton button : colourPickerButtons)
		{
			if(button.draw() == true)
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
