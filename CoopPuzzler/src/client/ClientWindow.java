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
import static org.lwjgl.util.glu.GLU.*;

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
		gluOrtho2D(0f, 640f, 0f, 480f);
		glMatrixMode(GL_MODELVIEW);
		glClearColor(94.0f/255.0f, 161.0f/255.0f, 255.0f/255.0f, 0.5f);
		glClearDepth(1.0);
		glEnable (GL_BLEND);
		glDepthFunc(GL_NEVER);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void mainLoop()
	{
		while (!Display.isCloseRequested() && running) {
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			Dimension newDim = canvasSize.getAndSet(null);
			if(newDim != null) {
				gluOrtho2D(0f, 640f, 0f, 480f);
				glViewport(0, 0, newDim.width, newDim.height);
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
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			
			
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			main.doFrame();
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
