package io.github.vale512.warcExtractor.exceptions;

import java.nio.file.FileAlreadyExistsException;

public class DirectoryAlreadyExistsException extends FileAlreadyExistsException {

	private static final long serialVersionUID = 1L;

	public DirectoryAlreadyExistsException(String msg) {
		super(msg);
	}
}
