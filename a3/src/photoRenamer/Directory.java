package photoRenamer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * The directory that the user wants to work with. This class also
 * sorts out all of the jpg images in the directory.
 */
public class Directory {
	
	/** The directory being worked in*/
	public File directory;
	
	/**
	 * Creates a new Directory class with the directory being worked in.
	 * 
	 * @param directory 
	 * 				The directory that the user wants to work in.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Directory(File directory) {
		this.directory = directory;
	}
	
	/**
	 * Return only file ending in .JPG
	 * 
	 * @param directory 
	 * 				The directory the user wants to work with.
	 * @param images 
	 * 				The ArrayList containing all images in the working directory.
	 * @return An ArrayList of all file ending in .JPG.
	 */
	public ArrayList<File> getImages(File directory, ArrayList<File> images) {
		File[] items = directory.listFiles();
		for (File x: items) {
			if (x.isFile() && x.getName().endsWith(".jpg")) {
				images.add(x);
			} else if (x.isDirectory()) {
				images = this.getImages(x, images);
			}
		}
		return images;
	}
	
	/**
	 * Sorts all images for ones that contain certain tags.
	 * 
	 * @param tags
	 * 				An Array of tags to sort through all images.
	 * @return An ArrayList of all images containing certain tags
	 */
	public ArrayList<File> getSpecificImages(String[] tags) {
		ArrayList<File> specificImages = new ArrayList<>();
		ArrayList<File> images = new ArrayList<>();
		images = this.getImages(this.directory, images);
		for (File x: images) {
			for (String y: tags) {
				if (x.getName().contains(y) && !specificImages.contains(x)) {
					specificImages.add(x);
				}
			}
		}
		return specificImages;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		//Question 2
		/*JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.showOpenDialog(null);
		Directory q1 = new Directory(fileChooser.getSelectedFile());
		ArrayList<String> images = new ArrayList<>();
		images = q1.getImages(q1.directory, images);
		System.out.println("Images: " + images);
		String[] tags = {"A", "B", "C"};*/
		//Question 6
		/*q1.updateTags(tags);
		System.out.println("All Tags: " + q1.getAllTagsUsed());*/
		
	}
}
