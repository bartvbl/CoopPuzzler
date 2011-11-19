package client.gui;

import javax.swing.JOptionPane;

public class FeedbackProvider {
	private static void showErrorMessage(String message)
	{
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void showFailedToFindServerMessage() {
		showErrorMessage("Could not find server");
	}
}
