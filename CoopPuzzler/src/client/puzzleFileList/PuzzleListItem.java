package client.puzzleFileList;

import java.io.File;

public class PuzzleListItem {
	private final File puzzleFile ;

	public PuzzleListItem(File puzzleFile) {
		this.puzzleFile = puzzleFile;
	}
	
	public String toString() {
		String fileName = puzzleFile.getName();
		String nameStrippedFromExtension = fileName.substring(0, fileName.length() - 4);
		return nameStrippedFromExtension;
	}
	
	public String getPath() {
		return this.puzzleFile.getPath();
	}
}
