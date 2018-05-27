package digital.cookbook.mkk;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The entity class of Recipe
 * 
 * @author Jisheng, Zifeng Zhang
 * @version 1.0 26/5/2018
 */

public class Recipe implements Serializable {
	
	private static int idCount = 0;
	
	// Recipe attributes
	private int recipeId = 0;
	private int uid = 0;
	private int preparationTime = 0;
	private int cookingTime = 0;
	private int servings = 0;
	private double rate = 0;
	private String type = null;
	private String name = null;
	private ArrayList<Ingredient> ingredients = null;
	private ArrayList<String> preparationSetps = null;
	
	/**
	 * Constructor
	 * @param name
	 * @param type
	 * @param servings
	 */
	public Recipe(String name, String type, int servings) {
		this.name = name;
		this.type = type;
		this.servings = servings;
		ingredients = new ArrayList<>();
		preparationSetps = new ArrayList<>();
		idCount++;
		this.recipeId = idCount;
	}
	
	/**
	 * Add ingredients to the recipe
	 * @param ingredient
	 */
	public void addIngredient(Ingredient ingredient) {
		this.ingredients.add(ingredient);
	}
	
	/**
	 * Add steps of preparing the meal
	 * @param preparationSetp
	 */
	public void addPreparationStep(String preparationSetp) {
		this.preparationSetps.add(preparationSetp);
	}
	
	/**
	 * Overide toString()
	 */
	public String toString() {
		String recipeInfo = null;
		
		for(Ingredient ingredient : ingredients) {
			recipeInfo += (ingredient.getName()+"/r/n");
		}
		
		return recipeInfo;
	}

	// getters and setters
	public int getRecipeId() {
		return recipeId;
	}

	public int getUid() {
		return uid;
	}

	public int getPreparationTime() {
		return preparationTime;
	}
	
	public void setPreparationTime(int preparationTime) {
		this.preparationTime =preparationTime;
	}

	public int getCookingTime() {
		return cookingTime;
	}

	public void setCookingTime(int cookingTime) {
		this.cookingTime = cookingTime;
	}

	public int getServings() {
		return servings;
	}

	public void setServings(int servings) {
		this.servings = servings;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}

	public ArrayList<String> getPreparationSetps() {
		return preparationSetps;
	}

}
