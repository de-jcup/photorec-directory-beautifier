package de.jcup.pdb;

import java.io.File;

class Context {

	TargetFilePathBuilder builder;

	long allFiles;
	long handledFiles;

	File sourceRootFolder;
	private long start;

	Context(File sourceRootFolder, File targetRootFolder) {
		this.sourceRootFolder = sourceRootFolder;
		builder = new TargetFilePathBuilder();
		builder.setTargetRooFolder(targetRootFolder);
		start=System.currentTimeMillis();
	}

	public String describeProgress(File file) {
		StringBuilder sb = new StringBuilder();
		StringBuilder estimate = new StringBuilder();
		if (allFiles <= 0) {
			sb.append("?");
		} else {
			long onePercent = allFiles / 100;
			long percentage = handledFiles / onePercent;
			sb.append(percentage);
			if (handledFiles>0) {
				long handledFilesTimeInMs = System.currentTimeMillis()-start;
				
				long handledOneFileTimeInMs= handledFilesTimeInMs/handledFiles;
				long toGoInMilliseconds=handledOneFileTimeInMs*(allFiles-handledFiles);
				
				int seconds = (int) (toGoInMilliseconds/1000);
				int minutes = seconds/60;
				int hours = minutes/60;
				/* reduce */
				if (minutes > 0) {
					seconds = seconds-(minutes*60);
				}
				if (hours > 0) {
					minutes = minutes-(hours*60);
				}
				if (hours>0) {
					estimate.append(hours).append(" h ");
				}
				if (minutes>0) {
					estimate.append(minutes).append(" m ");
				}
				if (minutes<=0 && hours <=0) {
					/* show only when not hours and minutes...*/
					estimate.append(seconds).append(" s ");
				}
				
			}
			
		}
		sb.append("% done. Processing:");
		sb.append(file.getAbsolutePath());
		sb.append(" - estimated time:");
		sb.append(estimate.toString());
		sb.append(" - file:").append(handledFiles).append(" of:").append(allFiles);
		return sb.toString();
	}

}