package client.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import common.ProtocolConstants;

import client.ClientMain;
import client.GameStartSettings;
import client.OperationMode;
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
		this.mainContentPanel.setPreferredSize(new Dimension(530, 311));
		this.validate();
		
		
		MainMenuView.connectToServerButton.addActionListener(this);
		MainMenuView.createServerButton.addActionListener(this);
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
			this.actionPerformed(new ActionEvent(MainMenuView.connectToServerButton, 0, ""));
		}
	}

	public void actionPerformed(ActionEvent event) {
		boolean isHostedGame = event.getSource() == MainMenuView.createServerButton;
		boolean isOnlineGame = event.getSource() == MainMenuView.connectToServerButton;
		OperationMode operationMode;
		
		String serverHostName = MainMenuView.serverAddressTextBox.getText();
		String puzzleFileSrc = "";
		int port = 0;

		if(isOnlineGame) {
			operationMode = OperationMode.ONLINE_GAME;
			if(serverHostName.contains(":")) {
				String[] addressParts = serverHostName.split(":");
				serverHostName = addressParts[0];
				try{
					port = Integer.parseInt(addressParts[1]);
				} catch(Exception e) {
					JOptionPane.showMessageDialog(null, "A port number must be an integer between 1 and 65535!", "oops!", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}
		} else if(isHostedGame) {
			if(MainMenuView.puzzleList.getSelectedValue() == null) {
				JOptionPane.showMessageDialog(null, "You have to select a puzzle to host!", "oops!", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			puzzleFileSrc = ((PuzzleListItem)MainMenuView.puzzleList.getSelectedValue()).getPath();
			
			try {
				serverHostName = "127.0.0.1";
				port = Integer.parseInt(MainMenuView.portTextField.getText());
				if((port > 65535) || (port < 1)) {
					JOptionPane.showMessageDialog(null, "A port number must be an integer between 1 and 65535!", "oops!", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null, "A port number must be an integer between 1 and 65535!", "oops!", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			operationMode = OperationMode.HOSTED_GAME;
		} else {
			if(MainMenuView.puzzleList.getSelectedValue() == null) {
				JOptionPane.showMessageDialog(null, "You have to select a puzzle to play!", "oops!", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			puzzleFileSrc = ((PuzzleListItem)MainMenuView.puzzleList.getSelectedValue()).getPath();
			
			operationMode = OperationMode.LOCAL_GAME;
		}
		
		this.main.window.disableMainMenu();
		this.main.runGame(new GameStartSettings(operationMode, serverHostName, port, puzzleFileSrc));
	}
}
