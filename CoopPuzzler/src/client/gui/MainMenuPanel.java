package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import client.ClientMain;
import client.GameStartSettings;
import client.puzzleFileList.PuzzleListItem;

@SuppressWarnings("serial")
public class MainMenuPanel extends JPanel implements ActionListener{
	private JPanel mainContentPanel;
	private ClientMain main;
	
	public MainMenuPanel(ClientMain main)
	{
		this.main = main;
		this.mainContentPanel = new MainMenuView();
		this.add(this.mainContentPanel);
		this.validate();
		MainMenuView.connectToServerButton.addActionListener(this);
		MainMenuView.playButton.addActionListener(this);
		MainMenuView.serverAddressTextBox.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {}
			public void keyTyped(KeyEvent event) {
				handleKeypress(event);
			}
		});
	}

	protected void handleKeypress(KeyEvent event) {
		if(event.getKeyChar() == '\n')
		{
			this.main.window.disableMainMenu();
			String hostName = MainMenuView.serverAddressTextBox.getText();
			GameStartSettings settings = new GameStartSettings(true, hostName, "");
			this.main.runGame(settings);
		}
	}

	public void actionPerformed(ActionEvent event) {
		if(MainMenuView.puzzleList.getSelectedValue() == null) {
			JOptionPane.showMessageDialog(null, "You have to select a puzzle to play!", "oops!", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		boolean isOnlineGame = event.getSource() == MainMenuView.connectToServerButton;
		String serverHostName = MainMenuView.serverAddressTextBox.getText();
		String puzzleFileSrc = ((PuzzleListItem)MainMenuView.puzzleList.getSelectedValue()).getPath();
		
		this.main.window.disableMainMenu();
		this.main.runGame(new GameStartSettings(isOnlineGame, serverHostName, puzzleFileSrc));
	}
}
