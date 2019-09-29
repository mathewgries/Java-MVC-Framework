package com.freedompay.data;

/**
 * <p>
 * A repository for uploaded FileModels.
 * We do not run off a database, so this is our data storage.
 * This is destroyed when the program is closed.
 * </p>
 */
import com.freedompay.models.FileModel;
import com.freedompay.util.FileType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileData {
	// The list of FileModel objects
	private static List<FileModel> fileModels = new ArrayList<FileModel>();
	//Keeps track of whether the main file is uploaded
	private static boolean isPOSLoaded = false;
	
	/**
	 * <p>
	 * Set the pos flag true of false when the file is
	 * uploaded or removed
	 * </p>
	 */
	public static void setIsPOSLoadedFlag() {
		FileData.isPOSLoaded = FileData.isPOSLoaded ? false : true;
	}
	
	/**
	 * <p>Get the current state of the pos flag</p>
	 * @return isPOSloaded flag
	 */
	public static boolean getIsPOSLoadedFlag() {
		return FileData.isPOSLoaded;
	}
	
	//======================= SAVE MODEL =========================
	
	/**
	 * <p>
	 * The save method for storing the FileModel
	 * </p>
	 * @param f the file model to be saved
	 */
	public static void saveFileModel(FileModel f) {
		if(f.getFileType() == FileType.POS) {
			FileData.fileModels.add(0, f);
			FileData.setIsPOSLoadedFlag();
		}else if(FileData.getAllFileModels().size() > 1 && f.getFileType() == FileType.UNCAPTURED_AUTH) {
			FileData.fileModels.add(1, f);
		}else {
			FileData.fileModels.add(f);
		}
	}
	
	//======================= DELETE MODEL =========================
	
	/**
	 * <p>Delete the FileModel by given name value</p>
	 * @param name the file name
	 */
	public static void deleteFileModel(String name) {
		FileType type = null;
		Iterator<FileModel> models = FileData.getAllFileModels().iterator();
		while(models.hasNext()) {
			if(models.next().getName().equalsIgnoreCase(name)) {
				type = models.next().getFileType();
				models.remove();
			}
		}
		if(type == FileType.POS) {
			FileData.setIsPOSLoadedFlag();
		}
	}
	
	/**
	 * <p>Delete the FileModel by FileModel object value</p>
	 * @param fm The FileModel to be deleted
	 */
	public static void deleteFileModel(FileModel fm) {
		FileType type = null;
		Iterator<FileModel> models = FileData.getAllFileModels().iterator();
		while(models.hasNext()) {
			if(models.next().equals(fm)) {
				type = models.next().getFileType();
				models.remove();
			}
		}
		if(type == FileType.POS) {
			FileData.setIsPOSLoadedFlag();
		}
	}
	
	/**
	 * <p>
	 * Delete the FileModel by Enum FileType
	 * </p>
	 * @param type The Enum FileType
	 */
	public static void deleteFileModel(FileType type) {
		Iterator<FileModel> models = FileData.getAllFileModels().iterator();
		while(models.hasNext()) {
			if(models.next().getFileType() == type) {
				models.remove();
			}
		}
		if(type == FileType.POS) {
			FileData.setIsPOSLoadedFlag();
		}
	}
	
	//======================= GET FILE MODEL =========================
	
	/**
	 * <p>Return the FileModel by given name value</p>
	 * @param name The file name of the FileModel
	 * @return the FileModel
	 */
	public static FileModel getFileModel(String name) {
		for(FileModel model : FileData.fileModels) {
			if(model.getName().equalsIgnoreCase(name)) {
				return model;
			}
		}
		return null;
	}
	
	/**
	 * <p>
	 * Return the FileModel by Enum FileType
	 * </p>
	 * @param type The Enum FileType
	 * @return The FileModel
	 */
	public static FileModel getFileModel(FileType type) {
		for(FileModel model : FileData.fileModels) {
			if(model.getFileType() == type) {
				return model;
			}
		}
		return null;
	}
	
	/**
	 * <p>
	 * Return the FileModel by FileModel id
	 * </p>
	 * @param id The id of the FileModel
	 * @return The FileModel
	 */
	public static FileModel getFileModel(int id) {
		for(FileModel model : FileData.fileModels) {
			if(model.getId() == id) {
				return model;
			}
		}
		return null;
	}
	
	//================ GET ALL FILE MODELS ===================
	
	/**
	 * <p>
	 * Return the full list of FileModels
	 * </p>
	 * @return FileModel list
	 */
	public static List<FileModel> getAllFileModels(){
		return FileData.fileModels;
	}
	
	/**
	 * <p>Count of saved FileModels</p>
	 * @return FileModel list size
	 */
	public static int getFileCount() {
		return FileData.fileModels.size();
	}
}
