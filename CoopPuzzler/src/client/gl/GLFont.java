package client.gl;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class GLFont
{
	private int fontSize;
	@SuppressWarnings("unused")
	private Graphics2D graphic;
	private Font font;
	private static final float QUALITY = 7.0f;
	
	public GLFont(Font font) 
	{
		this.fontSize = font.getSize();
		this.font = font.deriveFont((float)(font.getSize()*QUALITY));
	}
	public void setFont(Font font)
	{
		this.font = font.deriveFont((float)(font.getSize()*QUALITY));
	}
	public void setSize(int size)
	{
		this.fontSize = size;
	}
	public Texture createFontTexture(String msg) {
		boolean isAntiAliased = true;
		boolean usesFractionalMetrics = false;
		
		// get size of texture image neaded to hold 10x10 character grid
		int textureSize = (int)(QUALITY*30);
		// create a buffered image to hold charset
		BufferedImage image = new BufferedImage(textureSize, (int)(2.6*QUALITY*fontSize), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		
		// Clear image with background color (make transparent if color has alpha value)
		//if (bg.getAlpha() < 255) {
		//	g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, (float)bg.getAlpha()/255f));
		//}
		//g.setColor(bg);
		//g.fillRect(0,0,textureSize,4*textureSize);
		
		
		// prepare to draw characters in foreground color
		//g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g.setColor(Color.white);
		g.setFont(font);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, isAntiAliased? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, usesFractionalMetrics? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		// get font measurements
		g.drawString(msg, 0, fontSize + QUALITY*QUALITY);
		
		Texture tex = new Texture();
		tex.setImage(image);

		// draw the grid of 100 characters
		graphic = g;
		return tex;
	}
}
