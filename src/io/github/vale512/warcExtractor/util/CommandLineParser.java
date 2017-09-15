/**
 * Copyright 2017 Valerio Cetorelli (valerio_cetorelli@yahoo.it)
 * 
 * This file is part of WarcExtractor.
 *
 *   WarcExtractor is free software: you can redistribute it and/or modify
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
package io.github.vale512.warcExtractor.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineParser {

	public static final String INPUT_FILE = "file";
	public static final String OUTPUT_DIR = "outputDir";

	private final static Option HELP_OPTION = new Option("h", "help", false, "Print this message");
	private final static Option INPUT_FILE_OPTION = new Option("i", "input", true, "The WARC file to extract");
	private final static Option OUTPUT_DIR_OPTION = new Option("o", "output", true, "The output folder");

	private final static Options OPTIONS = new Options();

	public static Map<String,String> parse(String[] argc) {

		OPTIONS.addOption(HELP_OPTION);
		OPTIONS.addOption(INPUT_FILE_OPTION);
		OPTIONS.addOption(OUTPUT_DIR_OPTION);
		
		Map<String,String> args = new HashMap<>();
		DefaultParser parser = new DefaultParser();
		CommandLine line = null;
		
		try {
			line = parser.parse(OPTIONS, argc);
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(1);
		}

		if (line.hasOption(HELP_OPTION.getLongOpt()))
			ptintUsage(OPTIONS);
		if (!line.hasOption(INPUT_FILE_OPTION.getLongOpt())) {
			System.err.println("You must supply -i");
			ptintUsage(OPTIONS);
			System.exit(1);
		}
		if (!line.hasOption(OUTPUT_DIR_OPTION.getLongOpt())) {
			System.err.println("You must supply -o");
			ptintUsage(OPTIONS);
			System.exit(1);
		}

		args.put(INPUT_FILE, line.getOptionValue(INPUT_FILE_OPTION.getLongOpt()));
		args.put(OUTPUT_DIR, line.getOptionValue(OUTPUT_DIR_OPTION.getLongOpt()));

		return args;
	}

	private static void ptintUsage(Options options) {
		new HelpFormatter().printHelp("Warc Extractor", options, true);
		System.exit(0);
	}

}
