package client;

import java.awt.Font;
import java.util.HashMap;

import org.lwjgl.util.Color;

import common.FontColour;

import client.gl.GLFont;
import client.gl.Texture;

public class TextureLibrary {
	public static final char IJ = '+';
	
	private HashMap<FontColour, HashMap<Character, Texture>> textFontTextures;
	private HashMap<Integer, Texture> referenceTextures;
	private GLFont referenceFontGenerator;
	
	private static final String TEXT_FONT_FACE = "Arial";
	private static final int TEXT_FONT_STYLE = Font.PLAIN;
	private static final int TEXT_FONT_SIZE = 10;
	
	private static final String REFERENCE_FONT_FACE = "Arial";
	private static final int REFERENCE_FONT_STYLE = Font.PLAIN;
	private static final int REFERENCE_FONT_SIZE = 10;
	
	private static final float[] DEFAULT_BACKGROUND_COLOUR = new float[]{1.0f, 1.0f, 1.0f, 0.0f};
	private static final float[] REFERENCE_FONT_COLOUR = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
	
	public TextureLibrary()
	{
		this.textFontTextures = new HashMap<FontColour, HashMap<Character, Texture>>();
		this.referenceTextures = new HashMap<Integer, Texture>();
		this.initializeReferenceFontGenerator();
	}
	
	public void addFontColour(FontColour fontColour)
	{
		Color colour = fontColour.getColour();
		HashMap<Character, Texture> textureMap = new HashMap<Character, Texture>();
		this.generateFontTextures(textureMap, colour);
		this.textFontTextures.put(fontColour, textureMap);
	}
	
	public Texture getTextTexture(FontColour colour, char character)
	{
		return this.textFontTextures.get(colour).get(character);
	}
	
	public void addReferenceTexture(int questionReference) {
		Texture texture = this.referenceFontGenerator.createFontTexture(Integer.toString(questionReference));
		this.referenceTextures.put(questionReference, texture);
	}
	
	public Texture getReferenceTexture(int questionReference)
	{
		return this.referenceTextures.get(questionReference);
	}
	
	private void generateFontTextures(HashMap<Character, Texture> textureMap, Color colour)
	{
		Font textFont = new Font(TEXT_FONT_FACE, TEXT_FONT_STYLE, TEXT_FONT_SIZE);
		GLFont fontGenerator = this.createFontGenerator(textFont, colour);
		char generatedCharacter;
		Texture texture;
		for(int i = 97; i < 123; i++)
		{
			generatedCharacter = (char)i;
			texture = fontGenerator.createFontTexture(Character.toString(generatedCharacter));
			textureMap.put(generatedCharacter, texture);
		}
	}
	
	private GLFont createFontGenerator(Font font, Color colour)
	{
		float red = ((float)colour.getRed())/255f;
		float green = ((float)colour.getGreen())/255f;
		float blue = ((float)colour.getBlue())/255f;
		GLFont fontGenerator = new GLFont(font, new float[]{red, green, blue, 1.0f}, DEFAULT_BACKGROUND_COLOUR);
		return fontGenerator;
	}
	
	private void initializeReferenceFontGenerator()
	{
		Font referenceFont = new Font(REFERENCE_FONT_FACE, REFERENCE_FONT_STYLE, REFERENCE_FONT_SIZE);
		this.referenceFontGenerator = new GLFont(referenceFont, REFERENCE_FONT_COLOUR, DEFAULT_BACKGROUND_COLOUR);
	}

}
