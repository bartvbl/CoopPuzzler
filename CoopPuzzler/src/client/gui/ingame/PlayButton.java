package client.gui.ingame;

import client.ClientMain;
import client.gl.Texture;
import static org.lwjgl.opengl.GL11.*;

public class PlayButton extends Button {
	private static final int x = 10;
	private static final int y = 10;
	private static final int width = 100;
	private static final int height = 40;
	
	private final Texture upTexture;
	private final Texture hoverTexture;
	private final Texture downTexture;
	private final ClientMain main;
	
	public PlayButton(ClientMain clientMain) {
		super(x, y, width, height);
		this.main = clientMain;
		this.upTexture = new Texture("res/playButtonUp.png");
		this.hoverTexture = new Texture("res/playButton_hover.png");
		this.downTexture = new Texture("res/playButton_down.png");
	}

	protected void drawMouseUp() {
		drawButton(upTexture);
	}

	protected void drawMouseDown() {
		drawButton(downTexture);
	}

	protected void drawMouseOver() {
		drawButton(hoverTexture);
	}

	private void drawButton(Texture texture) {
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, texture.texRef);
		glColor4d(1, 1, 1, 1);
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex2d(x, y);
		glTexCoord2d(1, 0);
		glVertex2d(x + width, y);
		glTexCoord2d(1, 1);
		glVertex2d(x + width, y + height);
		glTexCoord2d(0, 1);
		glVertex2d(x, y + height);
		glEnd();
		glDisable(GL_TEXTURE_2D);
	}
	
	protected void onClick() {
		main.exitEditorMode();
	}
}
