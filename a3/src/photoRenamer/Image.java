package photoRenamer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that refers to the current selected image and its name file.
 */
public class Image {
	
	/** The selected image from a directory. */
	public File image;
	
	/** The text file that contains all name this image has been given. */
	private File file;
	
	/** The array form of all the names the image has been given. */
	private ArrayList<String> name;

	private static final Logger logger = Logger.getLogger(Image.class.getName());
	
	/**
	 * A new image with the file its referring to and the name history text file.
	 * 
	 * If filePath refers to a file that does not exists, then make a text file in the 
	 * directory of the image.
	 * 
	 * @param image
	 * 			the image selected from a directory
	 * @param textFilePath
	 * 			the file path of a text file that may or may not exists
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Image(File image, String textFilePath) throws ClassNotFoundException, IOException {
		this.image = image;
		this.name = new ArrayList<String>();
		
		logger.setLevel(Level.WARNING);
        
        this.file = new File(textFilePath);
        if (this.file.exists() && this.file.length() != 0) {
            readFromFile(textFilePath);
        } else {
            this.file.createNewFile();
            this.name.add(this.image.getName());
        }
	}
	
	/**
	 * Reads name history file and create an array form of it.
	 * 
	 * @param path
	 * 			path of the file from directory
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private void readFromFile(String path) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            this.name = (ArrayList<String>) input.readObject();
            input.close();
        } 
        catch (IOException ex) {
        	logger.log(Level.FINE, "Cannot read from input.", ex);
        }    

    }
	
	/**
	 * Writes the content of the array form of name history onto
	 * the file of the name history of the image.
	 * 
	 * @param filePath
	 * 			path to the text file found in the directory
	 * 
	 * @throws IOException
	 */
	public void saveToFile(String filePath) throws IOException {

        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(this.name);
        output.close();
    }
	
	/**
	 * Renames the image with a given name by the user and updates the name
	 * history. If the new name is not found in the name history, the new name
	 * will be added to the array form.
	 * 
	 * If the given name does not include the extension, then add default
	 * extension.
	 * 
	 * @param name
	 * 			given name from the user
	 * 
	 * @throws IOException
	 */
	public void rename(String name) throws IOException {
		String imagePath = this.image.getPath().split(this.image.getName())[0];
		imagePath += name;
		if (imagePath.endsWith(".jpg") == false) {
			imagePath += ".jpg";
		}
		File newName = new File(imagePath);
		logger.log(Level.WARNING,"renamed to: " + name + ".jpg");

		if (this.name.contains(newName.getName()) == false) {
			this.name.add(newName.getName());
			this.saveToFile(this.file.toString());
		}
		File newText = new File(imagePath.replace(".jpg", ".txt"));
		this.changeName(newText, newName);
	}
	
	/**
	 * Adds tags to the name of the file and updates the name 
	 * history if new name with tags is not found in the array
	 * form.
	 *
	 * Only tags that do not already exists in the name of the
	 * file will be added.
	 *  
	 * @param tags
	 * 			list of tags the user wants to add
	 * 
	 * @throws IOException
	 */
	public void addTag(String[] tags) throws IOException {
		String imagePath = this.image.getPath().split(".jpg")[0];
		String fullTag = "";
		for (String x: tags) {
			if (imagePath.contains(x) == false) {
				fullTag += x;
			}
		}
		File newName = new File(imagePath + fullTag + ".jpg");
		File newText = new File(imagePath + fullTag + ".txt");
		logger.log(Level.WARNING,"tag(s) added: " + fullTag);

		if (this.name.contains(newName.getName()) == false){
			this.name.add(newName.getName());
			this.saveToFile(this.file.toString());
		}
		this.changeName(newText, newName);
	}
	
	/**
	 * Removes tags from the name of the file and updates name
	 * history if new name with removed tags is not found in
	 * the array form.
	 * 
	 * @param tags
	 * 			list of tags the user wants to remove.
	 * 
	 * @throws IOException
	 */
	public void removeTag(String[] tags) throws IOException {
		String imagePath = this.image.getPath().split(".jpg")[0];
		String fullTag = "";
		for (String x: tags) {
			fullTag +=  x;
			if (imagePath.contains(x)) {
				imagePath = imagePath.replace(x, "");
			}
		}
		File newName = new File(imagePath + ".jpg");
		File newText = new File(imagePath + ".txt");
		logger.log(Level.WARNING, "tag removed: " + fullTag);
		if (this.name.contains(newName.getName()) == false){
			this.name.add(newName.getName());
			this.saveToFile(this.file.toString());
		}
		this.changeName(newText, newName);
	}
	
	/**
	 * Rename current working image with new name
	 * @param newText
	 * 				New name for text file.
	 * @param newName
	 * 				New name for image file.
	 */
	public void changeName(File newText, File newName){
		this.file.renameTo(newText);
		this.file = newText;
		this.image.renameTo(newName);
		this.image = newName;
	}
	/**
	 * gets all the names of the image.
	 * 
	 * @return The ArrayList name.
	 */
	public ArrayList<String> getNames(){
		return this.name;
	}
	
	/**
	 * Get name of image.
	 * @return The name of the image.
	 */
	public String getImageName(){
		return this.image.getName();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException{
		//Question 4
		/*JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(true);
		fileChooser.showOpenDialog(null);
		File pic = fileChooser.getSelectedFile();
		String imageText = pic.getPath().split(".jpg")[0] + ".txt";
		Image q2 = new Image(pic, imageText);
		System.out.println("Image Name: " + q2.image.getName());
		String[] tags = {"A", "B", "C"};
		q2.removeTag(tags);
		System.out.println("Image Name with Tags: " + q2.image.getName());*/
		//Question 6
		/*System.out.println("Name History: " + q2.getNames());
		String name = "CSC207";
		q2.rename(name);
		System.out.println("New Name: " + q2.image.getName());
		System.out.println("New Name History: " + q2.getNames());*/
	}
}
