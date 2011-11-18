package client;

import java.awt.Font;
import java.util.HashMap;

import org.lwjgl.util.Color;

import common.FontColour;

import client.gl.GLFont;
import client.gl.Texture;

public class TextureLibrary {
	public static final char IJ = '+';
	
	private HashMap<Character, Texture> textFontTextures;
	private HashMap<Integer, Texture> referenceTextures;
	private GLFont referenceFontGenerator;
	
	private static final String TEXT_FONT_FACE = "Arial";
	private static final int TEXT_FONT_STYLE = Font.PLAIN;
	private static final int TEXT_FONT_SIZE = 10;
	
	private static final String REFERENCE_FONT_FACE = "Arial";
	private static final int REFERENCE_FONT_STYLE = Font.PLAIN;
	private static final int REFERENCE_FONT_SIZE = 10;
	
	public TextureLibrary()
	{
		this.textFontTextures = new HashMap<Character, Texture>();
		this.referenceTextures = new HashMap<Integer, Texture>();
		this.initializeReferenceFontGenerator();
		this.generateTextFontTextures();
	}
	
	public Texture getTextTexture(char character)
	{
		return this.textFontTextures.get(character);
	}
	
	public void addReferenceTexture(int questionReference) {
		Texture texture = this.referenceFontGenerator.createFontTexture(Integer.toString(questionReference));
		this.referenceTextures.put(questionReference, texture);
	}
	
	public Texture getReferenceTexture(int questionReference)
	{
		return this.referenceTextures.get(questionReference);
	}
	
	private void generateTextFontTextures()
	{
		Font textFont = new Font(TEXT_FONT_FACE, TEXT_FONT_STYLE, TEXT_FONT_SIZE);
		GLFont fontGenerator = this.createFontGenerator(textFont);
		char generatedCharacter;
		Texture texture;
		for(int i = 97; i < 123; i++)
		{
			generatedCharacter = (char)i;
			texture = fontGenerator.createFontTexture(Character.toString(generatedCharacter));
			this.textFontTextures.put(generatedCharacter, texture);
		}
		texture = fontGenerator.createFontTexture("ij");
		this.textFontTextures.put(IJ, texture);
	}
	
	private GLFont createFontGenerator(Font font)
	{
		GLFont fontGenerator = new GLFont(font);
		return fontGenerator;
	}
	
	private void initializeReferenceFontGenerator()
	{
		Font referenceFont = new Font(REFERENCE_FONT_FACE, REFERENCE_FONT_STYLE, REFERENCE_FONT_SIZE);
		this.referenceFontGenerator = new GLFont(referenceFont);
	}

}
