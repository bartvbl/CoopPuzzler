package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import client.ClientMain;
import client.GameStarter;

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
			this.main.runGame(true, MainMenuView.serverAddressTextBox.getText());
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == MainMenuView.connectToServerButton)
		{
			this.main.window.disableMainMenu();
			this.main.runGame(true, MainMenuView.serverAddressTextBox.getText());
		} else if(event.getSource() == MainMenuView.playButton)
		{
			this.main.window.disableMainMenu();
			this.main.runGame(false, "");
		}
	}
}
