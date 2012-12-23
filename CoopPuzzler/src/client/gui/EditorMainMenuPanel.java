package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import client.ClientWindow;
import client.EditorMainSwitcher;
import client.puzzleFileList.PuzzleListItem;
import editor.EditorMain;

public class EditorMainMenuPanel implements ActionListener {

	private EditorMain editorMain;
	private ClientWindow window;

	public EditorMainMenuPanel(ClientWindow window) {
		new EditorMainMenuView();
		this.editorMain = new EditorMain(window);
		this.window = window;
		
		EditorMainMenuView.editCurrentButton.addActionListener(this);
		EditorMainMenuView.createNewButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == EditorMainMenuView.editCurrentButton) {
			window.disableMainMenu();
			String src = ((PuzzleListItem)EditorMainMenuView.existingPuzzleList.getSelectedValue()).getPath();
			this.editorMain.runGame(true, 0, 0, src);
		} else if(event.getSource() == EditorMainMenuView.createNewButton) {
			int rows = Integer.parseInt(EditorMainMenuView.rowsTextPane.getText());
			int columns = Integer.parseInt(EditorMainMenuView.columnsTextPane.getText());
			this.editorMain.runGame(false, rows, columns, "");			
		}
	}

}
