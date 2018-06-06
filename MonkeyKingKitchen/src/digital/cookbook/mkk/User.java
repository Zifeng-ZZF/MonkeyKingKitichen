package digital.cookbook.mkk;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

/**
 * The entity class of User
 * 
 * @author Jisheng, Zifeng Zhang
 * @version 1.0 26/5/2018 
 * @version 2.0 27/5/2018
 */

public class User implements Serializable{
	// User attributes
	private int uid;
	private String name;
	private String passwd;
	private ArrayList<Recipe> myFavoriteList;
	private ArrayList<Recipe> myRecipeList;
	private DBProcessor dbProcessor = new DBProcessor();
	
	/**
	 * Constructor of user
	 * @param username
	 * @param passwd
	 */
	public User(String username, String passwd) {

		this.name = username;
		this.passwd = passwd;
	}
	
	/**
	 * Add recipe to favorite
	 * @param recipe
	 */
	public void addToFavorite(Recipe recipe) {
		this.myFavoriteList.add(recipe);
		dbProcessor.insertIntoFavorite(recipe, this.uid);
	}

	//getters and setters
	public String getPasswd() {
		return this.passwd;
	}
	
	public int getUid() {
		return uid;
	}
	
	public void setUid(int uid) {
		this.uid = uid;
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
