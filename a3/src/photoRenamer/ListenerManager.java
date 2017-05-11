package photoRenamer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The class that manages all the action listener and what they do.
 */
public class ListenerManager {
	
	/** Implemented Observer design pattern since the directory, image and tag class are all
	 *  dependent on each other for the program. For example, adding a tag to an image requires
	 *  the program to check the master tag file found in the directory.
	 */
	
	/** All the buttons that the program requires. */
	private final JButton directoryButton;
	private final JButton addTagsButton;
	private final JButton removeTagsButton;
	private final JButton renameButton;
	private final JButton newTagButton;
	private final JButton deleteTagButton;
	private final JButton acceptButton;
	
	/** All the scroll panes that the program requires. */
	private final JScrollPane picturePane;
	private final JScrollPane tagsPane;
	private final JScrollPane namesPane;
	
	/** All the ListModels that are used by the JLists. */
	private DefaultListModel<String> pictureModel;
	private DefaultListModel<String> tagsModel;
	private DefaultListModel<String> namesModel;
	/** All the Jlists that are displayed on the scroll panes. */
	private JList<String> pictureList;
	private JList<String> tagsList;
	private JList<String> namesList;
	/** The strings or list of strings that the user selects. */
	private String pictureSelection;
	private String[] tagsSelection;
	private String nameSelection;
	
	/** The text area that the user uses to enter new values for tags and names. */
	private final JTextArea textArea;
	
	/** The other classes that are required for the program to function. */
	private Directory directory;
	private Tags tags;
	private ArrayList<Image> images;
	/** A boolean to check what the text area is used for. */
	private boolean acceptState;
	
	/**
	 * Creates a new manager that controls all the action listener of the program.
	 * 
	 * @param buttons
	 * 				The buttons that the program uses.
	 * @param scrollPanes
	 * 				The scroll panes that the program uses.
	 * @param textArea
	 * 				The text area that the program uses to get user input.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public ListenerManager(JButton[] buttons, JScrollPane[] scrollPanes, JTextArea textArea) throws ClassNotFoundException, IOException{
		this.directoryButton = buttons[0];
		this.addTagsButton = buttons[1];
		this.removeTagsButton = buttons[2];
		this.renameButton = buttons[3];
		this.newTagButton = buttons[4];
		this.deleteTagButton = buttons[5];
		this.acceptButton = buttons[6];
		this.picturePane = scrollPanes[0];
		this.tagsPane = scrollPanes[1];
		this.namesPane = scrollPanes[2];
		this.textArea = textArea;
		this.tags = Tags.getInstance();
		this.pictureModel = new DefaultListModel<String>();
		this.tagsModel = new DefaultListModel<String>();
		this.namesModel = new DefaultListModel<String>();
		this.pictureList = new JList<String>(pictureModel);
		this.tagsList = new JList<String>(tagsModel);
		this.namesList = new JList<String>(namesModel);
		this.pictureSelection = null;
		this.nameSelection = null;
		this.tagsSelection = null;
		init();
	}
	
	/**
	 * Initializes the JLists and sets their properties.
	 */
	private void init(){
		this.pictureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.tagsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.namesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		updateListModel(this.tagsModel, this.tags.getAllTagsUsed().toArray(new String[0]));
		this.picturePane.setViewportView(this.pictureList);
		this.tagsPane.setViewportView(this.tagsList);
		this.namesPane.setViewportView(this.namesList);
	}
	
	/**
	 * Creates all the action listeners that the program requires.
	 */
	public void createListeners(){
		
		this.pictureList.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				pictureSelection(pictureList.getSelectedValue());
			}
		});
		this.tagsList.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				tagsSelection(tagsList.getSelectedValuesList());
			}
		});
		this.namesList.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				nameSelection(namesList.getSelectedValue());
			}
		});
		this.directoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					showFileChooser();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.addTagsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					modifyTags(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.removeTagsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					modifyTags(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.renameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				renameSelection();
			}
		});
		this.deleteTagButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					removeTagFromList();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.newTagButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showTextArea(false);
			}
		});
		this.acceptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptChecker();
			}
		});
	}
	
	/**
	 * Sets nameSelection variable to what the user selected
	 * in the names JList.
	 * 
	 * @param nameSelected
	 * 				The value that the user selected from the names JList.
	 */
	private void nameSelection(String nameSelected){
		this.nameSelection = nameSelected;
	}
	
	/**
	 * Sets tagsSelection variable to what the user selected
	 * in the tags JList as well as enabling certain buttons.
	 * 
	 * @param selectedValues
	 * 				The list of values that the user selected from the tags JList.
	 */
	private void tagsSelection(List<String> selectedValues) {
		this.tagsSelection = selectedValues.toArray(new String[0]);
		if (this.tagsSelection != null && this.tagsSelection.length > 0){
			this.deleteTagButton.setEnabled(true);
			if (this.pictureSelection != null){
				this.addTagsButton.setEnabled(true);
				this.removeTagsButton.setEnabled(true);
			}
		}
	}
	
	/**
	 * Sets pictureSelection variable to what the user selected
	 * in the picture JList as well as displaying the previous names of 
	 * the selected image.
	 * 
	 * @param listSelection
	 * 				The value that the user selected from the picture JList.
	 */
	private void pictureSelection(String listSelection){
		this.pictureSelection = listSelection;
		if (this.pictureSelection != null){
			if (this.tagsSelection != null && this.tagsSelection.length > 0){
				this.addTagsButton.setEnabled(true);
				this.removeTagsButton.setEnabled(true);
			}
			updateListModel(this.namesModel, getImage(this.pictureSelection).getNames().toArray(new String[0]));
			this.renameButton.setEnabled(true);
		}
	}
	
	/**
	 * Removes elected tags from the tags list as well as disabling certain buttons.
	 * 
	 * @throws IOException
	 */
	private void removeTagFromList() throws IOException{
		String[] selected = this.tagsSelection;
		this.tags.deleteTags(selected);
		updateListModel(this.tagsModel, this.tags.getAllTagsUsed().toArray(new String[0]));
		this.deleteTagButton.setEnabled(false);
		this.addTagsButton.setEnabled(false);
		this.removeTagsButton.setEnabled(false);
	}
	
	/**
	 * Enables the accept button and displays the text area for user to type in.
	 * 
	 * @param accept
	 * 				true is for renaming and false if for creating a new tag.
	 */
	private void showTextArea(boolean accept){
		this.acceptState = accept;
		this.pictureList.setEnabled(false);
		this.tagsList.setEnabled(false);
		this.deleteTagButton.setEnabled(false);
		this.addTagsButton.setEnabled(false);
		this.removeTagsButton.setEnabled(false);
		this.newTagButton.setEnabled(false);
		this.renameButton.setEnabled(false);
		this.acceptButton.setEnabled(true);
		this.textArea.setVisible(true);	
	}
	
	/**
	 * Calls corresponding methods after checking if text area is being
	 * used for new name or new tag as well as disabling text area and the accept button.
	 */
	private void acceptChecker(){
		if (!acceptState){
			newTag();
		}else{
			renameFile(this.textArea.getText());
		}
		this.tagsList.setEnabled(true);
		this.pictureList.setEnabled(true);
		this.acceptButton.setEnabled(false);
		this.textArea.setText(null);
		this.textArea.setVisible(false);
	}
	
	/**
	 * Remove or add tags to the selected file name as well as
	 * disabling certain buttons.
	 * 
	 * @param modify
	 * 				true for adding tags and false for removing tags.
	 * @throws IOException
	 */
	private void modifyTags(boolean modify) throws IOException {
		String[] selected = this.tagsSelection;
		if (modify){
			getImage(this.pictureSelection).addTag(selected);
		}else{
			getImage(this.pictureSelection).removeTag(selected);
		}
		this.pictureList.clearSelection();
		this.tagsList.clearSelection();
		updateListModel(this.pictureModel, imageNames());
		this.namesModel.clear();
		this.nameSelection = null;
		this.addTagsButton.setEnabled(false);
		this.removeTagsButton.setEnabled(false);
		this.deleteTagButton.setEnabled(false);
	}
	
	/**
	 * Checks whether or not the user wants to enter a new name
	 * or change to a previous name.
	 */
	private void renameSelection(){
		if (this.nameSelection == null){
			showTextArea(true);
		}else{
			renameFile(this.nameSelection);
		}
	}
	
	/**
	 * Renames the selected file with the specified name.
	 * 
	 * @param name
	 * 				The name to change the current file name to.
	 */
	private void renameFile(String name){
		String selected = this.pictureSelection;
		String newName = name;
		try {
			getImage(selected).rename(newName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.newTagButton.setEnabled(true);
		this.tagsList.clearSelection();
		updateListModel(this.pictureModel, imageNames());
		this.namesModel.clear();
		this.nameSelection = null;
		this.renameButton.setEnabled(false);
		this.addTagsButton.setEnabled(false);
		this.removeTagsButton.setEnabled(false);
		this.deleteTagButton.setEnabled(false);
	}
	
	/**
	 * Returns the Image object with given string name.
	 * 
	 * @param imageName
	 * 				The name of the Image to search for.
	 * @return
	 * 				The Image object of the corresponding string.
	 */
	private Image getImage(String imageName){
		Image temp = null;
		for (Image item: this.images){
			if (item.getImageName().equals(imageName)){
				temp = item;
			}
		}
		return temp;
	}
	
	/**
	 * Adds a new tag to the tags object's list of tags.
	 */
	private void newTag(){
		String[] a = {this.textArea.getText()};
		try {
			this.tags.updateTags(a);
		} catch (IOException e) {
			System.out.println("asd");
		}
		this.newTagButton.setEnabled(true);
		this.pictureList.clearSelection();
		this.deleteTagButton.setEnabled(false);
		this.namesModel.clear();
		this.nameSelection = null;
		updateListModel(this.tagsModel, this.tags.getAllTagsUsed().toArray(new String[0]));
	}
	
	/**
	 * Shows a directory explorer to let user select a directory of .jpg files.
	 * Creates and display the new directory iff the user selects a directory.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void showFileChooser() throws ClassNotFoundException, IOException {
		this.renameButton.setEnabled(false);
		this.addTagsButton.setEnabled(false);
		this.removeTagsButton.setEnabled(false);
		this.namesModel.clear();
		this.pictureList.clearSelection();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.showOpenDialog(null);
		if(fileChooser.getSelectedFile() != null){
			this.directory = new Directory(fileChooser.getSelectedFile());
			ArrayList<File> images = new ArrayList<>();
			images = directory.getImages(directory.directory, images);
			createImages(images);
			String[] imagesName = imageNames();
			updateListModel(this.pictureModel, imagesName);
		}
	}
	
	/**
	 * Returns a String[] array of names of the list of Image object.
	 * 
	 * @return
	 * 				String[] array of names of all Image in directory.
	 */
	private String[] imageNames(){
		String[] temp = new String[images.size()];
		int x = 0;
		for (Image item: this.images){
			temp[x] = item.getImageName();
			x++;
		}
		return temp;
	}
	
	/**
	 * Creates new Image object with list of file from selected directory.
	 * 
	 * @param images
	 * 				ArrayList of files of the images in selected directory.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void createImages(ArrayList<File> images) throws ClassNotFoundException, IOException{
		this.images = new ArrayList<Image>();
		for (File item: images){
			this.images.add(new Image(item, item.getPath().split(".jpg")[0] + ".txt"));
		}
	}
	
	/**
	 * Changes the given list model to new given updated String[] array.
	 * 
	 * @param listModel
	 * 				The ListModel to be updated.
	 * @param updatedText
	 * 				The updated list to change the ListModel with.
	 */
	private void updateListModel(DefaultListModel<String>  listModel, String[] updatedText){
		listModel.clear();
		for (String item: updatedText){
			listModel.addElement(item);
		}
	}
}
