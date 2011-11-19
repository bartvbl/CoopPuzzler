package client.gui;

import javax.swing.JPanel;

import client.ClientMain;

@SuppressWarnings("serial")
public class MainMenuPanel extends JPanel{
	private JPanel mainContentPanel;
	
	public MainMenuPanel(ClientMain main)
	{
		this.mainContentPanel = new MainMenuView();
		this.add(this.mainContentPanel);
		this.validate();
	}
}
