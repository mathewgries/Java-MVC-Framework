package com.freedompay.data;

import com.freedompay.models.FileModel;
import com.freedompay.util.FileType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileData {
	private static List<FileModel> fileModels = new ArrayList<FileModel>();
	private static boolean isPOSLoaded = false;
	
	public static void setIsPOSLoadedFlag() {
		FileData.isPOSLoaded = FileData.isPOSLoaded ? false : true;
	}
	
	public static boolean getIsPOSLoadedFlag() {
		return FileData.isPOSLoaded;
	}
	
	//======================= SAVE MODEL =========================
	
	public static void saveModel(FileModel f) {
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
	
	public static void deleteModel(String name) {
		Iterator<FileModel> models = FileData.getAllFileModels().iterator();
		while(models.hasNext()) {
			if(models.next().getName().equalsIgnoreCase(name)) {
				models.remove();
			}
		}
	}
	
	public static void deleteModel(FileModel fm) {
		Iterator<FileModel> models = FileData.getAllFileModels().iterator();
		while(models.hasNext()) {
			if(models.next().equals(fm)) {
				models.remove();
			}
		}
	}
	
	public static void deleteModel(FileType type) {
		Iterator<FileModel> models = FileData.getAllFileModels().iterator();
		while(models.hasNext()) {
			if(models.next().getFileType() == type) {
				models.remove();
			}
		}
	}
	
	//======================= GET FILE MODEL =========================
	
	public static FileModel getFileModel(String name) {
		for(FileModel model : FileData.fileModels) {
			if(model.getName().equalsIgnoreCase(name)) {
				return model;
			}
		}
		return null;
	}
	
	public static FileModel getFileModel(FileType type) {
		for(FileModel model : FileData.fileModels) {
			if(model.getFileType() == type) {
				return model;
			}
		}
		return null;
	}
	
	public static FileModel getFileModel(int id) {
		for(FileModel model : FileData.fileModels) {
			if(model.getId() == id) {
				return model;
			}
		}
		return null;
	}
	
	//================ GET ALL FILE MODELS ===================
	
	public static List<FileModel> getAllFileModels(){
		return FileData.fileModels;
	}
	
	public static int getFileCount() {
		return FileData.fileModels.size();
	}
}
