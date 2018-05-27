package digital.cookbook.mkk;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The entity class of User
 * 
 * @author Jisheng
 * @version 1.0 26/5/2018 
 * @version 2.0 27/5/2018
 */

public class User implements Serializable{
	// User attributes
	private int uid;
	private static int idCount;
	private String name;
	private String passwd;
	private ArrayList<Recipe> myFavoriteList;
	private ArrayList<Recipe> myRecipeList;
	
	/**
	 * Constructor of user
	 * @param username
	 * @param passwd
	 */
	public User(String username, String passwd) {
		this.name = username;
		this.passwd = passwd;
		idCount++;
		this.uid = idCount;
	}

	//getters and setters
	public int getUid() {
		return uid;
	}

	public static int getIdCount() {
		return idCount;
	}

	public static void setIdCount(int idCount) {
		User.idCount = idCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Recipe> getMyFavoriteList() {
		return myFavoriteList;
	}

	public void setMyFavoriteList(ArrayList<Recipe> myFavoriteList) {
		this.myFavoriteList = myFavoriteList;
	}

	public ArrayList<Recipe> getMyRecipeList() {
		return myRecipeList;
	}

	public void setMyRecipeList(ArrayList<Recipe> myRecipeList) {
		this.myRecipeList = myRecipeList;
	}

}
