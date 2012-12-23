package editor.common;

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
