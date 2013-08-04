package client.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextPane;

public class TabSurpressor implements KeyListener {

	private final JTextPane target;

	public TabSurpressor(JTextPane target) {
		this.target = target;
	}

	public void keyPressed(KeyEvent event) {
		if(event.getKeyChar() == '\t') {
			event.consume();
			target.requestFocus();
			target.select(0, target.getText().length());
		}
	}

	public void keyReleased(KeyEvent event) {
		
	}

	public void keyTyped(KeyEvent arg0) {
		
	}

}
