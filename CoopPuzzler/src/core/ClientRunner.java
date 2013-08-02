package core;
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
