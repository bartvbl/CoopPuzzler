package editor.common;

import org.lwjgl.util.Color;
public class FontColour {
	private int colourIndex;
	
	public final static int BLACK = 0;
	public final static int DARK_BLUE = 1;
	public final static int DARK_RED = 2;
	public final static int DARK_GREEN = 3;
	public final static Color[] colours = new Color[]{
		new Color(0,0,0),
		new Color(0,0,170),
		new Color(170,0,0),
		new Color(0,170,0)
	};

	public FontColour(int fontcolourIndex){
		if(fontcolourIndex < 0 || fontcolourIndex >= colours.length){
			this.colourIndex = 0;
			return;
		}
		colourIndex = fontcolourIndex;
	}

	public Color getColour() {
		return colours[colourIndex];
	}
	
	public int getColourIndex()
	{
		return this.colourIndex;
	}
	
	public String toString(){
		return Integer.toString(this.colourIndex);
	}
}



