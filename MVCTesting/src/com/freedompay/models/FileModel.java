package com.freedompay.models;
import java.io.File;
import com.freedompay.util.FileType;

/**
 * <p>
 * The FileModel is the uploaded files. When uploaded, the model
 * is stored in the static FileData repository class.
 * </p>
 * @author MGries
 *
 */
public class FileModel extends Model {
	
	private static int lastId = 0;
	
	private int id;
	private FileType type;
	private File file;
	private String name;
	private String absolutePath;	
	private FileContents fileContents;
	
	public FileModel(File f, FileType type) {
		this.id = lastId++;							//Set the Id and increment lastId for next file
		this.file = f;								// The file being uploaded
		this.type = type;							// The enum value for file type: POS, UNCAPTURED_AUTH, CAPTURED
		this.name = f.getName();					// The name of the file with extension
		this.absolutePath = f.getAbsolutePath();	// The path of the file
		this.fileContents = new FileContents(this);
	}
	
	/**
	 * <p>The unique ID of the FileModel</p>
	 * @return FileModel id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * <p>
	 * The FileType Enum assigned to the file.
	 * </p>
	 * @return FileType Enum
	 */
	public FileType getFileType() {
		return this.type;
	}
	
	/**
	 * <p>
	 * The File object that was uploaded
	 * </p>
	 * @return The FileModel's File object
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * <p>
	 * The system name of the File
	 * </p>
	 * @return The File name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * <p>
	 * The path the file is located at on the system
	 * </p>
	 * @return The system file path
	 */
	public String getAbsolutePath() {
		return this.absolutePath;
	}
	
	/**
	 * <p>
	 * A class that contains the information on the
	 * actual file contents, such as file rows, headers,
	 * row and column count, and chosen columns to match against.
	 * </p>
	 * @return The FileContents class.
	 */
	public FileContents getFileContents() {
		return this.fileContents;
	}
}