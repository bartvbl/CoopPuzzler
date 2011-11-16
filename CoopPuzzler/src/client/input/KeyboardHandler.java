package client.input;

import org.lwjgl.input.Keyboard;

public class KeyboardHandler {
	private static final float MOVE_SPEED = 0.3f;
	
	public static void handleMapMovementXAxis(float boardX, float boardY){
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			boardY += MOVE_SPEED;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			boardY -= MOVE_SPEED;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			boardX += MOVE_SPEED;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			boardX -= MOVE_SPEED;
		}
		float aspect = window.windowWidth / window.windowHeight;
		float bound = 1 / zoomLevel;
		if (boardX < -1 * bound * aspect - mapNumColumns / 2) {
			boardX = -1 * bound * aspect - mapNumColumns / 2;
		}
		if (boardX > bound * aspect - mapNumColumns / 2) {
			boardX = bound * aspect - mapNumColumns / 2;
		}
		
	}
	public static void handleMapMovementYAxis(float boardY){
		if (boardY < -1 * bound - mapNumRows / 2) {
			float aspect = window.windowWidth / window.windowHeight;
			float bound = 1 / zoomLevel;
			boardY = -1 * bound - mapNumRows / 2;
		}
		if (boardY > bound - mapNumRows / 2) {
			boardY = bound - mapNumRows / 2;
		}
	}
}
