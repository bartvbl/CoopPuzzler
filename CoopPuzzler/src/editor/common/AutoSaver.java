package editor.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.Timer;

public class AutoSaver implements ActionListener {
	private PuzzleField[][] table;
	private Timer timer;
	
	private static final String AUTOSAVE_LOCATION = "res/puzzle.txt";
	
	public AutoSaver(PuzzleField[][] table)
	{
		this.table = table;
		this.timer = new Timer(5000, this);
		this.timer.start();
	}

	public void actionPerformed(ActionEvent arg0) {
		this.doSave();
	}

	private void doSave() {
		System.out.println("saving..");
		File destinationFile = new File(AUTOSAVE_LOCATION);
		if(destinationFile.exists())
		{
			destinationFile.delete();
		}
		try {
			destinationFile.createNewFile();
			FileWriter fileWriter = new FileWriter(destinationFile);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			writer.write(table.length + "x" + table[0].length);
			writer.newLine();
			for(int row = 0; row < this.table.length; row++)
			{
				for(int column = 0; column < this.table[0].length; column++)
				{
					if(this.table[row][column].isFilled)
					{
						writer.append('*');
					} else {
						if(this.table[row][column].hasIgnoreReference)
						{
							writer.append('i');
						} else {
							writer.append(' ');
						}
					}
				}
				writer.newLine();
			}
			writer.write("save");
			writer.newLine();
			for(int row = 0; row < this.table.length; row++)
			{
				for(int column = 0; column < this.table[0].length; column++)
				{
					writer.append(""+this.table[row][column].getFieldTextColour().getColourIndex());
					writer.append(this.table[row][column].getCurrentValueOfField());
				}
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("autosave failed.");
			e.printStackTrace();
		}
		
	}
}
