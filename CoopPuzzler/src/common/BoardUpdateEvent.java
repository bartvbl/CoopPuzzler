package common;

import org.lwjgl.util.Color;

public class BoardUpdateEvent {
	private int x,y;
	private char value;
	private FontColour colour;
	
	public BoardUpdateEvent(String description){
		if(description==null){return;}
		String[] split = description.split(" ");
		try{
			x = Integer.parseInt(split[1]);
			y = Integer.parseInt(split[2]);
			value = split[3].charAt(0);
			colour = new FontColour(Integer.parseInt(split[4]));
		}catch(Exception e){e.printStackTrace();}
		
		
	}
	
	public BoardUpdateEvent(int x, int y, char value, FontColour colour)
	{
		this.x = x;
		this.y = y;
		this.value = value;
		this.colour = colour;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public char getCharacterValue() {
		return value;
	}

	public FontColour getColour() {
		return colour;
	}
	
	public String toString(){
		return ProtocolConstants.BOARD_UPDATE + " " + x + " " + y + " " + value + " " + colour.toString();
	}

}
