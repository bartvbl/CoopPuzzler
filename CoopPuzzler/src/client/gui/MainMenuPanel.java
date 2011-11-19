package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == MainMenuView.connectToServerButton)
		{
			System.out.println("a");
			this.main.window.disableMainMenu();
		} else if(event.getSource() == MainMenuView.playButton)
		{
			System.out.println("b");
			this.main.window.disableMainMenu();
		}
	}
}
