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

public class Tags {
	/** The file that stores all the tags used*/
	private File tagFile;
	/** The arrayList that stores all the tags from the tags file*/
	private ArrayList<String> tags;
	
	private static final Logger logger = Logger.getLogger(Image.class.getName());
	
	/**Implemented Singleton design pattern to ensure that the user can only obtain one instance
	 * of tags. This prevents user from attempting to read another tag file after selecting the directory.
	 */
	
	/** The single instance of Tags*/
	private static Tags instance;
	static{
		try {
			instance = new Tags();
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/**
	 * Creates a new Tags instance.
	 * Creates a new tags text file if one does not exists already.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Tags() throws IOException, ClassNotFoundException {
		this.tags = new ArrayList<String>();
		// Associate the handler with the logger.
		logger.setLevel(Level.WARNING);
		
        // creates a string of the working directory where the source code originates
		String workingDir = System.getProperty("user.dir");
        workingDir += "\\src\\photoRenamer\\Tags.txt";
        
        // Reads serializable objects from file.
        // Populates the record list using stored data, if it exists.
        this.tagFile = new File(workingDir);
        if (this.tagFile.exists() && this.tagFile.length() != 0) {
            readFromFile(workingDir);
        } else {
            this.tagFile.createNewFile();
        }
	}
	
	/**
	 * Method that allows the user to obtain an instance of this class.
	 * 
	 * @return The single instance of Tags
	 */
	public static Tags getInstance(){
		return instance;
	}
	
	/**
	 * Reads the ArrayList containing tags from the file in the path received.
	 * 
	 * @param path 
	 * 				The path of the text file that stores tags.
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private void readFromFile(String path) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            
            //deserialize the Map
            this.tags = (ArrayList<String>) input.readObject();
            input.close();
        } 
        catch (IOException ex) {
        	logger.log(Level.FINE, "Cannot read from input.", ex);
        }    

    }
	
	/**
	 * Saves the current ArrayList of directory class to text file that stores tags.
	 * 
	 * @param filePath 
	 * 				The path of the text file that stores tags.
	 * @throws IOException
	 */
	public void saveToFile(String filePath) throws IOException {

        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);
        
        // serialize the Map
        output.writeObject(this.tags);
        output.close();
    }
	
	/**
	 * Return all usable tags.
	 * 
	 * @return The ArrayList of all usable tags.
	 */
	public ArrayList<String> getAllTagsUsed(){
		return this.tags;
	}
	
	/**
	 * Adds new tags to the ArrayList of tags and updates the text file.
	 * 
	 * @param tags
	 * 				The tags to add to the list of usable tags.
	 * @throws IOException
	 */
	public void updateTags(String[] tags) throws IOException {
		for (String x: tags) {
			if (x.length() > 0 && x.substring(0, 1).equals("@") == false){
				x = "@" + x;
			}
			if (this.tags.contains(x) == false) {
				this.tags.add(x);
				this.saveToFile(this.tagFile.toString());
			}
		}
	}
	

	
	/**
	 * Deletes certain tags from the list of usable tags.
	 * 
	 * @param tags
	 * 				The tags to delete from the list of usable tags.
	 * @throws IOException
	 */
	public void deleteTags(String[] tags) throws IOException {
        for (String x: tags) {
            if (this.tags.contains(x)) {
                this.tags.remove(x);
                this.saveToFile(this.tagFile.toString());
            }
        }
    }
}
