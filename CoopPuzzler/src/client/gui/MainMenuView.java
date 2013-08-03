/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainMenuPanel.java
 *
 * Created on 23.des.2012, 14:59:46
 */
package client.gui;

import java.awt.Font;

/**
 *
 * @author Bart
 */
public class MainMenuView extends javax.swing.JPanel {

	/** Creates new form MainMenuPanel */
	public MainMenuView() {
		initComponents();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		Font boldFont = new Font("Tahoma", Font.BOLD, 11);
		Font mainMenuFont = new Font("Tahoma", Font.BOLD, 18);


		mainMenuLabel = new javax.swing.JLabel();
		playLocalGameLabel = new javax.swing.JLabel();
		puzzleListScrollPane = new javax.swing.JScrollPane();
		puzzleList = new javax.swing.JList();
		playButton = new javax.swing.JButton();
		puzzleListDescriptionLabel = new javax.swing.JLabel();
		mainSeparator = new javax.swing.JSeparator();
		playGameOnServer = new javax.swing.JLabel();
		serverAddressLabel = new javax.swing.JLabel();
		serverAddressScrollPane = new javax.swing.JScrollPane();
		serverAddressTextBox = new javax.swing.JTextPane();
		connectToServerButton = new javax.swing.JButton();
		editPuzzleButton = new javax.swing.JButton();
		postPaneScrollPane = new javax.swing.JScrollPane();
		portTextField = new javax.swing.JTextPane();
		portLabel = new javax.swing.JLabel();
		createServerButton = new javax.swing.JButton();

		setName("Form"); // NOI18N
		setPreferredSize(new java.awt.Dimension(500, 241));
		setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		mainMenuLabel.setFont(mainMenuFont); // NOI18N
		mainMenuLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		mainMenuLabel.setText("Main Menu"); // NOI18N
		mainMenuLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		mainMenuLabel.setName("mainMenuLabel"); // NOI18N
		add(mainMenuLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 500, 25));

		playLocalGameLabel.setFont(boldFont); // NOI18N
		playLocalGameLabel.setText("Play local game"); // NOI18N
		playLocalGameLabel.setName("playLocalGameLabel"); // NOI18N
		add(playLocalGameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 31, -1, -1));

		puzzleListScrollPane.setName("puzzleListScrollPane"); // NOI18N

		puzzleList.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "Failed to list puzzles" };
			public int getSize() { return strings.length; }
			public Object getElementAt(int i) { return strings[i]; }
		});
		puzzleList.setName("puzzleList"); // NOI18N
		puzzleListScrollPane.setViewportView(puzzleList);

		add(puzzleListScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 71, 200, -1));

		playButton.setText("Play"); // NOI18N
		playButton.setName("playButton"); // NOI18N
		add(playButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 207, -1, -1));

		puzzleListDescriptionLabel.setText("Select a puzzle to solve:"); // NOI18N
		puzzleListDescriptionLabel.setName("puzzleListDescriptionLabel"); // NOI18N
		add(puzzleListDescriptionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 51, -1, -1));

		mainSeparator.setOrientation(javax.swing.SwingConstants.VERTICAL);
		mainSeparator.setName("mainSeparator"); // NOI18N
		add(mainSeparator, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 31, 13, 228));

		playGameOnServer.setFont(boldFont); // NOI18N
		playGameOnServer.setText("Play game on a server"); // NOI18N
		playGameOnServer.setName("playGameOnServer"); // NOI18N
		add(playGameOnServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(239, 31, -1, -1));

		serverAddressLabel.setText("Server address:"); // NOI18N
		serverAddressLabel.setName("serverAddressLabel"); // NOI18N
		add(serverAddressLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(239, 51, -1, 23));

		serverAddressScrollPane.setName("serverAddressScrollPane"); // NOI18N

		serverAddressTextBox.setName("serverAddressTextBox"); // NOI18N
		serverAddressScrollPane.setViewportView(serverAddressTextBox);

		add(serverAddressScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 51, 161, 23));

		connectToServerButton.setText("Connect"); // NOI18N
		connectToServerButton.setName("connectToServerButton"); // NOI18N
		add(connectToServerButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(239, 80, -1, -1));

		editPuzzleButton.setText("Create/edit a puzzle"); // NOI18N
		editPuzzleButton.setName("editPuzzleButton"); // NOI18N
		add(editPuzzleButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 207, -1, -1));

		postPaneScrollPane.setName("postPaneScrollPane"); // NOI18N

		portTextField.setName("portTextField"); // NOI18N
		postPaneScrollPane.setViewportView(portTextField);

		add(postPaneScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 236, 41, 23));

		portLabel.setText("Port:"); // NOI18N
		portLabel.setName("portLabel"); // NOI18N
		add(portLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 236, -1, 23));

		createServerButton.setText("Host game"); // NOI18N
		createServerButton.setName("createServerButton"); // NOI18N
		add(createServerButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(111, 236, -1, -1));
	}// </editor-fold>
	// Variables declaration - do not modify
	public static javax.swing.JButton connectToServerButton;
	public static javax.swing.JButton createServerButton;
	public static javax.swing.JButton editPuzzleButton;
	public static javax.swing.JLabel mainMenuLabel;
	public static javax.swing.JSeparator mainSeparator;
	public static javax.swing.JButton playButton;
	public static javax.swing.JLabel playGameOnServer;
	public static javax.swing.JLabel playLocalGameLabel;
	public static javax.swing.JLabel portLabel;
	public static javax.swing.JTextPane portTextField;
	public static javax.swing.JScrollPane postPaneScrollPane;
	public static javax.swing.JList puzzleList;
	public static javax.swing.JLabel puzzleListDescriptionLabel;
	public static javax.swing.JScrollPane puzzleListScrollPane;
	public static javax.swing.JLabel serverAddressLabel;
	public static javax.swing.JScrollPane serverAddressScrollPane;
	public static javax.swing.JTextPane serverAddressTextBox;
	// End of variables declaration
}
