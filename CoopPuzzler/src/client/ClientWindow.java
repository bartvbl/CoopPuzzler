package client;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import client.gui.EditorMainMenuPanel;
import client.gui.EditorMainMenuView;
import client.gui.EditorMainSwitcher;
import client.gui.MainMenuPanel;
import client.gui.MainMenuView;
import client.puzzleFileList.PuzzleListPopulator;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class ClientWindow {
	public float windowWidth = 640;
	public float windowHeight = 480;
	public JFrame jframe;
	public Canvas canvas;
	private AtomicReference<Dimension> canvasSize = new AtomicReference<Dimension>();
	private ClientMain main;
	private MainMenuPanel mainMenuPanel;
	private EditorMainMenuPanel editorMainPanel;
	private Dimension dimension;
	
	public ClientWindow(ClientMain main)
	{
		this.main = main;
		JFrame frame = new JFrame("Puzzler");
		this.jframe = frame;
		this.mainMenuPanel = new MainMenuPanel(this.main);
		this.editorMainPanel = new EditorMainMenuPanel(this, this.main);
		new EditorMainSwitcher(this.jframe, mainMenuPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(100, 100);
		this.jframe.setIconImage(Toolkit.getDefaultToolkit().getImage("res/icon_32.png"));
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
		jframe.addComponentListener(adapter);
		canvas.setIgnoreRepaint(true);
	}
	
	public void enableMainMenu()
	{
		PuzzleListPopulator.populatePuzzleList(MainMenuView.puzzleList);
		this.jframe.setSize(530, 301);
		this.jframe.setResizable(false);
		this.jframe.setVisible(true);
		this.jframe.add(mainMenuPanel);
		this.jframe.validate();
		this.jframe.repaint();
	}
	
	public void disableMainMenu()
	{
		this.jframe.remove(this.mainMenuPanel);
		this.jframe.remove(EditorMainMenuView.getInstance());
		this.jframe.setSize(640, 480);
		this.jframe.setResizable(true);
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
			Display.setFullscreen(true);
		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}
		glViewport(0, 0, dim.width, dim.height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluOrtho2D(0f, 640f, 0f, 480f);
		glMatrixMode(GL_MODELVIEW);
		glClearColor(94.0f/255.0f, 161.0f/255.0f, 255.0f/255.0f, 0.5f);
		glEnable (GL_BLEND);
		glDepthFunc(GL_NEVER);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public Dimension getSize() {
		Dimension newDim = canvasSize.getAndSet(null);
		if(newDim != null) {
			try {
				DisplayMode mode = new DisplayMode(newDim.width, newDim.width);
				Display.setDisplayMode(mode);
				this.dimension = newDim;
				mode = null;
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		}
		return this.dimension;
	}
	
	public void resize()
	{
		Dimension dim = this.canvas.getSize();
		this.windowWidth = dim.width;
		this.windowHeight = dim.height;
		canvasSize.set(dim);
		dim = null;
	}
}
