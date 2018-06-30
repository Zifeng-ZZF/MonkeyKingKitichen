package digital.cookbook.mkk.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.Ingredient;
import digital.cookbook.mkk.Recipe;
import junit.framework.TestCase;

public class RecipeTest extends TestCase {
	
	DBProcessor dbProcessor;
	Recipe testRecipe;
	Ingredient testIngredient;

	@BeforeEach
	public void setUp() throws Exception {
		super.setUp();
		dbProcessor = new DBProcessor();
		testRecipe = dbProcessor.fetchRecipe().get(1);
	}

	/**
	 * Test whether the addIngredient(Ingredient ingredient) is working
	 * Compare the size of the arraylist for ingredients in each recipe 
	 * before and after the function
	 */
	@Test
	public void testAddIngredient() {
		int sizeBefore = testRecipe.getIngredients().size();
		testRecipe.addIngredient(new Ingredient("salt", 2, "teaspoons"));
		int sizeAfter = testRecipe.getIngredients().size();
		assertEquals(1, sizeAfter-sizeBefore);
	}

	/**
	 * Compare the size of the arraylist for ingredients in each recipe 
	 * before and after the function
	 */
	@Test
	public void testDeleteIngredient() {
		testIngredient = testRecipe.getIngredients().get(1);
		int sizeBefore = testRecipe.getIngredients().size();
		testRecipe.deleteIngredient(testIngredient);
		int sizeAfter = testRecipe.getIngredients().size();
		assertEquals(-1, sizeAfter-sizeBefore);
	}

	/**
	 * Test whether the addPreparationStep(String preparationSetp) is working
	 * Compare the size of the arraylist for steps in each recipe 
	 * before and after the function
	 */
	@Test
	public void testAddPreparationStep() {
		int sizeBefore = testRecipe.getPreparationSetps().size();
		testRecipe.addPreparationStep("Fry an apple!");
		int sizeAfter = testRecipe.getPreparationSetps().size();
		assertEquals(1, sizeAfter-sizeBefore);
	}

	/**
	 * Test the whether the toString() has been override as expecting pattern
	 */
	@Test
	public void testToString() {
		System.out.println(testRecipe.toString());
	}

}
