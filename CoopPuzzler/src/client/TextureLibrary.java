package client;

import java.awt.Font;
import java.util.HashMap;

import client.gl.GLFont;
import client.gl.Texture;

public class TextureLibrary {
	private HashMap<Character, Texture> defaultColouredTextureMaps;
	private HashMap<Character, Texture> unsureMarkedTextureMaps;
	private GLFont defaultFontGenerator;
	
	public TextureLibrary()
	{
		this.defaultColouredTextureMaps = new HashMap<Character, Texture>();
		Font defaultFont = new Font("Arial", Font.PLAIN, 12);
		this.defaultFontGenerator = new GLFont(defaultFont, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, new float[]{0.0f, 0.0f, 0.0f, 0.0f});
		char generatedCharacter;
		for(int i = 97; i < 123; i++)
		{
			generatedCharacter = (char)i;
			Texture texture = this.defaultFontGenerator.createFontTexture(Character.toString(generatedCharacter));
			this.unsureMarkedTextureMaps.put(Character.valueOf(generatedCharacter), texture);
		}
	}
	
	//public Texture getTextureByCharacter(char )
}
