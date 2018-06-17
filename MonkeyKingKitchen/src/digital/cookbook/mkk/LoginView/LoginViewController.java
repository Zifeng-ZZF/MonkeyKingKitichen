package digital.cookbook.mkk.LoginView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.Recipe;
import digital.cookbook.mkk.User;
import digital.cookbook.mkk.MainPageView.MainPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

/**
 * Controller class for the login and register view
 * 
 * @author Zifeng Zhang
 *
 */
public class LoginViewController implements Initializable {

	private DBProcessor dbProcessor = new DBProcessor();
	private ArrayList<User> users;
	private Map<Integer, Recipe> recipes;

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
	 * To realize the login function Compare info from the view's textfields with
	 * the info retrieved from DB If matched, then open the main page and set up the
	 * user and recipe data
	 * 
	 * @param e
	 */
	@FXML
	private void handleLoginButtonAction(ActionEvent e) {
		String username = loginUsernameTxtField.getText();
		String passwd = loginPasswdTxtField.getText();
		ArrayList<Recipe> myRecipes = new ArrayList<>();

		System.out.println(username + passwd);

		for (User user : users) {
			// Initialize current user
			if (user.getName().equals(username) && user.getPasswd().equals(passwd)) {
				CookBook.setCurrentUser(user);
				// Initialize myFavoriteList
				user.setMyFavoriteList(dbProcessor.fetchFavorite(user.getUid()));
				// Initialize myRecipeList
				for (int recipeId : recipes.keySet()) {
					Recipe recipe = recipes.get(recipeId);
					if (recipe.getUid() == user.getUid())
						myRecipes.add(recipe);
				}
				user.setMyRecipeList(myRecipes);

				// At the meantime, jump to another
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("../MainPageView/MainPageView.fxml"));
				try {
					Scene scene = new Scene(fxmlLoader.load());
					//set the name tag
					MainPageController controller = fxmlLoader.getController();
					controller.setUserTag(user.getName());
					// get the event button (Login button)'s window, which is also a stage
					Stage oldStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					oldStage.close();
					Stage newStage = new Stage();
					newStage.setScene(scene);
					newStage.setTitle("MMK Digital Cookbook");
					newStage.show();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @param e
	 */
	@FXML
	private void handleRegisterButtonAction(ActionEvent e) {
		String username = registerUsernameTxtField.getText();
		String passwd = registerPasswdTxtField.getText();
		boolean isExist = false;

		for (User user : users) {
			if (user.getName().equals(username)) {
				isExist = true;
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("User name exists!");
				alert.show();
			}
		}
		
		if(!isExist) {
			User newUser = new User(username, passwd);
			dbProcessor.insertUser(newUser);
			users.add(newUser);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Register successfully!\r\n Log in right now!");
			alert.setHeaderText("Warning");
			loginTab.getTabPane().getSelectionModel().select(loginTab);
			alert.show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		users = dbProcessor.fetchUserInfo();
		recipes = dbProcessor.fetchRecipe();
		
		
	}

}
