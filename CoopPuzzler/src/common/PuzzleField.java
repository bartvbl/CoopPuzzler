package common;

import java.util.concurrent.atomic.AtomicInteger;

public class PuzzleField {
	public final boolean isFilled;
	public final int questionReference;
	private AtomicInteger currentValueOfField = new AtomicInteger();
	
	public PuzzleField(boolean isFilled, int questionReference)
	{
		this.isFilled = isFilled;
		this.questionReference = questionReference;
		this.setNewCharacterValue(' ');
	}
	
	public void setNewCharacterValue(char newCharacter)
	{
		this.currentValueOfField.set((int)newCharacter);
	}
	
	public char getCurrentValueOfField()
	{
		return (char)this.currentValueOfField.get();
	}
}
