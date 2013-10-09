package common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import common.puzzle.PuzzleField;
import common.puzzle.PuzzleSaver;

import client.gl.Texture;
import static org.lwjgl.opengl.GL11.*;

public class ManualSaver extends PuzzleSaver implements ActionListener {

	private static ManualSaver instance;
	private static double alpha = 0;
	private static Texture message;
	private static Timer timer;
	private static boolean saveCooldownExpired = true;

	public ManualSaver(PuzzleField[][] table, String saveTarget) {
		super(table, saveTarget);
		this.instance = this;
		message = new Texture("res/saved.png");
		timer = new Timer(3000, instance);
	}
	
	public static void doManualSave() {
		if(saveCooldownExpired != true) return;
		saveCooldownExpired = false;
		timer.restart();
		timer.start();
		instance.doSave();
		alpha = 1;
		System.out.println("saved..");
	}
	
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("resetting..");
		timer.stop();
		saveCooldownExpired = true;
	}
	
	public static void draw() {
		if(alpha <= 0) return;
		if(saveCooldownExpired == true) return;
		glColor4d(1, 1, 1, alpha);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, message.texRef);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(5, 50);
		glTexCoord2f(1, 0);
		glVertex2f(145, 50);
		glTexCoord2f(1, 1);
		glVertex2f(145, 120);
		glTexCoord2f(0, 1);
		glVertex2f(5, 120);
		glEnd();
		glDisable(GL_TEXTURE_2D);
		alpha -= 0.01;
		glColor4d(1, 1, 1, 1);
	}
}
