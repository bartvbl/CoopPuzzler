/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJPanel.java
 *
 * Created on Nov 19, 2011, 1:35:50 AM
 */
package client.gui;

import java.awt.Font;

/**
 * 
 * @author Bart
 */
@SuppressWarnings("serial")
public class MainMenuView extends javax.swing.JPanel {

	/** Creates new form NewJPanel */
	public MainMenuView() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {

		mainMenuLabel = new javax.swing.JLabel();
		serverAddressTextBox = new javax.swing.JTextField();
		playLocalGameLabel = new javax.swing.JLabel();
		playButton = new javax.swing.JButton();
		playGameOnServerLabel = new javax.swing.JLabel();
		serverAddressLabel = new javax.swing.JLabel();
		connectToServerButton = new javax.swing.JButton();
		mainSeparator = new javax.swing.JSeparator();

		setName("Form"); // NOI18N

		Font mainMenuFont = new Font("Tahoma", Font.BOLD, 18);
		Font headerFont = new Font("Tahoma", Font.BOLD, 11);
		mainMenuLabel.setFont(mainMenuFont); // NOI18N
		mainMenuLabel.setText("Main Menu"); // NOI18N
		mainMenuLabel.setName("mainMenuLabel"); // NOI18N

		serverAddressTextBox.setText(""); // NOI18N
		serverAddressTextBox.setName("jTextField1"); // NOI18N

		playLocalGameLabel.setFont(headerFont); // NOI18N
		playLocalGameLabel.setText("Play local game"); // NOI18N
		playLocalGameLabel.setName("playLocalGameLabel"); // NOI18N

		playButton.setText("Play"); // NOI18N
		playButton.setName("playButton"); // NOI18N

		playGameOnServerLabel.setFont(headerFont); // NOI18N
		playGameOnServerLabel.setText("Play game on a server"); // NOI18N
		playGameOnServerLabel.setName("playGameOnServerLabel"); // NOI18N

		serverAddressLabel.setText("Server address: "); // NOI18N
		serverAddressLabel.setName("serverAddressLabel"); // NOI18N

		connectToServerButton.setText("Connect"); // NOI18N
		connectToServerButton.setName("connectToServerButton"); // NOI18N

		mainSeparator.setOrientation(javax.swing.SwingConstants.VERTICAL);
		mainSeparator.setName("mainSeparator"); // NOI18N

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addContainerGap()
																.addComponent(
																		playLocalGameLabel))
												.addGroup(
														layout.createSequentialGroup()
																.addGap(32, 32,
																		32)
																.addComponent(
																		playButton)))
								.addGap(18, 18, 18)
								.addComponent(mainSeparator,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										10,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		mainMenuLabel,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		84,
																		Short.MAX_VALUE)
																.addGap(168,
																		168,
																		168))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		serverAddressLabel)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		serverAddressTextBox,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		160,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		playGameOnServerLabel)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED))
												.addComponent(
														connectToServerButton))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(mainMenuLabel)
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		playLocalGameLabel)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		playButton)
																.addContainerGap())
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.TRAILING,
																				false)
																				.addComponent(
																						mainSeparator,
																						javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						javax.swing.GroupLayout.Alignment.LEADING,
																						layout.createSequentialGroup()
																								.addComponent(
																										playGameOnServerLabel)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.BASELINE)
																												.addComponent(
																														serverAddressLabel)
																												.addComponent(
																														serverAddressTextBox,
																														javax.swing.GroupLayout.PREFERRED_SIZE,
																														javax.swing.GroupLayout.DEFAULT_SIZE,
																														javax.swing.GroupLayout.PREFERRED_SIZE))
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addComponent(
																										connectToServerButton)))
																.addContainerGap()))));
	}// </editor-fold>//GEN-END:initComponents



	// Variables declaration - do not modify//GEN-BEGIN:variables
	public static javax.swing.JButton connectToServerButton;
	public static javax.swing.JTextField serverAddressTextBox;
	public static javax.swing.JLabel mainMenuLabel;
	public static javax.swing.JSeparator mainSeparator;
	public static javax.swing.JButton playButton;
	public static javax.swing.JLabel playGameOnServerLabel;
	public static javax.swing.JLabel playLocalGameLabel;
	public static javax.swing.JLabel serverAddressLabel;
	// End of variables declaration//GEN-END:variables
}
