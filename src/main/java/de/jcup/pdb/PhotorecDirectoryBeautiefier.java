package de.jcup.pdb;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

public class PhotorecDirectoryBeautiefier {

	private static final Logger LOG = LoggerFactory.getLogger(PhotorecDirectoryBeautiefier.class);

	public static void main(String[] argv) throws Exception {
		Args args = new Args();
		JCommander jc = JCommander.newBuilder().addObject(args).programName("pdb").build();
		jc.parse(argv);
		if (args.isHelp()) {
			System.out.println("Photorec directory beautifier");
			System.out.println("Please visit https://github.com/de-jcup/photorec-directory-beautifier");
			jc.usage();
			return;
		}
		new PhotorecDirectoryBeautiefier().execute(args);
	}

	// https://github.com/apache/commons-imaging/blob/master/src/test/java/org/apache/commons/imaging/examples/MetadataExample.java
	private void execute(Args args) throws Exception {
		LOG.info("initializing");
		File sourceRootFolder = args.getSource();
		if (!sourceRootFolder.exists()) {
			LOG.error("Source directory does not exist:{}", sourceRootFolder);
			System.exit(1);
		}
		if (!sourceRootFolder.isDirectory()) {
			LOG.error("Source not a directory:{}", sourceRootFolder);
			System.exit(1);
		}
		File targetRootFolder = args.getTarget();
		if (targetRootFolder.exists()) {
			LOG.error("Target directory does already exist:{}", targetRootFolder);
			System.exit(2);
		}

		Context c = new Context(sourceRootFolder, targetRootFolder);
		LOG.info("start counting");
		countFileOrFolder(c);
		LOG.info("counted, all files:{}", c.allFiles);
		inspectFileOrFolder(c);

	}

	private void countFileOrFolder(Context context) {
		countFileOrFolder(context, context.sourceRootFolder);
	}

	private void countFileOrFolder(Context context, File source) {
		if (source.isDirectory()) {
			for (File file : source.listFiles()) {
				countFileOrFolder(context, file);
			}
		} else {
			context.allFiles++;
		}

	}

	private void inspectFileOrFolder(Context context) {
		inspectFileOrFolder(context, context.sourceRootFolder);
	}

	private void inspectFileOrFolder(Context context, File source) {
		if (source.isDirectory()) {
			for (File file : source.listFiles()) {
				inspectFileOrFolder(context, file);
			}
		} else {
			copySafeFile(context, source);
		}

	}

	private void copySafeFile(Context context, File source) {
		File target = null;
		try {
			target = context.builder.build(source);
			target.getParentFile().mkdirs();

			context.handledFiles++;
			System.out.println(context.describeProgress(target));
			FileUtils.copyFile(source, target);
		} catch (Exception e) {
			LOG.error("Copy failed for source:{}, target:{}, exception:{}",source, target,e);
		}
	}

}
