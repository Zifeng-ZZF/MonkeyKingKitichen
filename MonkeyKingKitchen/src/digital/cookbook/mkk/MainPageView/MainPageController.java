package digital.cookbook.mkk.MainPageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.Recipe;
import digital.cookbook.mkk.ResultView.ResultViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MainPageController implements Initializable {

	private DBProcessor dbProcessor = new DBProcessor();
	private Map<Integer, Recipe> allRecipes;

	@FXML
	private Label userLabel;

	@FXML
	private ImageView recommendImg;

	@FXML
	private Label myRecipeLabel;

	@FXML
	private MenuButton typeBtn;

	@FXML
	private Label myFavoriteLabel;

	@FXML
	private TextField searchTxtField;

	@FXML
	private Button searchBtn;

	@FXML
	private Label recommendNameLabel;

	@FXML
	private Label logoutLabel;

	@FXML
	private Label briefStepLabel;

	@FXML
	private Label exitLabel;
	
	/**
	 * Invoked in LoginView to set the user name tag
	 * @param username
	 */
	public void setUserTag(String username){
		userLabel.setText(username);
	}

	/**
	 * Search recipe by name Send the result to the recipe result view
	 * 
	 * @param e
	 */
	@FXML
	public void sendSearchResult(ActionEvent e) {
		ArrayList<Recipe> results = new ArrayList<>();
		String name = searchTxtField.getText().trim();

		// Find all the results with given name
		Set recipeIds = allRecipes.keySet();
		for (Object recipeIdObj : recipeIds) {
			int id = (Integer) recipeIdObj;
			String recipeName = allRecipes.get(id).getName();
			if (recipeName.equals(name)) {
				results.add(allRecipes.get(id));
			}
		}

		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../ResultView/ResultView.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			ResultViewController controller = fxmlLoader.getController();
			controller.intializeResult(results);

			Scene scene = new Scene(parent);
			Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allRecipes = dbProcessor.fetchRecipe();
		
		//Set exit label
		exitLabel.setOnMouseClicked(e -> {
			Stage currentStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
			currentStage.close();
		});
	}

}
