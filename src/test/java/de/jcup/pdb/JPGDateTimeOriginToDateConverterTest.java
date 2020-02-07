package de.jcup.pdb;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class JPGDateTimeOriginToDateConverterTest {
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
	private JPGDateTimeOriginToDateConverter converterToTest;

	@Before
	public void before() {
		converterToTest = new JPGDateTimeOriginToDateConverter();
	}
	
	@Test
	public void jpeg_result_string_2014_09_04__15_14_08() throws Exception{
		/* prepare */
		// date_time_original:'2014:09:04 15:14:08'
		Date dateExpected = format.parse("2014-09-04_15-14-08");
		
		/* execute */
		Date result = converterToTest.convert("'2014:09:04 15:14:08'");
		
		/* test */
		assertEquals(dateExpected, result);
		
	}
	
}
