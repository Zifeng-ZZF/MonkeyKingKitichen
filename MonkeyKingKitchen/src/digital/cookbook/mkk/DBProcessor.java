package digital.cookbook.mkk;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Retrieve information from the DB
 * 
 * @author Zifeng Zhang, Xinyue Shi, Sheng Ji, Zhibin Xin
 * 
 */
public class DBProcessor {

	/**
	 * Acquire all the user infomation in the DB
	 * 
	 * @return ArrayList contains all the user in the DB
	 */
	public ArrayList<User> fetchUserInfo() {

		Connection connection = DBUtil.getConnection();
		String sql = "select*from usertb;";
		ArrayList<User> userlist = new ArrayList<>(); // To store all the users in the DB

		try {
			Statement statement = connection.createStatement();
			ResultSet rSet = statement.executeQuery(sql);
			while (rSet.next()) {
				User userInDB = new User(rSet.getString(2), rSet.getString(3));
				userInDB.setUid(rSet.getInt(1));
				userlist.add(userInDB);
			}
			rSet.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userlist;
	}

	/**
	 * Fetch all the Recipe in the db to the CookBook
	 * 
	 * @param uid
	 * @return
	 */
	public HashMap<Integer, Recipe> fetchRecipe() {

		Connection connection = DBUtil.getConnection();
		String sql1 = "select*from recipetb;";
		HashMap<Integer, Recipe> recipeList = new HashMap<>();

		String sql2 = "select*from recipeingredientdb where recipe_id=?";

		try {
			Statement statement = connection.createStatement();
			ResultSet rSet = statement.executeQuery(sql1);
			PreparedStatement pStatement = connection.prepareStatement(sql2);

			while (rSet.next()) {
				Recipe recipe = new Recipe(rSet.getString(2), rSet.getString(8), rSet.getInt(6));
				recipe.setRecipeId(rSet.getInt(1));
				recipe.setPreparationTime(rSet.getInt(3));
				recipe.setCookingTime(rSet.getInt(4));
				recipe.setRate(rSet.getDouble(5));
				recipe.setUid(rSet.getInt(7));

				String[] steps = rSet.getString(9).split("\\|");
				for (String step : steps) {
					recipe.addPreparationStep(step);
				}

				pStatement.setInt(1, recipe.getRecipeId());
				ResultSet ingredientRSet = pStatement.executeQuery();
				while (ingredientRSet.next()) {
					Ingredient ingredient = new Ingredient(ingredientRSet.getString(1), ingredientRSet.getDouble(5),
							ingredientRSet.getString(3), ingredientRSet.getString(4));
					recipe.addIngredient(ingredient);
				}

				recipeList.put(recipe.getRecipeId(), recipe);

			}

			rSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return recipeList;
	}

	/**
	 * Fetch all the ingredients from the db to the ArrayList allIngredients
	 * 
	 * @return ArrayList contains all ingredients
	 */
	public ArrayList<String> fetchIngredients() {
		Connection conn = DBUtil.getConnection();
		ArrayList<String> allIngredients = new ArrayList<String>();
		String sql = "select*from ingredienttb";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				allIngredients.add(rs.getString(1));
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allIngredients;
	}

	/**
	 * Fetch all the MyFavouriteRecipe from the db to the ArrayList MyFavouriteList
	 * 
	 * @param uid
	 * @return ArrayList contains all recipes from MyFavouriteList
	 */
	public ArrayList<Recipe> fetchFavorite(int uid) {
		Connection connection = DBUtil.getConnection();
		String sql1 = "select*from recipetb where recipe_id in"
				+ "(select recipe_id from myfavoritetb where user_id=?)";
		String sql2 = "select*from recipeingredientdb where recipe_id=?";
		ArrayList<Recipe> favoriteList = new ArrayList<>();

		try {
			PreparedStatement pStmt1 = connection.prepareStatement(sql1);
			pStmt1.setInt(1, uid);
			ResultSet recipeRS = pStmt1.executeQuery();

			PreparedStatement pStmt2 = connection.prepareStatement(sql2);

			while (recipeRS.next()) {
				Recipe recipe = new Recipe(recipeRS.getString(2), recipeRS.getString(8), recipeRS.getInt(6));
				recipe.setRecipeId(recipeRS.getInt(1));
				recipe.setPreparationTime(recipeRS.getInt(3));
				recipe.setCookingTime(recipeRS.getInt(4));
				recipe.setRate(recipeRS.getDouble(5));
				recipe.setUid(recipeRS.getInt(7));

				// Get the preparation steps
				String[] steps = recipeRS.getString(9).split("\\|");
				for (String step : steps) {
					recipe.addPreparationStep(step);
				}

				// Get corresponding ingredient used in this recipe
				pStmt2.setInt(1, recipe.getRecipeId());
				ResultSet ingredientRS = pStmt2.executeQuery();
				while (ingredientRS.next()) {
					Ingredient ingredient = new Ingredient(ingredientRS.getString(1), ingredientRS.getDouble(5),
							ingredientRS.getString(3), ingredientRS.getString(4));
					recipe.addIngredient(ingredient);
				}
				ingredientRS.close();

				favoriteList.add(recipe);
			}

			recipeRS.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return favoriteList;
	}

	/**
	 * Insert new user into the db
	 * 
	 * @param user
	 */
	public void insertUser(User user) {
		Connection conn = DBUtil.getConnection();

		// At first, find the maximum id of current db
		String idSql = "select max(user_id) from usertb;";
		Statement statement = null;
		int maxID = 0;
		try {
			statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(idSql);
			if (resultSet.next())
				maxID = resultSet.getInt(1);
			else
				maxID = 0;

			resultSet.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		String sql = "insert into usertb(user_id,user_name,user_passwd) values(?,?,?);";
		try {
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, maxID + 1);
			pStatement.setString(2, user.getName());
			pStatement.setString(3, user.getPasswd());

			pStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insert one recipe into recipetb in DB
	 * 
	 * @param recipe
	 */
	public void insertRecipe(Recipe recipe, int currentUserID) {
		Connection conn = DBUtil.getConnection();
		String sql = "insert into recipetb(recipe_name,recipe_preparation_time,"
				+ "recipe_cooking_time,recipe_averate,recipe_servings,user_id,recipe_type,recipe_steps)"
				+ " values(?,?,?,?,?,?,?,?);";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, recipe.getName());
			pstmt.setInt(2, recipe.getPreparationTime());
			pstmt.setInt(3, recipe.getCookingTime());
			pstmt.setDouble(4, recipe.getRate());
			pstmt.setInt(5, recipe.getServings());
			pstmt.setInt(6, currentUserID);
			pstmt.setString(7, recipe.getType());
			String steps = "";
			for (String step : recipe.getPreparationSetps()) {
				step += "|";
				steps += step;
			}
			pstmt.setString(8, steps);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insert an ingredient into the db while adding ingredient to recipe
	 * 
	 * @param ingredient
	 */
	public void addIngredient(Ingredient ingredient) {
		Connection connection = DBUtil.getConnection();
		String sql = "insert into recipeingredientdb(ingredient_name,recipe_id,unit,process_method,amount)"
				+ " values(?,?,?,?,?)";
		try {
			PreparedStatement pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, ingredient.getName());
			pStatement.setInt(2, ingredient.getRecipeId());
			pStatement.setString(3, ingredient.getUnit());
			pStatement.setString(4, ingredient.getProcessMethod());
			pStatement.setDouble(5, ingredient.getAmount());
			pStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete a ingredient from recipeingredientdb in db
	 * 
	 * @param recipe,
	 *            ingredient
	 */
	public void deleteIngredient(int recipeId) {
		Connection conn = DBUtil.getConnection();
		String sql = "delete from recipeingredientdb where recipe_id=?;";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recipeId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update the recipe if it is editted
	 * 
	 * @param recipe
	 */
	public void updateRecipe(Recipe recipe) {
		Connection connection = DBUtil.getConnection();
		String sql = "update recipetb set recipe_name = ?, recipe_preparation_time = ?,"
				+ "recipe_cooking_time = ?, recipe_averate = ?,"
				+ "recipe_servings = ?, recipe_type = ?, recipe_steps = ? " + "where recipe_id = ?;";

		try {
			PreparedStatement pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, recipe.getName());
			pStatement.setInt(2, recipe.getPreparationTime());
			pStatement.setInt(3, recipe.getCookingTime());
			pStatement.setDouble(4, recipe.getRate());
			pStatement.setInt(5, recipe.getServings());
			pStatement.setString(6, recipe.getType());
			pStatement.setInt(8, recipe.getRecipeId());

			String steps = "";
			for (String step : recipe.getPreparationSetps()) {
				step += "|";
				steps += step;
			}
			pStatement.setString(7, steps);

			pStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete a Recipe in db
	 * 
	 * @param recipeId
	 */
	public void deleteRecipe(int recipeId) {
		Connection connection = DBUtil.getConnection();
		String sql = "delete from recipetb where recipe_id=?;";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, recipeId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete a Recipe from favorite in db
	 * 
	 * @param recipeId
	 */
	public void deleteFavoriteRecipe(int recipeId, int userId) {
		Connection connection = DBUtil.getConnection();
		String sql = "delete from myfavoritetb where recipe_id=? and user_id=?;";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, recipeId);
			statement.setInt(2, userId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * insert the chosen recipe into myfavoritetb
	 * 
	 * @param recipe,currentUserID
	 */
	public void insertIntoFavorite(Recipe currentRecipe, int currentUserID) {
		Connection conn = DBUtil.getConnection();
		String sql = "insert into myfavoritetb(user_id,recipe_id)values(?,?);";

		try {

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, currentUserID);
			pstmt.setInt(2, currentRecipe.getRecipeId());
			pstmt.executeUpdate();

		} catch (

		SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Insert individual rates of the user
	 * 
	 * @param recipe
	 * @param userID
	 * @param rate
	 */
	public void insertRate(Recipe recipe, int userID, double rate) {
		Connection conn = DBUtil.getConnection();

		double aveRate = 0;
		String sql = "insert into ratetb(user_id,recipe_id,rate)values(?,?,?)";
		String querysql = "select * from ratetb where recipe_id=?";
		String updatesql = "update recipetb set recipe_averate =? where recipe_id=?";

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userID);
			pstmt.setInt(2, recipe.getRecipeId());
			pstmt.setDouble(3, rate);
			pstmt.executeUpdate();

			PreparedStatement queryPStmt = conn.prepareStatement(querysql);
			queryPStmt.setInt(1, recipe.getRecipeId());
			ResultSet rs = queryPStmt.executeQuery();
			int Rate = 0;
			int amount = 0;

			while (rs.next()) {
				Rate = Rate + rs.getInt(3);
				amount = amount + 1;
				aveRate = Rate / amount;
			}

			PreparedStatement updatePStmt = conn.prepareStatement(updatesql);
			updatePStmt.setDouble(1, aveRate);
			updatePStmt.setInt(2, recipe.getRecipeId());
			updatePStmt.executeUpdate();
			recipe.setRate(aveRate);

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the current largest recipe id in the recipetb
	 * 
	 * @return maxID
	 */
	public int getTheMaxID() {
		Connection conn = DBUtil.getConnection();
		String idSql = "select max(recipe_id) from recipetb;";
		String cleanAutoIncreaseSql = "alter table recipetb auto_increment=1;";
		int maxID = 0;
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(idSql);
			if (resultSet.next()) {
				maxID = resultSet.getInt(1);
				if (maxID == 0) {
					Statement statementClean = conn.createStatement();
					statementClean.executeUpdate(cleanAutoIncreaseSql);
				}
			}

			resultSet.close();

		} catch (

		SQLException e1) {
			e1.printStackTrace();
		}
		System.out.println("Current Max id is: " + maxID);
		return maxID;
	}

	/**
	 * Get the recommending Recipe
	 * 
	 * @return
	 */
	public Recipe getRecommendRecipe() {
		Connection connection = DBUtil.getConnection();
		String sql1 = "select * from recipetb order by recipe_averate desc limit 1";
		String sql2 = "select*from recipeingredientdb where recipe_id=?";
		Recipe recipe = null;

		try {
			Statement statement = connection.createStatement();
			PreparedStatement pStatement = connection.prepareStatement(sql2);

			ResultSet rSet = statement.executeQuery(sql1);
			while (rSet.next()) {
				recipe = new Recipe(rSet.getString(2), rSet.getString(8), rSet.getInt(6));
				recipe.setRecipeId(rSet.getInt(1));
				recipe.setPreparationTime(rSet.getInt(3));
				recipe.setCookingTime(rSet.getInt(4));
				recipe.setRate(rSet.getDouble(5));
				recipe.setUid(rSet.getInt(7));

				String[] steps = rSet.getString(9).split("\\|");
				for (String step : steps) {
					recipe.addPreparationStep(step);
				}

				pStatement.setInt(1, recipe.getRecipeId());
				ResultSet ingredientRSet = pStatement.executeQuery();
				while (ingredientRSet.next()) {
					Ingredient ingredient = new Ingredient(ingredientRSet.getString(1), ingredientRSet.getDouble(5),
							ingredientRSet.getString(3), ingredientRSet.getString(4));
					recipe.addIngredient(ingredient);
				}
			}

			rSet.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recipe;
	}

	/**
	 * 
	 * @param recipe
	 * @param userID
	 * @return
	 */
	public int getTheRate(Recipe recipe, int userID) {

		int rate = 0;
		Connection conn = DBUtil.getConnection();
		String sql = "select rate from ratetb where user_id=? and recipe_id=?;";

		try {
			PreparedStatement queryPStmt = conn.prepareStatement(sql);
			queryPStmt.setInt(1, userID);
			queryPStmt.setInt(2, recipe.getRecipeId());
			ResultSet rs = queryPStmt.executeQuery();
			if (rs.first()) {
				rate = rs.getInt("rate");
				rs.previous();
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rate;
	}

	/**
	 * Search recipe in the DB by name 
	 * @param name
	 * @return
	 */
	public ArrayList<Integer> matchRecipeName(String name) {

		ArrayList<Integer> result = new ArrayList<Integer>();
		Connection conn = DBUtil.getConnection();
		String sql = "select recipe_id from recipetb where recipe_name like ?;";

		try {
			PreparedStatement queryPStmt = conn.prepareStatement(sql);
			queryPStmt.setString(1, "%" + name + "%");
			ResultSet rSet = queryPStmt.executeQuery();
			while (rSet.next()) {
				result.add(rSet.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
