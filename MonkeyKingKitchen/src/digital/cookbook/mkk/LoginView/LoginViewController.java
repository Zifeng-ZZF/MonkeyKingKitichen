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
import javafx.scene.control.Label;
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
	private boolean isPasswordValid;
	private boolean isPasswordPaired;
	private boolean isRegisterNameValid;

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

	@FXML
	private Label loginReminderLabel;
	
	@FXML
	private Label registerNameRemindLb;
	
	@FXML
	private Label registerPasswdRemindLb;
	
	@FXML
	private Label pairedLabel;
	
	/**
	 * To realize the login function Compare info from the view's textfields with
	 * the info retrieved from DB 
	 * If matched, then open the main page and set up the
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
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setContentText("The password or username you entered is incorrect. Please check again!");
				alert.show();
			}
		}
	}

	/**
	 * Register a new account After which will directily jump to login interface
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
			}else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Username or password not valid. Check again.");
				alert.setHeaderText("Warning");
				loginTab.getTabPane().getSelectionModel().select(loginTab);
				alert.show();
			}
		}

	}

	/**
	 * Grap needed info Add properties listeners
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		users = dbProcessor.fetchUserInfo();
		recipes = dbProcessor.fetchRecipe();

		// Login username length listener
		loginUsernameTxtField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			String loginName = loginUsernameTxtField.getText();
			if (oldVal) {
				System.out.println("Un Focused");
				if (loginName.length() < 8) {
					loginReminderLabel.setVisible(true);
					isLoginNameValid = false;
				}else {
					loginReminderLabel.setVisible(false);
					isLoginNameValid = true;
				}
			}
			
			if(isLoginNameValid && isPasswordValid)
				loginBtn.setDisable(false);
			else
				loginBtn.setDisable(true);
			
		});
		
		// Login password empty listener
		loginPasswdTxtField.setOnKeyPressed(e -> {
			if(loginPasswdTxtField.getText().length() != 0)
				isPasswordValid = true;
			else
				isPasswordValid = false;
			
			if(isLoginNameValid && isPasswordValid)
				loginBtn.setDisable(false);
			else
				loginBtn.setDisable(true);
		});
		
		// register username length listener
		registerUsernameTxtField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			String registerName = registerUsernameTxtField.getText();
			if (oldVal) {
				System.out.println("Un Focused");
				if (registerName.length() < 8) {
					registerNameRemindLb.setVisible(true);
					isRegisterNameValid = false;
				}else {
					registerNameRemindLb.setVisible(false);
					isRegisterNameValid = true;
				}
			}
		});
		
		// register password length listener
		registerPasswdTxtField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			String registerPasswd = registerPasswdTxtField.getText();
			if (oldVal) {
				System.out.println("Un Focused");
				if (registerPasswd.length() < 6) {
					registerPasswdRemindLb.setVisible(true);
				}else {
					registerPasswdRemindLb.setVisible(false);
				}
			}
		});
		
		// register password match listener
		confirmPasswdTxtField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			String registerPasswd = registerPasswdTxtField.getText();
			String confirmPasswd = confirmPasswdTxtField.getText();
			if (oldVal) {
				System.out.println("Un Focused");
				if (!registerPasswd.equals(confirmPasswd)) {
					pairedLabel.setVisible(true);
					isPasswordPaired = false;
				}else {
					pairedLabel.setVisible(false);
					isPasswordPaired = true;
				}
			}
		});
	}

}
