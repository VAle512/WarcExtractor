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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.lemurproject.galago.core.parse.WARCRecord;

import io.github.vale512.warcExtractor.exceptions.DirectoryAlreadyExistsException;
import io.github.vale512.warcExtractor.exceptions.FileFormatException;
import io.github.vale512.warcExtractor.util.CommandLineParser;

//TODO Find a way to clean HTML from WARC information.

public class WarcExtractor {

	public int extract(DataInputStream inputStream,String outputDir) throws IOException {
		WARCRecord record;
		int i = 0;
		System.out.println("Extraction in progress, please wait...");
		while ((record = WARCRecord.readNextWarcRecord(inputStream)) != null) {
			File file = new File(outputDir, i+".html");
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(record.getContentUTF8());
			writer.close();
			i++;
		}
		return i;
	}

	private DataInputStream createInputStream(String path) throws IOException {
		File file = new File(path);
		if(!file.exists()) {
			throw new FileNotFoundException("File "+file.getName()+" doesn't exist.");
		}
		String extension = path.substring(path.lastIndexOf("."));
		switch (extension) {
		case ".gz":
			return new DataInputStream(new GZIPInputStream(new FileInputStream(file)));
		case ".warc":
			return new DataInputStream(new FileInputStream(file));
		default:
			throw new FileFormatException("File extension must be .warc or .warc.gz");
		}
	}

	private void createOutputDir(String path) throws DirectoryAlreadyExistsException{
		File directory = new File(path);
		if(directory.exists())
			throw new DirectoryAlreadyExistsException("Directory "+directory.getName()+" alreay exists.");
		directory.mkdirs();
	}

	private int run(String inputFilePath, String outputPath) throws IOException {
		DataInputStream inputStream = this.createInputStream(inputFilePath);
		this.createOutputDir(outputPath);
		return this.extract(inputStream, outputPath);
	}

	public static void main(String[] argc)  {
		Map<String,String> inputArgs = CommandLineParser.parse(argc);
		String inputFilePath = inputArgs.get(CommandLineParser.INPUT_FILE);
		String outputPath = inputArgs.get(CommandLineParser.OUTPUT_DIR);
		int extractedFiles = 0;
		try {
			extractedFiles = new WarcExtractor().run(inputFilePath, outputPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Files extracted: "+extractedFiles);
	}

}
