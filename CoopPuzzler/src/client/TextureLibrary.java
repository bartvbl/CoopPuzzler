package client;

import java.awt.Font;
import java.util.HashMap;

import client.gl.GLFont;
import client.gl.Texture;

public class TextureLibrary {
	public static final char IJ = '+';
	
	private HashMap<Character, Texture> defaultTextureMaps;
	private HashMap<Character, Texture> unsureMarkedTextureMaps;
	private HashMap<Integer, Texture> referenceTextures;
	private GLFont defaultFontGenerator;
	private GLFont unsureMarkFontGenerator;
	private GLFont referenceFontGenerator;
	
	public TextureLibrary()
	{
		this.defaultTextureMaps = new HashMap<Character, Texture>();
		this.unsureMarkedTextureMaps = new HashMap<Character, Texture>();
		this.referenceTextures = new HashMap<Integer, Texture>();
		this.initializeFontGenerators();
		char generatedCharacter;
		for(int i = 97; i < 123; i++)
		{
			generatedCharacter = (char)i;
			this.addFontTextureToHashMaps(generatedCharacter);
		}
		Texture texture = this.defaultFontGenerator.createFontTexture("ij");
		this.defaultTextureMaps.put(Character.valueOf(IJ), texture);
		texture = this.unsureMarkFontGenerator.createFontTexture("ij");
		this.unsureMarkedTextureMaps.put(Character.valueOf(IJ), texture);
	}
	
	private void initializeFontGenerators()
	{
		Font defaultFont = new Font("Arial", Font.PLAIN, 12);
		Font unsureMarkFont = new Font("Arial", Font.ITALIC, 12);
		Font referenceFont = new Font("Arial", Font.PLAIN, 10);
		this.defaultFontGenerator = new GLFont(defaultFont, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, new float[]{1.0f, 1.0f, 1.0f, 0.0f});
		this.unsureMarkFontGenerator = new GLFont(unsureMarkFont, new float[]{1.0f, 0.398f, 0.0f, 1.0f}, new float[]{1.0f, 1.0f, 1.0f, 0.0f});
		this.referenceFontGenerator = new GLFont(referenceFont, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, new float[]{1.0f, 1.0f, 1.0f, 0.0f});
	}
	
	private void addFontTextureToHashMaps(Character generatedCharacter)
	{
		Texture texture = this.defaultFontGenerator.createFontTexture(Character.toString(generatedCharacter));
		this.defaultTextureMaps.put(Character.valueOf(generatedCharacter), texture);
		texture = this.unsureMarkFontGenerator.createFontTexture(Character.toString(generatedCharacter));
		this.unsureMarkedTextureMaps.put(Character.valueOf(generatedCharacter), texture);
	}
	
	public Texture getDefaultTextureByCharacter(char character)
	{
		return this.defaultTextureMaps.get(character);
	}

	public void addReferenceTexture(int questionReference) {
		Texture texture = this.referenceFontGenerator.createFontTexture(Integer.toString(questionReference));
		this.referenceTextures.put(questionReference, texture);
	}
	
	public Texture getReferenceTexture(int questionReference)
	{
		return this.referenceTextures.get(questionReference);
	}
}
