package com.dimnorin.watermarking;

import java.io.File;

import com.dimnorin.watermarking.lsb.CheckLSB;
import com.dimnorin.watermarking.lsb.LSB;
import com.dimnorin.watermarking.utils.CommonUtils;

public class Watermarker {
	private static final String KEY_W 	= "-w";
	private static final String KEY_C 	= "-c";
	
	private String key;
	private String msg;
	private String password;
	private File inputFile;
	private File outputFile;
	/**
	 * Start program from here
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Java Audio Water Marking Utility v."+LSB.VERSION);
		if(checkInput(args)){
			new Watermarker(args).start();
		}
	}
	/**
	 * Init parameters
	 * @param args
	 */
	public Watermarker(String[] args){
		this.key = args[0];
		this.inputFile = new File(args[1]);
		// Create output file name
		this.outputFile = CommonUtils.newFileName(inputFile, "-watermark");
		
		this.msg = args[2];
		this.password = args[3];
	}
	/**
	 * Start water marking routine depending from flags
	 */
	public void start(){
		LSB lsb = new LSB();
		switch (key) {
		case KEY_W:
			boolean result = lsb.embedMessage(inputFile, outputFile, msg, -1, password);
			System.out.println("Watermarking complete!");
			break;

		case KEY_C:
			CheckLSB checkLSB = new CheckLSB();
			result = checkLSB.check(inputFile);
			if(!result){
				showResult(result);
				return;
			}
			String msg = lsb.retrieveMessage(checkLSB, password);
			showResult(this.msg.equals(msg));
			break;
		}
	}
	
	private void showResult(boolean result){
		System.out.println(String.format("Does %s contains watermark: %b", inputFile.getName(), result));
	}
	
	private static boolean checkInput(String[] args){
		if(args.length < 4){
			showUsage();
			return false;
		}else if(!KEY_W.equals(args[0]) && !KEY_C.equals(args[0])){
			showUsage();
			return false;
		}
		return true;
	}
	
	private static void showUsage(){
		System.out.println("Usage: java -jar watermark.jar [-w] [-c] input_file water_mark_msg password \n");
		System.out.println("Options:");
		System.out.println("\t -w\t\t\t Watermarking (input_file).");
		System.out.println("\t -c\t\t\t Check if (input_file) was watermarked.");
	}
}
