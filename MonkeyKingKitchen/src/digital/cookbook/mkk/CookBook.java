package digital.cookbook.mkk;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * The cookbook
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

	public static User getCurrentUser() {
		return currentUser;
	}
	
	public static void setCurrentUser(User user) {
		currentUser = user;
	}

	public static Recipe getCurrentRecipe() {
		return currentRecipe;
	}
	
	public static void setCurrentRecipe(Recipe recipe) {
		currentRecipe = recipe;
	}
	
	/**
	 * Constructor
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
