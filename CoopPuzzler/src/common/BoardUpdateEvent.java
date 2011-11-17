package common;

public class BoardUpdateEvent {
	private int row,column;
	private char value;
	private FontColour colour;
	
	public BoardUpdateEvent(String description){
		if(description==null){return;}
		String[] split = description.split(" ");
		try{
			row = Integer.parseInt(split[1]);
			column = Integer.parseInt(split[2]);
			value = split[3].charAt(0);
			colour = new FontColour(Integer.parseInt(split[4]));
		}catch(Exception e){e.printStackTrace();}
	}
	
	public BoardUpdateEvent(int row, int column, char value, FontColour colour)
	{
		this.row = row;
		this.column = column;
		this.value = value;
		this.colour = colour;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public char getCharacterValue() {
		return value;
	}

	public FontColour getColour() {
		return colour;
	}
	
	public String toString(){
		return ProtocolConstants.BOARD_UPDATE + " " + row + " " + column + " " + value + " " + colour.toString()+"\n";
	}

}
