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
 * @author Zifeng Zhang, Ji Sheng
 *
 */
public class LoginViewController implements Initializable {

	private DBProcessor dbProcessor = new DBProcessor();
	private ArrayList<User> users;
	private Map<Integer, Recipe> recipes;
	private boolean isLoginNameValid;
	private boolean isRegisterNameValid;
	private boolean isPasswordValid;

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
	
	@FXML
	private PasswordField confirmPasswdTxtField;

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
					// set the name tag
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
	 * Register a new account
	 * After which will directily jump to login interface
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

		if (!isExist) {
			if (isRegisterNameValid && isPasswordValid) {
				User newUser = new User(username, passwd);
				dbProcessor.insertUser(newUser);
				users.add(newUser);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Register successfully!/r/n Log in right now!");
				alert.setHeaderText("Warning");
				loginTab.getTabPane().getSelectionModel().select(loginTab);
				alert.show();
			}
		}

	}

	/**
	 * Test the username's validity
	 * @param e
	 */
	@FXML
	private void handleUsernameLength(ActionEvent e) {
		String username = registerUsernameTxtField.getText();
		if (username.length() < 8) {
			registerUsernameTxtField.setStyle("-fx-focus-color:red; -fx-text-box-border:red;");
		} else {
			this.isRegisterNameValid = true;
		}
	}
	
	/**
	 * Judge the validity and compatibility of the registering password
	 * @param e
	 */
	@FXML
	private void handlePasswordConfirm(ActionEvent e) {
		String fisrtPasswd = registerPasswdTxtField.getText();
		String confirmPasswd = confirmPasswdTxtField.getText();
		if(fisrtPasswd.length() < 6)
			registerPasswdTxtField.setStyle("-fx-effect: innershadow( one-pass-box , red , 8 , 0.0 , 2 , 0 )");
		if(!fisrtPasswd.equals(confirmPasswd))
			registerPasswdTxtField.setStyle("-fx-effect: innershadow( one-pass-box , red , 8 , 0.0 , 2 , 0 )");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		users = dbProcessor.fetchUserInfo();
		recipes = dbProcessor.fetchRecipe();

		loginUsernameTxtField.focusedProperty().addListener((obs, newVal, oldVal)->{
			String loginName = loginUsernameTxtField.getText();
			if(newVal) {
				System.out.println("Un Focused");
				if(loginName.length() < 8) {
					
				}
			}else {
				System.out.println("Focused");
			}
		});
	}

}
