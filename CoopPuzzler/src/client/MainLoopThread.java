package client;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

import java.awt.Dimension;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class MainLoopThread implements Runnable{
	private ClientMain main;

	public MainLoopThread(ClientMain main)
	{
		this.main = main;
	}

	@Override
	public void run() {
		main.initialize();
		
		this.mainLoop();
	}
	
	public void mainLoop()
	{
		while(!Display.isCloseRequested())
		{
			Dimension windowSize = this.main.window.getSize();
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			float windowWidth, windowHeight;
			if((windowSize.width == 0) || (windowSize.height == 0))
			{
				windowWidth = 100f;
				windowHeight = 100f;
			} else {
				windowWidth = windowSize.width;
				windowHeight = windowSize.height;
			}
			float aspectRatio = windowWidth/windowHeight;
			glViewport(0, 0, (int)windowWidth, (int)windowHeight);
			gluOrtho2D(-1 * aspectRatio, 1*aspectRatio, -1, 1);
			
			main.doFrame();
			
			glOrtho(0.0f, windowWidth, 0.0f, windowHeight, -1.0f, 1.0f);
			
			main.handleUI();
			Display.update();
			Display.sync(50);
		}
	}
}
