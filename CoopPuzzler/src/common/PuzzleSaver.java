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
			for(int x = 0; x < this.table.length; x++)
			{
				for(int y = 0; y < this.table[0].length; y++)
				{
					if(this.table[x][y].isFilled)
					{
						writer.append('*');
					} else {
						if(this.table[x][y].hasIgnoreReference)
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
			for(int x = 0; x < this.table.length; x++)
			{
				for(int y = 0; y < this.table[0].length; y++)
				{
					writer.append(""+this.table[x][y].getFieldTextColour().getColourIndex());
					writer.append(this.table[x][y].getCurrentValueOfField());
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