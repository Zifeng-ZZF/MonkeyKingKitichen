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

	/**
	 * Fetch favorite recipes from db
	 * @param uid
	 * @return
	 */
	public ArrayList<Recipe> fetchFavorite(int uid) {
		return null;
	}
	
	public void insertUser(User user) {
		Connection conn = DBUtil.open();
		
        //At first, find the maximum id of current db
        String idSql = "select max(user_id) from usertb;";
        Statement statement = null;
        int maxID = 0;
        try {
        	statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(idSql);
			if(resultSet.next())
				maxID = resultSet.getInt(1);
			maxID = 0;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        
        String sql = "insert into usertb(user_id,user_name,user_passwd) values(?,?,?);";
        try {
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1,maxID+1);
			pStatement.setString(2, user.getName());
			pStatement.setString(3, user.getPasswd());
			
			pStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Insert one recipe into recipetb in DB
	 * @param recipe
	 */
	public void insetRecipe(Recipe recipe, int currentUserID) {
        Connection conn = DBUtil.open();
        
        //At first, find the maximum id of current db
        String idSql = "select max(recipe_id) from recipetb;";
        Statement statement = null;
        int maxID = 0;
        try {
        	statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(idSql);
			if(resultSet.next())
				maxID = resultSet.getInt(1);
			maxID = 0;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        
        
        String sql = "insert into recipetb(recipe_id,recipe_name,recipe_preparation_time,"
        		+ "recipe_cooking_time,recipe_averate,recipe_servings,user_id,recipe_type,recipe_steps)"
        		+ " values(?,?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, maxID);
            pstmt.setString(2,recipe.getName());
            pstmt.setInt(3,recipe.getPreparationTime());
            pstmt.setInt(4,recipe.getCookingTime());
            pstmt.setDouble(5, recipe.getRate());
            pstmt.setInt(6,recipe.getServings());
            pstmt.setInt(7,currentUserID);
            pstmt.setString(8, recipe.getType());
            String steps = "";
            for (String step : recipe.getPreparationSetps()) {
				step += "|";
				steps += step;
			}
            pstmt.setString(9, steps);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn);
        }
	}
	
	public void updateRecipe() {
		
	}
}
