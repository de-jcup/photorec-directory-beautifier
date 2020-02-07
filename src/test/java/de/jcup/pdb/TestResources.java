package de.jcup.pdb;

import java.io.File;

public class TestResources {

	public static File getTestResourceFolder() {
		File file = new File("./src/test/resources");
		if (!file.exists()) {
			throw new IllegalStateException("Cannot access:" + file);
		}
		return file;
	}
}
