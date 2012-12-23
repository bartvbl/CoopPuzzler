package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import client.puzzleFileList.PuzzleListPopulator;

public class EditorMainSwitcher implements ActionListener {

	private JFrame jframe;
	private MainMenuPanel mainMenuPanel;

	public EditorMainSwitcher(JFrame jframe, MainMenuPanel mainMenuPanel) {
		this.jframe = jframe;
		this.mainMenuPanel = mainMenuPanel;
		EditorMainMenuView.mainMenuButton.addActionListener(this);
		MainMenuView.editPuzzleButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == MainMenuView.editPuzzleButton) {
			this.jframe.remove(this.mainMenuPanel);
			this.jframe.add(EditorMainMenuView.getInstance());
			PuzzleListPopulator.populatePuzzleList(EditorMainMenuView.existingPuzzleList);
		} else if(event.getSource() == EditorMainMenuView.mainMenuButton) {
			this.jframe.remove(EditorMainMenuView.getInstance());
			this.jframe.add(this.mainMenuPanel);
		}
		this.jframe.validate();
		this.jframe.repaint();
	}

}
