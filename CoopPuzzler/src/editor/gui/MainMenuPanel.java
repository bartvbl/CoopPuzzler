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
public class MainMenuPanel extends JPanel implements ActionListener{
	private JPanel mainContentPanel;
	private EditorMain main;
	
	public MainMenuPanel(EditorMain main)
	{
		this.main = main;
		this.mainContentPanel = new MainMenuView();
		this.add(this.mainContentPanel);
		this.validate();
		MainMenuView.createNewButton.addActionListener(this);
		MainMenuView.editCurrentButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == MainMenuView.createNewButton) {
			this.runAsCreateNewPuzzle();
		}
		if(event.getSource() == MainMenuView.editCurrentButton) {
			this.runAsEditCurrentPuzzle();
		}
	}
	
	private void runAsEditCurrentPuzzle() {
		int choice = JOptionPane.showConfirmDialog(this.main.window.jframe, "Editing an existing puzzle will delete any autosave.\nContinue anyway?", "Confirm discard progress", JOptionPane.YES_NO_OPTION);
		if(choice != 0){return;}
		
		this.main.window.disableMainMenu();
		this.main.runGame(true, -1, -1);
	}

	private void runAsCreateNewPuzzle() {
		int rows = 4;
		int columns = 4;
		
		try {
			rows = Integer.parseInt(MainMenuView.rowsTextPane.getText());
			columns = Integer.parseInt(MainMenuView.columnsTextPane.getText());
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "You must enter a row and column count to create a new puzzle.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int choice = JOptionPane.showConfirmDialog(this.main.window.jframe, "Creating a new puzzle will discard the previous one.\nIf you want to keep the old one you should make a backup\nContinue anyway?", "Confirm overwrite old puzzle", JOptionPane.YES_NO_OPTION);
		if(choice != 0){return;}
		
		this.main.window.disableMainMenu();
		this.main.runGame(false, rows, columns);
	}
}
