package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;
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
		int rows = -1, columns = -1;
		String src = "";
		boolean startWithEmptyBoard = false;
		
		if(event.getSource() == EditorMainMenuView.editCurrentButton) {
			if(EditorMainMenuView.existingPuzzleList.getSelectedValue() == null) {
				JOptionPane.showMessageDialog(null, "You have to select a puzzle to edit!", "oops!", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			src = ((PuzzleListItem)EditorMainMenuView.existingPuzzleList.getSelectedValue()).getPath();
			startWithEmptyBoard = false;
		} else if(event.getSource() == EditorMainMenuView.createNewButton) {
			String puzzleName = EditorMainMenuView.nameTextPane.getText();
			src = "res/puzzles/" + puzzleName + ".txt";
			if(new File(src).exists()) {
				int result = JOptionPane.showConfirmDialog(null, "A puzzle with this name already exists.\nDo you want to overwrite it?", "Overwrite puzzle?", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
				if(result != 0) {
					return;
				}
			}
			rows = Integer.parseInt(EditorMainMenuView.rowsTextPane.getText());
			columns = Integer.parseInt(EditorMainMenuView.columnsTextPane.getText());
			startWithEmptyBoard = true;
		}
		
		GameStartSettings settings = new GameStartSettings(OperationMode.EDITOR, "", src);
		settings.rows = rows;
		settings.columns = columns;
		settings.startWithEmptyEditor = startWithEmptyBoard;
		window.disableMainMenu();
		main.runGame(settings);
	}

}
