package client.gl;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX;
import static org.lwjgl.opengl.GL11.GL_PROJECTION_MATRIX;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glLoadMatrix;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.util.glu.GLU.gluProject;
import static org.lwjgl.util.glu.GLU.gluUnProject;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class CoordConverter {
	private FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
	private FloatBuffer projection = BufferUtils.createFloatBuffer(16);
	private IntBuffer viewport = BufferUtils.createIntBuffer(16);
	private FloatBuffer location = BufferUtils.createFloatBuffer(3);
	private FloatBuffer winZ = BufferUtils.createFloatBuffer(1);
	
	public float[] getScreenCoords(float x, float y)
	{
		float z = 0;
		modelView.clear().rewind();
		projection.clear().rewind();
		viewport.clear().rewind();
		location.clear().rewind();
		
		glGetFloat(GL_MODELVIEW_MATRIX, modelView);
		glGetFloat(GL_PROJECTION_MATRIX, projection);
		glGetInteger(GL_VIEWPORT, viewport);
		
		gluProject(x, y, z, modelView, projection, viewport, location);
		return new float[] {location.get(0), location.get(1), location.get(2)};
	}
	public float[] getMapCoords(int x, int y, float f)
	{
		modelView.clear().rewind();
		projection.clear().rewind();
		viewport.clear().rewind();
		location.clear().rewind();
		winZ.clear().rewind();
		
		glGetFloat(GL_MODELVIEW_MATRIX, modelView);
		glGetFloat(GL_PROJECTION_MATRIX, projection);
		glGetInteger(GL_VIEWPORT, viewport);
		
		glReadPixels(x, y, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, winZ);
		System.out.println("depth component: " + winZ.get(0));
		gluUnProject(x, y, f, modelView, projection, viewport, location);
		return new float[] {location.get(0), location.get(1)};
	}
	public void setBillboard()
	{
		glGetFloat(GL_MODELVIEW_MATRIX, modelView); 
		modelView.put(0, 1);
		modelView.put(1, 0);
		modelView.put(2, 0);
		
		modelView.put(4, 0);
		modelView.put(5, 1);
		modelView.put(6, 0);
		
		modelView.put(8, 0);
		modelView.put(9, 0);
		modelView.put(10, 1);
		
		glLoadMatrix(modelView);
	}
}
