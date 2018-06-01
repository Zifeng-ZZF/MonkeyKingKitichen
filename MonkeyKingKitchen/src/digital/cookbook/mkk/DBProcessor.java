package digital.cookbook.mkk;

import java.sql.*;
import java.util.ArrayList;

/**
 * Retrieve information from the DB
 * @author User
 *
 */
public class DBProcessor {
	
	/**
	 * Acquire all the user infomation in the DB
	 * @param user
	 * @return ArrayList contains all the user in the DB
	 */
	public ArrayList<User> fetchUserInfo(User user) {
		
		Connection connection = DBUtil.open();
		String sql = "select*from usertb;";
		ArrayList<User> userlist = new ArrayList<>(); //To store all the users in the DB
		
		try {
			Statement statement = connection.createStatement();
			ResultSet rSet = statement.executeQuery(sql);
			while(rSet.next()) {
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
	
	public ArrayList<Recipe> fetchMyRecipe(int uid){
		return null;
	}
	
	public ArrayList<Recipe> fetchFavorite(int uid){
		return null;
	}
}
