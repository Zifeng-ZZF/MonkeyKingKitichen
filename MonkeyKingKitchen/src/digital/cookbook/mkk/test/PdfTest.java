package digital.cookbook.mkk.test;

import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.PdfProcess;
import digital.cookbook.mkk.Recipe;
import junit.framework.TestCase;

public class PdfTest extends TestCase {
	
	DBProcessor dbProcessor;
	Recipe testRecipe;
	PdfProcess pdfProcess;

	@BeforeEach
	protected void setUp() throws Exception {
		super.setUp();
		dbProcessor = new DBProcessor();
		testRecipe = dbProcessor.fetchRecipe().get(1);
		pdfProcess = new PdfProcess();
	}

	/**
	 * Test whether the pdf has been exported to the location we expected
	 */
	@Test
	public void testExportPDF() {
		pdfProcess.exportPDF(testRecipe, "C:/users/user/desktop/testRecipe.pdf");
		File file = new File("C:/users/user/desktop/testRecipe.pdf");
		
		assertTrue(file.exists());
	}

}
