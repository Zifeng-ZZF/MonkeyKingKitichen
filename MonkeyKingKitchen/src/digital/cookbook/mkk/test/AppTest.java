package digital.cookbook.mkk.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;
/**
 * Test suite
 * @author Zifeng Zhang, Zhixin Xin
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
	DBProcessorTest.class,
	PdfTest.class,
	RecipeTest.class
})

public class AppTest extends TestSuite {

}
