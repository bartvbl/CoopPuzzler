package common;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PuzzleField {
	public boolean isFilled;
	public final int questionReference;
	public boolean hasIgnoreReference;
	private AtomicReference<FontColour> fieldTextColour;
	private AtomicInteger currentValueOfField;

	public PuzzleField(boolean isFilled, int questionReference, boolean hasIgnoreReference)
	{
		this.initialize();
		this.isFilled = isFilled;
		this.questionReference = questionReference;
		this.fieldTextColour.set(new FontColour(FontColour.BLACK));
		this.hasIgnoreReference = hasIgnoreReference;
	}
	
	public PuzzleField(String messageString)
	{
		this.initialize();
		int contentOfMessageStartIndex = messageString.indexOf('(');
		String messageContent = messageString.substring(contentOfMessageStartIndex + 1, messageString.length() - 1);
		
		String[] messageParts = messageContent.split(ProtocolConstants.BOARD_UPDATE_SEPARATOR);
		this.isFilled = Boolean.parseBoolean(messageParts[0]);
		this.questionReference = Integer.parseInt(messageParts[1]);
		this.fieldTextColour.set(new FontColour(Integer.parseInt(messageParts[2])));
		this.currentValueOfField.set(messageParts[3].charAt(0));
		this.hasIgnoreReference = Boolean.parseBoolean(messageParts[4]);
	}
	
	private void initialize(){
		this.fieldTextColour = new AtomicReference<FontColour>();
		this.currentValueOfField = new AtomicInteger();
		this.setNewCharacterValue(' ');
	}

	public FontColour getFieldTextColour() {
		return fieldTextColour.get();
	}

	public void setFieldTextColour(FontColour fieldTextColour) {
		this.fieldTextColour.set(fieldTextColour);
	}

	public void setNewCharacterValue(char newCharacter)
	{
		this.currentValueOfField.set((int)newCharacter);
	}

	public char getCurrentValueOfField()
	{
		return (char)this.currentValueOfField.get();
	}
	
	public String toString()
	{
		System.out.println("");
		return "(" + Boolean.toString(this.isFilled) + "/" + 
		Integer.toString(this.questionReference) + "/" + 
		fieldTextColour.toString() + "/" + 
		Character.toString((char)this.currentValueOfField.get()) + "/" + 
		Boolean.toString(this.hasIgnoreReference) + ")";
	}
}
