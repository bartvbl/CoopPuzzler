package common;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PuzzleField {
	public final boolean isFilled;
	public final int questionReference;
	private AtomicReference<FontColour> fieldTextColour;
	private AtomicInteger currentValueOfField = new AtomicInteger();

	public PuzzleField(boolean isFilled, int questionReference)
	{
		this.isFilled = isFilled;
		this.questionReference = questionReference;
		this.setNewCharacterValue(' ');
		this.fieldTextColour = new AtomicReference<FontColour>();
		this.fieldTextColour.set(new FontColour(FontColour.BLACK));
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


}
