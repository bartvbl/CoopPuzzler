package common;

import java.util.concurrent.atomic.AtomicInteger;

public class PuzzleField {
	public final boolean isFilled;
	public final int questionReference;
	private AtomicInteger currentValueOfField;
	
	public PuzzleField(boolean isFilled, int questionReference)
	{
		this.isFilled = isFilled;
		this.questionReference = questionReference;
	}
	
	public void setNewCharacterValue(char newCharacter)
	{
		this.currentValueOfField.set(newCharacter);
	}
	
	public char getCurrentValueOfField()
	{
		return (char)this.currentValueOfField.get();
	}
}
