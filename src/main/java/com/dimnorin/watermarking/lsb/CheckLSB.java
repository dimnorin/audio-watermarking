package com.dimnorin.watermarking.lsb;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class CheckLSB {
	private File	file;
	private String	version;
	private byte	features;
	private short	compressionRatio;
	private int		dataLength, temp;

	private byte	byteArray[], name[], byte1, byte2;
	private int		inputMarker, i, j;

	public File getFile() {
		return file;
	}

	public int getInputMarker() {
		return inputMarker;
	}

	public String getVersion() {
		return version;
	}

	public byte getFeatures() {
		return features;
	}

	public short getCompressionRatio() {
		return compressionRatio;
	}

	public int getDataLength() {
		return dataLength;
	}

	private void retrieveBytes(byte[] bytes, byte[] array, int marker) {
		byteArray = array;
		inputMarker = marker;

		int size = bytes.length;

		for (i = 0; i < size; i++) {
			byte1 = 0;
			for (j = 6; j >= 0; j -= 2) {
				byte2 = byteArray[inputMarker];
				inputMarker++;

				byte2 &= 0x03;
				byte2 <<= j;
				byte1 |= byte2;
			}
			bytes[i] = byte1;
		}
	}

	private void readBytes(byte[] bytes, byte[] array, int marker) {
		byteArray = array;
		inputMarker = marker;

		int size = bytes.length;

		for (i = 0; i < size; i++) {
			bytes[i] = byteArray[inputMarker];
			inputMarker++;
		}
	}

	private void readBytes(byte[] bytes) {
		int size = bytes.length;

		for (i = 0; i < size; i++) {
			bytes[i] = byteArray[inputMarker];
			inputMarker++;
		}
	}

	public static char[] byteToCharArray(byte[] bytes) {
		int size = bytes.length, i;
		char[] chars = new char[size];
		for (i = 0; i < size; i++) {
			bytes[i] &= 0x7F;
			chars[i] = (char) bytes[i];
		}
		return chars;
	}

	public boolean check(File inputFile) {
		this.file = inputFile;

		if (!inputFile.exists()) {
			System.err.println("Input file "+inputFile.getAbsolutePath()+" does not exist!");
			return false;
		}

		byteArray = new byte[(int) inputFile.length()];
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(inputFile));
			in.read(byteArray, 0, (int) inputFile.length());
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		// Obtain the original file length
		name = new byte[4];

		inputMarker = LSB.OFFSET_WAVE;

		retrieveBytes(name, byteArray, inputMarker);
		dataLength = 0;
		for (i = 24, j = 0; i >= 0; i -= 8, j++) {
			temp = name[j];
			temp &= 0x000000FF;
			temp <<= i;
			dataLength |= temp;
		}
		inputMarker = dataLength;

		if (dataLength < 0 || dataLength > inputFile.length()) {
			return false;
		}

		// Retrive the name and version information
		byte versionArray[] = new byte[3];
		readBytes(versionArray, byteArray, inputMarker);
		char[] versionTemp = byteToCharArray(versionArray);
		char[] ver = new char[5];
		for (i = 0, j = 0; i < 5; i++)
			if (i == 1 || i == 3)
				ver[i] = '.';
			else {
				ver[i] = versionTemp[j++];
			}

		version = new String(ver);

		// Obtain the features
		name = new byte[1];
		readBytes(name);
		features = name[0];

		// Obtain the compression ratio
		readBytes(name);
		name[0] &= 0x7F;
		compressionRatio = name[0];

		// Obtain the data length
		name = new byte[4];
		readBytes(name);
		dataLength = 0;
		for (i = 24, j = 0; i >= 0; i -= 8, j++) {
			temp = name[j];
			temp &= 0x000000FF;
			temp <<= i;
			dataLength |= temp;
		}
		
		return true;
	}
}