package digital.cookbook.mkk;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * The cookbook
 * @author Zifeng Zhang
 *
 */
public class CookBook {
	
	private String cookBookTitle;
	private Map<Integer,Recipe> recipes;
	
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
		this.recipes.put(recipe.getUid(), recipe);
	}
	
	/**
	 * Use name to get the recipe with the same name
	 * @param name
	 * @return null if no result, otherwise return the name-matched recipes
	 */
	public Recipe getRecipe(String name) {
		Recipe result = null;
		Set recipeIds = recipes.keySet();
		for (Object recipeIdObj : recipeIds) {
			int id = (Integer)recipeIdObj;
			String recipeName = recipes.get(id).getName();
			if(recipeName.equals(name)) {
				return recipes.get(id);
			}
		}
		return null;
	}
	
	/**
	 * Use name to get the recipe with the same name
	 * @param name
	 * @return null if no result, otherwise return the name-matched recipes
	 */
	public Recipe searchByName(String name) {
		Recipe result = null;
		Set recipeIds = recipes.keySet();
		for (Object recipeIdObj : recipeIds) {
			int id = (Integer)recipeIdObj;
			String recipeName = recipes.get(id).getName();
			if(recipeName.equals(name)) {
				return recipes.get(id);
			}
		}
		return null;
	}
	
	public Map<Integer, Recipe> recipesList(){
		return this.recipes;
	}
}
