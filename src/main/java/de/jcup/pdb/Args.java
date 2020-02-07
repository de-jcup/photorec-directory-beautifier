package de.jcup.pdb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

public class Args {
	@Parameter
	private List<String> parameters = new ArrayList<>();

	@Parameter(names = { "--source",
			"-s" }, description = "source directory", converter = FileConverter.class, required = true)
	private File source;

	@Parameter(names = { "--target",
			"-t" }, description = "target directory", converter = FileConverter.class, required = true)
	private File target;

	@Parameter(names = "--help", help = true)
	private boolean help;

	public boolean isHelp() {
		return help;
	}

	public File getTarget() {
		return target;
	}

	public File getSource() {
		return source;
	}
}