package de.jcup.pdb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class JPGDateTimeOriginToDateConverter {

	private static Pattern replaceSingleQuotes = Pattern.compile("'");
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

	public Date convert(String string) {
//		'2014:09:04 15:14:08'
		try {
			String data = replaceSingleQuotes.matcher(string).replaceAll("");
			return format.parse(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
