package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import client.ClientMain;
import client.ClientWindow;
import client.GameStartSettings;
import client.OperationMode;
import client.puzzleFileList.PuzzleListItem;

public class EditorMainMenuPanel implements ActionListener {

	private ClientWindow window;
	private ClientMain main;

	public EditorMainMenuPanel(ClientWindow window, ClientMain main) {
		new EditorMainMenuView();
		this.window = window;
		this.main = main;
		
		EditorMainMenuView.editCurrentButton.addActionListener(this);
		EditorMainMenuView.createNewButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		window.disableMainMenu();
		int rows = -1, columns = -1;
		String src = "";
		if(event.getSource() == EditorMainMenuView.editCurrentButton) {
			src = ((PuzzleListItem)EditorMainMenuView.existingPuzzleList.getSelectedValue()).getPath();
		} else if(event.getSource() == EditorMainMenuView.createNewButton) {
			rows = Integer.parseInt(EditorMainMenuView.rowsTextPane.getText());
			columns = Integer.parseInt(EditorMainMenuView.columnsTextPane.getText());		
		}
		GameStartSettings settings = new GameStartSettings(OperationMode.EDITOR, "", src);
		settings.rows = rows;
		settings.columns = columns;
		main.runGame(settings);
	}

}
