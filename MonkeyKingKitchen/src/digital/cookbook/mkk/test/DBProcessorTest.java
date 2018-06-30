package digital.cookbook.mkk.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.Ingredient;
import digital.cookbook.mkk.Recipe;
import digital.cookbook.mkk.User;
import junit.framework.TestCase;

public class DBProcessorTest extends TestCase{
	
	DBProcessor dbProcessor = new DBProcessor();

	@BeforeEach
	protected void setUp() throws Exception {
	}

	/**
	 * test whether the method FetchUserInfor() can work correctly
	 */
	@Test
	public void testFetchUserInfo() {
		ArrayList<User> users = dbProcessor.fetchUserInfo();
		
		for(int i = 0;i < users.size();i++) {
			assertEquals(i+1, users.get(i).getUid());
		}
	}

	/**
	 * test whether the method FetchRecipe() can work correctly
	 */
	@Test
	public void testFetchRecipe() {
		HashMap<Integer, Recipe> recipes = dbProcessor.fetchRecipe();
		
		assertEquals(100, recipes.get(2).getCookingTime());
		assertEquals(30, recipes.get(1).getPreparationTime());
		
	}
	
	/**
	 * test whether the method FetchIngredient() can work correctly
	 */
	@Test
	public void testFetchIngredients() {
		ArrayList<String> ingredients = dbProcessor.fetchIngredients();
		
		assertEquals("Chiangang vinegar", ingredients.get(1));
		assertEquals("brown sugar", ingredients.get(0));
	}

	/**
	 * test whether the method FetchFavorite() can work correctly
	 */
	@Test
	public void testFetchFavorite() {
		ArrayList<Recipe> fRecipes = dbProcessor.fetchFavorite(24);
		

		assertEquals(2, fRecipes.get(0).getRecipeId());
	}

	/**
	 * test whether the method InsertUser() can work correctly
	 */
	@Test
	public void testInsertUser() {
		int userNumber0 = dbProcessor.fetchUserInfo().size();
		User testUser = new User("testUser","123456" );
		dbProcessor.insertUser(testUser);
		int userNumber1 = dbProcessor.fetchUserInfo().size();
		
		assertEquals(1, userNumber1 - userNumber0);
	}

	/**
	 * test whether the method InsertRecipe() can work correctly
	 */
	@Test
	public void testInsertRecipe() {
		int recipeNumber0 = dbProcessor.fetchRecipe().size();
		Recipe testRecipe = new Recipe("testRecipe", "test", 0);
		dbProcessor.insertRecipe(testRecipe, 5);
		int recipeNumber1 = dbProcessor.fetchRecipe().size();
		
		assertEquals(1, recipeNumber1 - recipeNumber0);
		
	}

	/**
	 * test whether the method addIngredient() can work correctly
	 */
	@Test
	public void testAddIngredient() {
		HashMap<Integer, Recipe> recipes0 = dbProcessor.fetchRecipe();
		int ingredientNumber0 = recipes0.get(2).getIngredients().size();
		Ingredient testIngredient = new Ingredient("sugar",1,"tablespoon");
		testIngredient.setRecipeId(2);
		dbProcessor.addIngredient(testIngredient);		
		HashMap<Integer, Recipe> recipes1 = dbProcessor.fetchRecipe();
		int ingredientNumber1 = recipes1.get(2).getIngredients().size();
		
		assertEquals(1, ingredientNumber1 - ingredientNumber0);
		
	}

	/**
	 * test whether the method FetchUserInfor() can work correctly
	 */
	@Test
	public void testDeleteIngredient() {

		dbProcessor.deleteIngredient(2);
		HashMap<Integer, Recipe> recipes = dbProcessor.fetchRecipe();
		
		assertEquals(0, recipes.get(2).getIngredients().size());
		
	}

	/**
	 * test whether the method updateRecipe() can work correctly
	 */
	@Test
	public void testUpdateRecipe() {
		Recipe testRecipe = dbProcessor.fetchRecipe().get(1);
		testRecipe.setRate(1.0);
		dbProcessor.updateRecipe(testRecipe);
		
		double newRate =  dbProcessor.fetchRecipe().get(1).getRate();
		assertEquals(1.0, newRate, 0.0);
	}

	/**
	 * test whether the method deleteRecipe() can work correctly
	 */
	@Test
	public void testDeleteRecipe() {
		HashMap<Integer, Recipe> recipes = dbProcessor.fetchRecipe();
		int recipeNumber0 = recipes.size();
		dbProcessor.deleteRecipe(recipes.size());
		int recipeNumber1 = dbProcessor.fetchRecipe().size();

		assertEquals(1, recipeNumber0 - recipeNumber1);
	}

	/**
	 * test whether the method deleteFavoriteRecipe() can work correctly
	 */
	@Test
	public void testDeleteFavoriteRecipe() {
		
		dbProcessor.insertIntoFavorite(dbProcessor.fetchRecipe().get(1), 11);
		int favoriteNumber0 = dbProcessor.fetchFavorite(11).size();
		dbProcessor.deleteFavoriteRecipe(1, 11);
		int favoriteNumber1 = dbProcessor.fetchFavorite(11).size();
		
		assertEquals(1, favoriteNumber0 - favoriteNumber1);
	}

	/**
	 * test whether the method insertIntoFavorite() can work correctly
	 */
	@Test
	public void testInsertIntoFavorite() {
		int favoriteNumber0 = dbProcessor.fetchFavorite(5).size();
		dbProcessor.insertIntoFavorite(dbProcessor.fetchRecipe().get(5), 5);
		int favoriteNumber1 = dbProcessor.fetchFavorite(5).size();
		
		assertEquals(1, favoriteNumber1 - favoriteNumber0);
	}

	/**
	 * test whether the method insertRate() can work correctly
	 */
	@Test
	public void testInsertRate() {
		
		dbProcessor.insertRate(dbProcessor.fetchRecipe().get(3), 4, 5.0);
		
		assertEquals(5.0, dbProcessor.fetchRecipe().get(3).getRate(),0.0);
	}

	/**
	 * test whether the method getTheMaxID() can work correctly
	 */
	@Test
	public void testGetTheMaxID() {
		Recipe testRecipe = new Recipe("maxID", "max", 0);
		dbProcessor.insertRecipe(testRecipe, 8);
		int maxID = dbProcessor.getTheMaxID();
		
		assertEquals("maxID", dbProcessor.fetchRecipe().get(maxID).getName());
	}

	/**
	 * test whether the method getRecommendRecipe() can work correctly
	 */
	@Test
	public void testGetRecommendRecipe() {
		Recipe highestRateRecipe;
		double rate = 0;
		Collection<Recipe> allRecipes = dbProcessor.fetchRecipe().values();

		for (Recipe recipe : allRecipes) {
			if(rate <= recipe.getRate()) {
				rate = recipe.getRate();
			}
		}
		
		assertEquals(rate, dbProcessor.getRecommendRecipe().getRate());
		
	}

	/**
	 * test whether the method getTheRate() can work correctly
	 */
	@Test
	public void testGetTheRate() {
		
		dbProcessor.insertRate(dbProcessor.fetchRecipe().get(2), 4, 0.0);
		
		assertEquals(0.0, dbProcessor.getTheRate(dbProcessor.fetchRecipe().get(2), 4), 0.0);
	}

	/**
	 * test whether the method matchRecipeName() can work correctly
	 */
	@Test
	public void testMatchRecipeName() {
		
		assertEquals(3, dbProcessor.matchRecipeName("Gong Bao Jiding").size());
	}

}
