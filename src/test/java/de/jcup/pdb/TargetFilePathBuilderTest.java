package de.jcup.pdb;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class TargetFilePathBuilderTest {

	private static final File TARGET_ROOT_FOLDER = new File("/home/albert/target");
	private TargetFilePathBuilder builderToTest;
	
	@Before
	public void before() {
		builderToTest = new TargetFilePathBuilder();
		builderToTest.setTargetRooFolder(TARGET_ROOT_FOLDER);
	}
	
	@Test
	public void f0354144_has_filename_containing_year_and_month_as_subfolders_and_name_with_fulldate_data() {
		/* prepare */
		File source = new File(TestResources.getTestResourceFolder(),"f0354144.jpg");
		File expectedFile = new File(TARGET_ROOT_FOLDER,"JPG/2014/09/2014-09-04_15-14-08.jpg");
		
		/* execute */
		File result = builderToTest.build(source);
		
		/* test */
		assertEquals(expectedFile.toString(),result.toString());
		assertEquals(expectedFile,result);
		
	}
	
	@Test
	public void f1314456_has_filename_containing_year_and_month_as_subfolders_and_name_with_fulldate_data() {
		/* prepare */
		File source = new File(TestResources.getTestResourceFolder(),"f1314456.jpg");
		File expectedFile = new File(TARGET_ROOT_FOLDER,"JPG/2015/05/2015-05-24_12-04-55.jpg");

		/* execute */
		File result = builderToTest.build(source);
		
		/* test */
		assertEquals(expectedFile.toString(),result.toString());
		assertEquals(expectedFile,result);
		
	}

}
