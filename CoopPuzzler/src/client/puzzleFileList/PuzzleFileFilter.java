package client.puzzleFileList;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.filechooser.FileFilter;

public class PuzzleFileFilter extends FileFilter implements FilenameFilter {

	public boolean accept(File file, String name) {
		return name.endsWith(".txt");
	}

	public String getDescription() {
		return "Puzzle Files";
	}

	public boolean accept(File file) {
		return file.getName().endsWith(".txt");
	}	
}
