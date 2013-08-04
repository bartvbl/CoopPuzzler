package common;

import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PuzzleSaver {

	PuzzleField[][] table;
	private String saveTargetLocation;
	private boolean isSavingEnabled = true;;
	
	public PuzzleSaver(PuzzleField[][] table, String saveTarget) {
		this.table = table;
		this.saveTargetLocation = saveTarget;
	}
	
	public void setSavingEnabled(boolean enabled) {
		this.isSavingEnabled  = enabled;
	}

	protected void doSave() {
		if(!isSavingEnabled) {
			return;
		}
		File destinationFile = new File(saveTargetLocation);
		System.out.println("saving to " + destinationFile.getPath() + "..");
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