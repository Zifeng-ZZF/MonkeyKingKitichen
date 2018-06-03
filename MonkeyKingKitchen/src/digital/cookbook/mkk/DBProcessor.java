package digital.cookbook.mkk;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Retrieve information from the DB
 * 
 * @author User
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
	 * @param uid
	 * @return
	 */
	public HashMap<Integer,Recipe> fetchRecipe() {

		Connection connection = DBUtil.open();
		String sql = "select*from recipetb;";
		HashMap<Integer,Recipe> recipeList = new HashMap<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet rSet = statement.executeQuery(sql);
			while (rSet.next()) {
				Recipe recipe = new Recipe(rSet.getString(2), rSet.getString(9), rSet.getInt(7));
				recipe.setRecipeId(rSet.getInt(1));
				recipe.setPreparationTime(rSet.getInt(3));
				recipe.setCookingTime(rSet.getInt(4));
				recipe.setRate(rSet.getDouble(6));

				String step = "";
				String steps = rSet.getString(5);
				char[] stepsManage = steps.toCharArray();
				for (char c : stepsManage) {
					if (c != '|') {
						step += c;
					} else {
						recipe.addPreparationStep(step);
						step = "";
					}
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

	public ArrayList<Recipe> fetchFavorite(int uid) {
		return null;
	}
	
	
}
