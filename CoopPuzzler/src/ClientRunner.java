import javax.swing.JOptionPane;

import client.ClientMain;


public class ClientRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			new ClientMain();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "launch failed: " + e.getMessage());
		}
		
	}

}
