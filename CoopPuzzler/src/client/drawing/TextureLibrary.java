package client.drawing;

import java.awt.Font;
import java.util.HashMap;

import org.lwjgl.util.Color;

import common.FontColour;

import client.gl.GLFont;
import client.gl.Texture;

public class TextureLibrary {
	public static final char IJ = '+';
	
	private HashMap<Character, Texture> textFontTextures;
	
	private static final String TEXT_FONT_FACE = "Arial";
	private static final int TEXT_FONT_STYLE = Font.PLAIN;
	private static final int TEXT_FONT_SIZE = 10;
	public TextureLibrary()
	{
		this.textFontTextures = new HashMap<Character, Texture>();
		this.generateTextFontTextures();
	}
	
	public Texture getTextTexture(char character)
	{
		return this.textFontTextures.get(character);
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
}
