package digital.cookbook.mkk;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * The cookbook entity, graps necessary static data from the DB
 * 
 * @author Zifeng Zhang, Xinyue Shi
 * @version 2.0.1 28/5/2018
 *
 */
public class CookBook {

	private String cookBookTitle;
	private static Map<Integer, Recipe> recipes;
	private ArrayList<User> users;
	private DBProcessor dbProcessor = new DBProcessor();
	private static User currentUser;
	private static Recipe currentRecipe = null;
	private static Recipe recommendRecipe;

	/**
	 * Get the currentUser entity
	 * @return currentUser
	 */
	public static User getCurrentUser() {
		return currentUser;
	}
	
	/**
	 * This method will be called via login
	 * @param user
	 */
	public static void setCurrentUser(User user) {
		currentUser = user;
	}

	/**
	 * This function can get the browsed recipe or editted recipe.
	 * @return currentRecipe
	 */
	public static Recipe getCurrentRecipe() {
		return currentRecipe;
	}
	
	/**
	 * Set the current recipe
	 * Called while editing the recipe or browsing the recipe
	 * @param recipe
	 */
	public static void setCurrentRecipe(Recipe recipe) {
		currentRecipe = recipe;
	}
	
	/**
	 * Constructor
	 * fetch recommending recipe using DBProcessor from DB
	 * fetch all recipes from the DB
	 * 
	 * @param title
	 */
	public CookBook(String title) {
		recommendRecipe = dbProcessor.getRecommendRecipe();
		this.cookBookTitle = title;
		this.recipes = dbProcessor.fetchRecipe();
	}


	/**
	 * Get the total recipes of the cook book.
	 * 
	 * @return the recipes list
	 */
	public static Map<Integer, Recipe> getRecipesList() {
		return recipes;
	}

	
	/**
	 * Get the recommendRecipe
	 * @return
	 */
	public static Recipe getRecommandRecipe() {
		return recommendRecipe;
	}
}
