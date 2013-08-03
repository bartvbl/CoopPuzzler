package core;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import client.ClientMain;


public class ClientRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			//pre-initialization settings 
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "CoopPuzzler");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			//launching the game
			new ClientMain();
		}
		catch(Exception e)
		{
			String message = "launch failed: " + e.getMessage();
			StackTraceElement[] elements = e.getStackTrace();
			e.printStackTrace();
			for(StackTraceElement element : elements) {
				message += "\n" + element.toString();
			}
			JOptionPane.showMessageDialog(null, message);
			
		}
		
	}

}
