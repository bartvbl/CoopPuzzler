package client;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class ClientWindow {
	private float windowWidth, windowHeight;
	private boolean running = true;
	public JFrame jframe;
	public Canvas canvas;
	private AtomicReference<Dimension> canvasSize = new AtomicReference<Dimension>();
	private ClientMain main;
	
	public ClientWindow(ClientMain main)
	{
		this.main = main;
		Canvas canvas = new Canvas();
		JFrame frame = new JFrame("Puzzler");
		this.canvas = canvas;
		this.jframe = frame;
		ComponentAdapter adapter = new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				resize();
			}
		};
		
		canvas.addComponentListener(adapter);
		canvas.setIgnoreRepaint(true);
		frame.setSize(640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(canvas);
		frame.setVisible(true);
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
		glOrtho(0, 640, 0, 480, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glClearColor(94.0f/255.0f, 161.0f/255.0f, 255.0f/255.0f, 0.5f);
		glClearDepth(1.0);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
	}
	
	public void mainLoop()
	{
		while (!Display.isCloseRequested() && running) {
			Dimension newDim = canvasSize.getAndSet(null);
			if(newDim != null) {
				glOrtho(0, newDim.width, 0, newDim.height, 1, -1);
				try {
					DisplayMode mode = new DisplayMode(newDim.width, newDim.height);
					Display.setDisplayMode(mode);
					this.windowHeight = newDim.height;
					this.windowWidth = newDim.width;
					mode = null;
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}
			main.doFrame();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			glEnable(GL_DEPTH_TEST);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			Display.update();
			// no need to run at full speed
			try {
				Thread.sleep(1000/60);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void resize()
	{
		Dimension dim = this.canvas.getSize();
		canvasSize.set(dim);
		dim = null;
	}
}
