package com.freedompay.data;

import com.freedompay.models.FileModel;
import com.freedompay.util.FileType;

import java.util.ArrayList;
import java.util.List;

public class FileData {
	private static List<FileModel> files = new ArrayList<FileModel>();
	private static boolean isPOSLoaded = false;
	
	public static void setIsPOSLoadedFlag() {
		FileData.isPOSLoaded = FileData.isPOSLoaded ? false : true;
	}
	
	public static boolean getIsPOSLoadedFlag() {
		return FileData.isPOSLoaded;
	}
	
	public static void saveModel(FileModel f) {
		if(f.getFileType() == FileType.POS) {
			FileData.files.add(0, f);
			FileData.setIsPOSLoadedFlag();
		}else {
			FileData.files.add(f);
		}

	}
	
	public static void deleteModel(String name) {
		for(FileModel file : FileData.getAllFiles()) {
			if(file.getName().equalsIgnoreCase(name)) {
				FileData.files.remove(file);
				break;
			}
		}
	}
	
	public static void deleteModel(FileModel model) {
		for(FileModel file : FileData.getAllFiles()) {
			if(file.equals(model)) {
				if(model.getFileType() == FileType.POS) {
					FileData.setIsPOSLoadedFlag();
				}
				FileData.files.remove(model);
				break;
			}
		}
	}
	
	public static FileModel getFile(String name) {
		for(FileModel file : FileData.files) {
			if(file.getName().equalsIgnoreCase(name)) {
				return file;
			}
		}
		return null;
	}
	
	public static FileModel getFile(FileType type) {
		for(FileModel file : FileData.files) {
			if(file.getFileType() == type) {
				return file;
			}
		}
		return null;
	}
	
	public static FileModel getFile(int id) {
		for(FileModel model : FileData.files) {
			if(model.getId() == id) {
				return model;
			}
		}
		return null;
	}
	
	public static List<FileModel> getAllFiles(){
		return FileData.files;
	}
	
	public static int getFileCount() {
		return FileData.files.size();
	}
}
