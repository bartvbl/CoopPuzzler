package editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import editor.EditorMain;

@SuppressWarnings("serial")
public class MainMenuPanel extends JPanel {
	private JPanel mainContentPanel;
	private EditorMain main;
	
	public MainMenuPanel(EditorMain main)
	{
		this.main = main;
		this.mainContentPanel = new MainMenuView();
		this.add(this.mainContentPanel);
		this.validate();
	}
}
