package server;

import java.awt.ScrollPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
		this.jframe.addWindowListener(new WindowListener() {public void windowOpened(WindowEvent e) {}public void windowIconified(WindowEvent e) {}public void windowDeactivated(WindowEvent e) {}public void windowClosed(WindowEvent e) {}public void windowActivated(WindowEvent e) {}public void windowDeiconified(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				System.out.println("g'day!");
			}
		});
	}
	
	public void writeMessage(String message)
	{
		this.outputPane.setText(this.outputPane.getText() + "\n" + message);
		this.outputPane.setCaretPosition(this.outputPane.getText().length() - 1);
	}
}
