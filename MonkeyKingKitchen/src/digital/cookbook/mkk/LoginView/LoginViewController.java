/**
 * 
 */
package digital.cookbook.mkk.LoginView;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.Recipe;
import digital.cookbook.mkk.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

/**
 * Controller class for the login and register view
 * @author Zifeng Zhang
 *
 */
public class LoginViewController implements Initializable {
	
	private DBProcessor dbProcessor = new DBProcessor();
	private ArrayList<User> users;
	private Map<Integer, Recipe> recipes;
	
	/**
	 * Private constructor to fetch data from DB
	 */
	private LoginViewController() {
		users = dbProcessor.fetchUserInfo();
		recipes = dbProcessor.fetchRecipe();
	}

	@FXML
	private Button registerBtn;

	@FXML
	private TextField loginUsernameTxtField;

	@FXML
	private Button loginBtn;

	@FXML
	private Tab registerTab;

	@FXML
	private PasswordField registerPasswdTxtField;

	@FXML
	private Tab loginTab;

	@FXML
	private TextField registerUsernameTxtField;

	@FXML
	private PasswordField loginPasswdTxtField;
	
	/**
	 * To realize the login function
	 * Compare info from the view's textfields with the info retrieved from DB
	 * If matched, then open the main page and set up the user and recipe data
	 * @param e
	 */
	@FXML
	private void handleButtonAction(ActionEvent e) {
		String username = loginUsernameTxtField.getText();
		String passwd = loginPasswdTxtField.getText();
		ArrayList<Recipe> myRecipes = new ArrayList<>();
		
		for (User user : users) {
			//Initialize current user
			if (user.getName().equals(username) && user.getPasswd().equals(passwd)) {
				CookBook.setCurrentUser(user);
				//Initialize myFavoriteList
				user.setMyFavoriteList(dbProcessor.fetchFavorite(user.getUid()));
				//Initialize myRecipeList
				for (int recipeId : recipes.keySet()) {
					Recipe recipe = recipes.get(recipeId);
					if(recipe.getUid() == user.getUid())
						myRecipes.add(recipe);
				}
				user.setMyRecipeList(myRecipes);
				
				//At the meantime, jump to another 
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("../MainPageView/MainPageView/fxml"));
				try {
					Scene scene = new Scene(fxmlLoader.load());
					//get the event button (Login button)'s window, which is also a stage 
					Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
					stage.setScene(scene);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}
