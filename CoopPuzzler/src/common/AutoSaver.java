package common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Writer;

import javax.swing.Timer;

public class AutoSaver extends PuzzleSaver implements ActionListener {
	private Timer timer;
	private boolean isEnabled = true;
	
	public AutoSaver(PuzzleField[][] table, String autosaveLocation)
	{
		super(table, autosaveLocation);
		this.timer = new Timer(10000, this);
		this.timer.start();
	}

	public void actionPerformed(ActionEvent arg0) {
		if(isEnabled) {
			this.doSave();
		}
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}
}
