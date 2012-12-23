package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import client.ClientWindow;
import client.puzzleFileList.PuzzleListItem;

public class EditorMainMenuPanel implements ActionListener {

	private ClientWindow window;

	public EditorMainMenuPanel(ClientWindow window) {
		new EditorMainMenuView();
		this.window = window;
		
		EditorMainMenuView.editCurrentButton.addActionListener(this);
		EditorMainMenuView.createNewButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		window.disableMainMenu();
		if(event.getSource() == EditorMainMenuView.editCurrentButton) {
			String src = ((PuzzleListItem)EditorMainMenuView.existingPuzzleList.getSelectedValue()).getPath();
		} else if(event.getSource() == EditorMainMenuView.createNewButton) {
			int rows = Integer.parseInt(EditorMainMenuView.rowsTextPane.getText());
			int columns = Integer.parseInt(EditorMainMenuView.columnsTextPane.getText());		
		}
		
	}

}
