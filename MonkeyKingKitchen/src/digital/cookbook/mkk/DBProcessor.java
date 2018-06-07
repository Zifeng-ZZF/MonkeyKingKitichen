package digital.cookbook.mkk;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Retrieve information from the DB
 * 
 * @author Zifeng Zhang, Xinyue Shi, Zhibin Xin
 * 
 */
public class DBProcessor {

	/**
	 * Acquire all the user infomation in the DB
	 * 
	 * @return ArrayList contains all the user in the DB
	 */
	public ArrayList<User> fetchUserInfo() {

		Connection connection = DBUtil.open();
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

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(connection);
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

		Connection connection = DBUtil.open();
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

				String[] steps = rSet.getString(9).split("|");
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(connection);
		}

		return recipeList;
	}

	/**
	 * Fetch all the MyFavouriteRecipe from the db to the ArrayList MyFavouriteList
	 * 
	 * @param uid
	 * @return ArrayList contains all recipes from MyFavouriteList
	 */
	public ArrayList<Recipe> fetchFavorite(int uid) {
		Connection connection = DBUtil.open();
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

				// Get the preparation steps
				String[] steps = recipeRS.getString(9).split("|");
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

				favoriteList.add(recipe);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(connection);
		}

		return favoriteList;
	}

	/**
	 * Insert new user into the db
	 * 
	 * @param user
	 */
	public void insertUser(User user) {
		Connection conn = DBUtil.open();

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
		Connection conn = DBUtil.open();
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
		} finally {
			DBUtil.close(conn);
		}
	}

	/**
	 * Insert an ingredient into the db while adding ingredient to recipe
	 * 
	 * @param ingredient
	 */
	public void addIngredient(Ingredient ingredient) {
		Connection connection = DBUtil.open();
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
	 * Update the recipe if it is editted
	 * 
	 * @param recipe
	 */
	public void updateRecipe(Recipe recipe) {
		Connection connection = DBUtil.open();
		String sql = "update recipetb set recipe_name = ?, recipe_preparation_time = ?,"
				+ "recipe_cooking_time = ?, recipe_averate = ?,"
				+ "recipe_serving = ?, recipe_type = ?, recipe_steps = ? " + "where recipe_id = ?;";

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
		Connection connection = DBUtil.open();
		String sql = "delete from recipetb where recipe_id=?;";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, recipeId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(connection);
		}
	}

	/**
	 * Delete a Recipe from favorite in db
	 * 
	 * @param recipeId
	 */
	public void deleteFavoriteRecipe(int recipeId, int userId) {
		Connection connection = DBUtil.open();
		String sql = "delete from myfavoritetb where recipe_id=? and user_id=?;";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, recipeId);
			statement.setInt(2, userId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(connection);
		}
	}

	/**
	 * insert the chosen recipe into myfavoritetb
	 * 
	 * @param recipe,currentUserID
	 */
	public void insertIntoFavorite(Recipe recipe, int currentUserID) {
		Connection conn = DBUtil.open();
		String qeurySql = "select recipe_id from myfavoritetb where user_id=?;";
		String sql = "insert into myfavoritetb(user_id,recipe_id)values(?,?);";

		try {
			PreparedStatement querystmt = conn.prepareStatement(qeurySql);
			querystmt.setInt(1, currentUserID);
			ResultSet rs = querystmt.executeQuery();

			while (rs.next()) {
				// check if recipe has been added
				if (recipe.getRecipeId() == rs.getInt(2)) {
					System.out.println("Recipe has been added to your favorite list");
				} else {
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, currentUserID);
					pstmt.setInt(2, recipe.getRecipeId());
					pstmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn);
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
		Connection conn = DBUtil.open();
		String sql = "insert into ratetb(user_id,recipe_id,rate)values(?,?,?)";
		String querysql = "select rate from ratetb where recipe_id=?";
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
			}
			double aveRate = Rate / amount;
			PreparedStatement updatePStmt = conn.prepareStatement(updatesql);
			updatePStmt.setDouble(1, aveRate);
			updatePStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn);
		}

	}

	/**
	 * Get the current largest recipe id in the recipetb
	 * 
	 * @return maxID
	 */
	public int getTheMaxID() {
		Connection conn = DBUtil.open();
		String idSql = "select max(recipe_id) from recipetb;";
		String cleanAutoIncreaseSql = "alter table recipetb auto_increment=1;";
		int maxID = 0;
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(idSql);
			if(resultSet.next()) {
				maxID = resultSet.getInt(1);
				System.out.println("Current Max id is: "+maxID);
				if (maxID == 0) {
					Statement statementClean = conn.createStatement();
					statementClean.executeUpdate(cleanAutoIncreaseSql);
				}
			}
		} catch (

		SQLException e1) {
			e1.printStackTrace();
		} finally {
			DBUtil.close(conn);
		}
//		System.out.println(maxID);
		return maxID;
	}
}
