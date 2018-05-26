package digital.cookbook.mkk;

import java.util.HashMap;
import java.util.Map;

/**
 * The cookbook
 * @author Zifeng Zhang
 *
 */
public class CookBook {
	
	private String cookBookTitle;
	private Map<String,Recipe> recipes;
	
	/**
	 * Constructor
	 * @param title
	 */
	public CookBook(String title) {
		this.cookBookTitle = title;
		recipes = new HashMap<>();
	}
	
	/**
	 * Add recipe to the cookBook
	 * @param recipe
	 */
	public void add(Recipe recipe) {
		this.recipes.put(recipe.getName(), recipe);
	}
	
	/**
	 * 
	 * @param name
	 * @return the recipe with the given name
	 */
	public Recipe getRecipe(String name) {
		Recipe recipe = this.recipes.get(name);
		return recipe;
	}
	
}
