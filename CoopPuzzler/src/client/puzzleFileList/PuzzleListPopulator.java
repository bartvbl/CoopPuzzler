package client.puzzleFileList;

import java.io.File;

import javax.swing.JList;
import javax.swing.JOptionPane;

public class PuzzleListPopulator {
	private static final String puzzleFilesDirectoryPath = "res/puzzles/";

	public static void populatePuzzleList(JList list) {
		PuzzleListModel model = new PuzzleListModel();
		File[] puzzleFiles = findPuzzleFiles();
		addEntriesToModel(puzzleFiles, model);
		list.setModel(model);
	}

	private static File[] findPuzzleFiles() {
		File puzzleDirectory = new File(puzzleFilesDirectoryPath);
		File[] puzzleFiles = puzzleDirectory.listFiles(new PuzzleFileFilter());
		return puzzleFiles;
	}

	private static void addEntriesToModel(File[] puzzleFiles, PuzzleListModel model) {
		if((puzzleFiles == null) || (model == null)) {
			JOptionPane.showMessageDialog(null, "Could not find any puzzles at " + (new File(puzzleFilesDirectoryPath).getAbsolutePath()) + ".");
			return;
		}
		for(File puzzleFile : puzzleFiles) {
			model.addElement(new PuzzleListItem(puzzleFile));
		}
	}
}


