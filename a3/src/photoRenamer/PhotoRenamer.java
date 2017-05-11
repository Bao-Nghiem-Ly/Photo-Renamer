package photoRenamer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

/**
 * Create and show a program that allow the user to choose a directory
 * if the directory contains .jpg files then the program allows the user
 * to add tags to the file or rename it.
 */
public class PhotoRenamer {
	/** The JFrame that contains the program. */
	private final JFrame directoryFrame;
	
	/** The dimensions for the scroll pane that contains the file names. */
	private static final Dimension d1 = new Dimension(150, 200);
	/** The dimensions for the scroll panes that contains the tags and previous names */
	private static final Dimension d2 = new Dimension(125, 200);
	
	/**
	 * Creates the window and sets up the layout for the Photo Renamer program.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private PhotoRenamer() throws ClassNotFoundException, IOException {
		this.directoryFrame = new JFrame("Photo Renamer");

		JLabel tagsLabel = new JLabel("Tags:");
		JLabel nameLabel = new JLabel("Names:");
		JLabel pictureLabel = new JLabel("Images:");
		
		JTextArea textArea = new JTextArea(1,41);
		textArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		textArea.setVisible(false);
		
		JScrollPane[] paneList = new JScrollPane[3];
		JScrollPane picturePane = new JScrollPane();
		paneList[0] = picturePane;
		JScrollPane tagsPane = new JScrollPane();
		paneList[1] = tagsPane;
		JScrollPane namesPane = new JScrollPane();
		paneList[2] = namesPane;
		picturePane.setPreferredSize(d1);
		tagsPane.setPreferredSize(d2);
		namesPane.setPreferredSize(d1);
		
		JButton[] buttonList = new JButton[7];
		JButton directoryButton = new JButton("Choose directory");
		buttonList[0] = directoryButton;
		JButton addTagsButton = new JButton("Add tags");
		addTagsButton.setEnabled(false);
		buttonList[1] = addTagsButton;
		JButton removeTagsButton = new JButton("Remove tags");
		removeTagsButton.setEnabled(false);
		buttonList[2] = removeTagsButton;
		JButton renameButton = new JButton("Rename");
		renameButton.setEnabled(false);
		buttonList[3] = renameButton;
		JButton newTagButton = new JButton("Add new tag");
		buttonList[4] = newTagButton;
		JButton deleteTagButton = new JButton("Remove tag from list");
		deleteTagButton.setEnabled(false);
		buttonList[5] = deleteTagButton;
		JButton acceptButton = new JButton("Accept");
		buttonList[6] = acceptButton;
		acceptButton.setEnabled(false);
		acceptButton.setPreferredSize(new Dimension(75,20));

		Container c = directoryFrame.getContentPane();
		JPanel labelScroll1 = new JPanel(new BorderLayout());
		JPanel labelScroll2 = new JPanel(new BorderLayout());
		JPanel labelScroll3 = new JPanel(new BorderLayout());
		JPanel combinedPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel2 = new JPanel(new BorderLayout());
		JPanel combinedPanel2 = new JPanel(new BorderLayout());
		JPanel newTagPanel = new JPanel(new BorderLayout());
		labelScroll1.add(pictureLabel, BorderLayout.NORTH);
		labelScroll1.add(picturePane, BorderLayout.SOUTH);
		labelScroll2.add(tagsLabel, BorderLayout.NORTH);
		labelScroll2.add(tagsPane, BorderLayout.SOUTH);
		labelScroll3.add(nameLabel, BorderLayout.NORTH);
		labelScroll3.add(namesPane, BorderLayout.SOUTH);
		combinedPanel.add(labelScroll1 , BorderLayout.CENTER);
		combinedPanel.add(labelScroll2, BorderLayout.LINE_START);
		combinedPanel.add(labelScroll3, BorderLayout.LINE_END);
		buttonPanel.add(addTagsButton , BorderLayout.LINE_START);
		buttonPanel.add(removeTagsButton , BorderLayout.CENTER);
		buttonPanel.add(renameButton, BorderLayout.LINE_END);
		buttonPanel2.add(deleteTagButton , BorderLayout.EAST);
		buttonPanel2.add(newTagButton , BorderLayout.WEST);
		newTagPanel.add(textArea , BorderLayout.WEST);
		newTagPanel.add(acceptButton , BorderLayout.EAST);
		combinedPanel2.add(buttonPanel , BorderLayout.LINE_START);
		combinedPanel2.add(buttonPanel2 , BorderLayout.LINE_END);
		combinedPanel2.add(newTagPanel , BorderLayout.PAGE_END);
		c.add(directoryButton, BorderLayout.PAGE_START);
		c.add(combinedPanel, BorderLayout.CENTER);
		c.add(combinedPanel2, BorderLayout.PAGE_END);
		
		ListenerManager manager = new ListenerManager(buttonList, paneList, textArea);
		manager.createListeners();
	}
	
	/**
	 * Sets up the window's JFrame and makes it visible to the user.
	 */
	private void createAndShowGui() {
		directoryFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		directoryFrame.pack();
		directoryFrame.setVisible(true);
	}
	
	/**
	 * Create and show a PhotoRenamer, which allows the users to modify the name .jpg files.
	 *
	 * @param argsv
	 *            the command-line arguments.
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		PhotoRenamer v = new PhotoRenamer();
		v.createAndShowGui();

	}

}
