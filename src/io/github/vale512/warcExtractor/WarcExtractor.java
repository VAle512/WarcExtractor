/**
 * Copyright 2017 Valerio Cetorelli (valerio_cetorelli@yahoo.it)
 * 
 * This file is part of WarcExtractor.
 *
 *  WarcExtractor is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   WarcExtractor is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with WarcExtractor.  If not, see <http://www.gnu.org/licenses/>
 *   
 *   @author Valerio Cetorelli (valerio_cetorelli@yahoo.it)
 */
package io.github.vale512.warcExtractor;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.lemurproject.galago.core.parse.WARCRecord;

import io.github.vale512.warcExtractor.util.CommandLineParser;

//TODO Find a way to clean HTML from WARC information.

public class WarcExtractor {

	public int extract(File warcFile,String outputDir) {
		DataInputStream inStream = this.gzipStream(warcFile);
		WARCRecord record;
		int i = 0;
		try {
			while ((record = WARCRecord.readNextWarcRecord(inStream)) != null) {
				File file = new File(outputDir, i+".html");
				file.createNewFile();
				FileWriter writer = new FileWriter(file);
				writer.write(record.getContentUTF8());
				writer.close();
				i++;
			}
		} catch (IOException e) {
			System.out.println("Error while reading "+warcFile.getName());
			e.printStackTrace();
		}
		return i;
	}

	private DataInputStream gzipStream(File file) {
		try {
			return new DataInputStream(new GZIPInputStream(new FileInputStream(file)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void createOutputDir(String path){
		File directory = new File(path);
		if(directory.exists()) {
			System.out.println("[ERROR]Directory "+path+" already exists.");
			System.exit(1);
		}
		directory.mkdirs();
	}
	
	public static void main(String[] argc) {
		Map<String,String> inputArgs = CommandLineParser.parse(argc);
		File inputFile = new File(inputArgs.get(CommandLineParser.INPUT_FILE));
		if(!inputFile.exists()) {
			System.out.println("[ERROR]File "+inputFile.getName()+" doesn't exist.");
			System.exit(1);
		}
		String outputPath = inputArgs.get(CommandLineParser.OUTPUT_DIR);
		createOutputDir(outputPath);
		int extractedFiles = new WarcExtractor().extract(inputFile, outputPath);
		System.out.println("Files extracted: "+extractedFiles);
	}

}

