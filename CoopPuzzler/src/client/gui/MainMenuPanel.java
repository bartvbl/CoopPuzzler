package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import client.ClientMain;

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
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == MainMenuView.connectToServerButton)
		{
			this.main.runGame(true, MainMenuView.serverAddressTextBox.getText());
			this.main.window.disableMainMenu();
		} else if(event.getSource() == MainMenuView.playButton)
		{
			this.main.runGame(false, "");
			this.main.window.disableMainMenu();
		}
	}
}
