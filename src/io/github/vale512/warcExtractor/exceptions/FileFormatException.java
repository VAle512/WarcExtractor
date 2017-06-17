package io.github.vale512.warcExtractor.exceptions;

import java.io.IOException;

public class FileFormatException extends IOException {

	private static final long serialVersionUID = 1L;

	public FileFormatException(String msg) {
		super(msg);
	}
}
