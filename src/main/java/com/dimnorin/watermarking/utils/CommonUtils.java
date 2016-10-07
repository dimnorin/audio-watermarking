package com.dimnorin.watermarking.utils;

import java.io.File;

public class CommonUtils {
	public static File newFileName(File file, String name){
		String fileName = file.getName();
		String ext = getExtension(fileName);
		String absolutePath = file.getAbsolutePath();
		String parent = getParentFolder(absolutePath);
		
		return new File(parent, fileName+name+"."+ext);
	}
	/**
	 * Get extension from the fileName
	 * @param fileName given file name
	 * @return extension
	 */
	public static String getExtension(String fileName){
		String extension = "";

		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		if (i > p) {
		    extension = fileName.substring(i+1);
		}
		return extension;
	}
	/**
	 * Get parent folder for absolute path
	 * @param absolutePath given absolute path
	 * @return absolute parent folder
	 */
	public static String getParentFolder(String absolutePath){
		int p = Math.max(absolutePath.lastIndexOf('/'), absolutePath.lastIndexOf('\\'));
		
		return absolutePath.substring(0, p);
	}
}
