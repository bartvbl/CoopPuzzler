package server;

import java.awt.ScrollPane;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class ServerWindow {
	private JFrame jframe;
	private JTextPane outputPane;
	private JScrollPane scroller;
	
	public ServerWindow()
	{
		this.jframe = new JFrame("Server");
		
		JScrollPane scroller = new JScrollPane();
		JTextPane outputPane = new JTextPane();
		this.outputPane = outputPane;
		outputPane.setEditable(false);
		outputPane.setAutoscrolls(true);
		this.scroller = scroller;
		scroller.setViewportView(outputPane);
		scroller.setAutoscrolls(true);
		this.jframe.setContentPane(scroller);
		this.jframe.pack();
		this.jframe.setSize(300, 300);
		this.jframe.setVisible(true);
		this.outputPane.setText("Coop puzzler server v0.1");
	}
	
	public void writeMessage(String message)
	{
		this.outputPane.setText(this.outputPane.getText() + "\n" + message);
		this.outputPane.setCaretPosition(this.outputPane.getText().length() - 1);
	}
}
