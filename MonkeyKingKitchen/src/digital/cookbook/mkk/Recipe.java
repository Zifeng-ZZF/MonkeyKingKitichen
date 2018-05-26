package digital.cookbook.mkk;

import java.util.ArrayList;

/**
 * The entity class of Recipe
 * 
 * @author Jisheng
 * @version 1.0 26/5/2018
 */

public class Recipe {
	// Recipe attributes
	private int recipeId;
	private int uid;
	private int preparationTime;
	private int cookingTime;
	private int servings;
	private double rate;
	private String type;
	private String name;
	private ArrayList<Ingredient> ingredients;
	private ArrayList<String> preparationSetps;

	// getters and setters
	public int getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(int recipeId) {
		this.recipeId = recipeId;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getPreparationTime() {
		return preparationTime;
	}

	public void setPreparationTime(int preparationTime) {
		this.preparationTime = preparationTime;
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

	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public ArrayList<String> getPreparationSetps() {
		return preparationSetps;
	}

	public void setPreparationSetps(ArrayList<String> preparationSetps) {
		this.preparationSetps = preparationSetps;
	}

}
