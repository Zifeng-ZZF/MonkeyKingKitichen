package digital.cookbook.mkk;

import java.sql.*;
import java.util.ArrayList;

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
	 * @param user
	 * @return ArrayList contains all the user in the DB
	 */
	public ArrayList<User> fetchUserInfo(User user) {

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
	 * Fetch all the myrecipe from the db to the ArrayList MyRecipeList
	 * @param uid
	 * @return
	 */
	public ArrayList<Recipe> fetchMyRecipe(int uid) {

		Connection connection = DBUtil.open();
		String sql = "select*from recipetb where recipe_id=?;";
		ArrayList<Recipe> recipeList = new ArrayList<>();

		try {
			PreparedStatement pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, uid);
			ResultSet rSet = pStatement.executeQuery();
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
				recipeList.add(recipe);
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
