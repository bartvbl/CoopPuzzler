package common;

import org.lwjgl.util.Color;

public class BoardUpdateEvent {
	private int x,y;
	private char value;
	private Color color;
	
	public BoardUpdateEvent(String description){
		if(description==null){return;}
		String[] split = description.split(" ");
		try{
			x = Integer.parseInt(split[0]);
			y = Integer.parseInt(split[1]);
			value = split[2].charAt(0);
			color = new Color(Integer.parseInt(split[3]),Integer.parseInt(split[4]),Integer.parseInt(split[5]));
		}catch(Exception e){e.printStackTrace();}
		
		
	}
	public BoardUpdateEvent(int x, int y, char value, Color colour)
	{
		this.x = x;
		this.y = y;
		this.value = value;
		this.color = colour;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public char getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}

}
