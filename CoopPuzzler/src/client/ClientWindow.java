package client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import client.gui.MainMenuPanel;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class ClientWindow {
	public float windowWidth = 640;
	public float windowHeight = 480;
	private boolean running = true;
	public JFrame jframe;
	public Canvas canvas;
	private AtomicReference<Dimension> canvasSize = new AtomicReference<Dimension>();
	private ClientMain main;
	private MainMenuPanel mainMenuPanel;
	
	public ClientWindow(ClientMain main)
	{
		this.main = main;
		JFrame frame = new JFrame("Puzzler");
		this.jframe = frame;
		this.mainMenuPanel = new MainMenuPanel(this.main);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(100, 100);
	}
	
	private void createCanvas()
	{
		Canvas canvas = new Canvas();
		this.canvas = canvas;
		ComponentAdapter adapter = new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				resize();
			}
		};
		canvas.addComponentListener(adapter);
		canvas.setIgnoreRepaint(true);
	}
	
	public void enableMainMenu()
	{
		this.jframe.setSize(500, 170);
		
		this.jframe.setVisible(true);
		this.jframe.add(mainMenuPanel);
//		this.disableMainMenu();
//		this.main.runGame(false, "");
	}
	
	public void disableMainMenu()
	{
		
		this.jframe.remove(this.mainMenuPanel);
		this.jframe.setSize(640, 480);
		this.createCanvas();
		this.resize();
		this.jframe.add(canvas);
		
	}
	
	public void createOpenGLContext()
	{
		Dimension dim = this.canvas.getSize();
		Display.setLocation(100, 100);
		Display.setTitle("Puzzler");
		try {
			Display.setDisplayMode(new DisplayMode(dim.width, dim.height));
			Display.setParent(canvas);
			Display.create();
		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}
		glViewport(0, 0, dim.width, dim.height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluOrtho2D(0f, 640f, 0f, 480f);
		glMatrixMode(GL_MODELVIEW);
		glClearColor(94.0f/255.0f, 161.0f/255.0f, 255.0f/255.0f, 0.5f);
		//glClearDepth(1.0);
		glEnable (GL_BLEND);
		glDepthFunc(GL_NEVER);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void mainLoop()
	{
		while (!Display.isCloseRequested() && running) {
			Dimension newDim = canvasSize.getAndSet(null);
			if(newDim != null) {
				
				try {
					DisplayMode mode = new DisplayMode(newDim.width, newDim.width);
					Display.setDisplayMode(mode);
					this.windowHeight = newDim.height;
					this.windowWidth = newDim.width;
					mode = null;
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			
			float aspectRatio = this.windowWidth/this.windowHeight;
			glViewport(0, 0, (int)this.windowWidth, (int)this.windowHeight);
			gluOrtho2D(-1 * aspectRatio, 1*aspectRatio, -1, 1);
			
			main.doFrame();
			Display.update();
			Display.sync(50);
		}
	}
	
	public void resize()
	{
		System.out.println("resizing");
		Dimension dim = this.canvas.getSize();
		this.windowWidth = dim.width;
		this.windowHeight = dim.height;
		canvasSize.set(dim);
		dim = null;
	}
}
