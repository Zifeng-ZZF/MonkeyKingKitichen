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
	private Map<Integer, Recipe> recipes;
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
	 * Add recipe to the cookBook, insert the reicpe to recipetb
	 * Meanwhile insert the ingredient into recipeingredientdb
	 * @param recipe
	 */
	public void addRecipe(Recipe recipe) {
		//1. Insert recipes
		this.recipes.put(recipe.getRecipeId(), recipe);
		dbProcessor.insertRecipe(recipe, currentUser.getUid());
		//2. Insert ingredients
		for (Ingredient ingredient : recipe.getIngredients()) {
			dbProcessor.addIngredient(ingredient);
		}
	}


	/**
	 * Use name to get the recipe with the same name
	 * 
	 * @param name
	 * @return null if no result, otherwise return the name-matched recipes
	 */
	public Recipe getRecipe(String name) {
		Set recipeIds = recipes.keySet();
		for (Object recipeIdObj : recipeIds) {
			int id = (Integer) recipeIdObj;
			String recipeName = recipes.get(id).getName();
			if (recipeName.equals(name)) {
				return recipes.get(id);
			}
		}
		return null;
	}

	/**
	 * Use name to get the recipe with the same name
	 * 
	 * @param name
	 * @return null if no result, otherwise return the name-matched recipes
	 */
	public Recipe searchByName(String name) {
		Set recipeIds = recipes.keySet();
		for (Object recipeIdObj : recipeIds) {
			int id = (Integer) recipeIdObj;
			String recipeName = recipes.get(id).getName();
			if (recipeName.equals(name)) {
				return recipes.get(id);
			}
		}
		return null;
	}

	/**
	 * Get the total recipes of the cook book.
	 * 
	 * @return the recipes list
	 */
	public Map<Integer, Recipe> getRecipesList() {
		return this.recipes;
	}

	
	/**
	 * Delete the recipe of the current user in myRecipeList
	 * @param recipe
	 */
	public void deleteTheRecipe(Recipe myRecipe) {
		if(currentUser.getUid() == myRecipe.getUid())
			dbProcessor.deleteRecipe(myRecipe.getRecipeId());
	}
	
	/**
	 * Over write deleteMethod
	 * Delete the currentRecipe if it belongs to the currentUser
	 */
	public void deleteTheRecipe() {
		if(currentUser.getUid() == this.currentRecipe.getUid())
			dbProcessor.deleteRecipe(currentRecipe.getRecipeId());
	}
	
	/**
	 * Open one single recipe
	 * @param recipe
	 */
	public void openRecipe(Recipe recipe) {
		this.currentRecipe = recipe;
		System.out.println(currentRecipe);
	}
	
	/**
	 * Get the recommendRecipe
	 * @return
	 */
	public static Recipe getRecommandRecipe() {
		return recommendRecipe;
	}
}
