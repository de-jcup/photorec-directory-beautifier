package de.jcup.pdb;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Will do following: File abc.jpg with date
 * <ul>
 * <li>year: 2019</li>
 * <li>month: 12</li>
 * <li>day:6</li>
 * <li>hour:14</li>
 * <li>minute:12:</li>
 * <li>seconds 7</li>
 * </ul>
 * and having target source folder: `/home/albert/target'<br>
 * <br>
 * results in: 'home/albert/target/2019/12/2019-12-06_14-12-07.jpg`. If this
 * file already exists an alternative will be
 * used:'home/albert/target/2019/12/2019-12-06_14-12-07_1.jpg` and so on...
 * 
 * 
 * @author albert
 *
 */
public class TargetFilePathBuilder {
	private SimpleDateFormat outputDateFormat;
	private JPGDateTimeOriginToDateConverter dateConverter;
	private File targetRooFolder;
	private Calendar calendar;

	private static final Logger LOG = LoggerFactory.getLogger(TargetFilePathBuilder.class);

	public TargetFilePathBuilder() {
		dateConverter = new JPGDateTimeOriginToDateConverter();
		outputDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		calendar = Calendar.getInstance();
	}

	public TargetFilePathBuilder setTargetRooFolder(File targetRootFolder) {
		this.targetRooFolder = targetRootFolder;
		return this;
	}

	public File build(File file) {
		if (file == null) {
			throw new IllegalArgumentException("File param may not be null!");
		}
		if (!file.exists()) {
			throw new IllegalArgumentException("File does not exist:" + file.getAbsolutePath());
		}
		ImageMetadata metadata = null;
		try {
			metadata = Imaging.getMetadata(file);
		} catch (ImageReadException | IOException e) {
		}
		Date date = null;
		if (metadata instanceof JpegImageMetadata) {
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			String dateTime = getOriginDateTime(jpegMetadata);
			date = dateConverter.convert(dateTime);
		}
		if (date == null) {
			LOG.debug("Fallback to last modified for file {}", file);
			date = new Date(file.lastModified());
		}

		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		month++; // we start with 1 and not 0...
		String monthPrefix = "";
		if (month < 10) {
			monthPrefix = "0";
		}
		String extensionUppercase = FilenameUtils.getExtension(file.getAbsolutePath()).toUpperCase();
		File yearMonthTargetFolder = new File(targetRooFolder,
				extensionUppercase + "/" + year + "/" + monthPrefix + month);
		String fileNameWithoutEnding = outputDateFormat.format(date);
		File resultFile = null;
		int count = 0;
		do {
			resultFile = new File(yearMonthTargetFolder, createFileName(count++, fileNameWithoutEnding, file));
		} while (resultFile.exists());

		return resultFile;
	}

	private String createFileName(int count, String part, File originFile) {
		StringBuilder sb = new StringBuilder();
		sb.append(part);
		if (count > 0) {
			sb.append("_");
			sb.append(count);
		}
		sb.append(".");
		sb.append(FilenameUtils.getExtension(originFile.getAbsolutePath()).toLowerCase());
		return sb.toString();

	}

	private String getOriginDateTime(final JpegImageMetadata jpegMetadata) {
		return getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
	}

	private String getTagValue(final JpegImageMetadata jpegMetadata, final TagInfo tagInfo) {
		final TiffField field = jpegMetadata.findEXIFValueWithExactMatch(tagInfo);
		if (field == null) {
			return null;
		} else {
			return field.getValueDescription();
		}
	}
}
