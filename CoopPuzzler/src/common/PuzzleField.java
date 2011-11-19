package common;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PuzzleField {
	public final boolean isFilled;
	public final int questionReference;
	private AtomicReference<FontColour> fieldTextColour;
	private AtomicInteger currentValueOfField;

	public PuzzleField(boolean isFilled, boolean ignoreQuestionreference, int questionReference)
	{
		this.initialize();
		this.isFilled = isFilled;
		this.questionReference = questionReference;
		this.fieldTextColour.set(new FontColour(FontColour.BLACK));
	}
	
	public PuzzleField(String messageString)
	{
		this.initialize();
		int contentOfMessageStartIndex = messageString.indexOf('(');
		String messageContent = messageString.substring(contentOfMessageStartIndex + 1, messageString.length() - 1);
		
		String[] messageParts = messageContent.split("/");
		this.isFilled = Boolean.parseBoolean(messageParts[0]);
		this.questionReference = Integer.parseInt(messageParts[1]);
		this.fieldTextColour.set(new FontColour(Integer.parseInt(messageParts[2])));
		this.currentValueOfField.set(messageParts[3].charAt(0));
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
		Character.toString((char)this.currentValueOfField.get()) + ")";
		
	}
}
