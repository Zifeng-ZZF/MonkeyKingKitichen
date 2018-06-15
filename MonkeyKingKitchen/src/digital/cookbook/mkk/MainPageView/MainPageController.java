package digital.cookbook.mkk.MainPageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.Ingredient;
import digital.cookbook.mkk.Recipe;
import digital.cookbook.mkk.RecipeView.RecipeViewController;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * View Controller for main page
 * @author Zifeng Zhang, Xinyue Shi, Sheng Ji
 *
 */
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
	 * 
	 * @param username
	 */
	public void setUserTag(String username) {
		userLabel.setText(username);
	}

	/**
	 * Search recipe by name or ingredientNmae
	 * 
	 * @param e
	 */
	@FXML
	public void handleSearchResult(ActionEvent e) {

		ArrayList<Recipe> results = new ArrayList<>();

		// 1.Judge which type of search
		String content = this.searchTxtField.getText();
		String type = this.typeBtn.getText();
		if (type.equals("recipe name") || type.equals("--select--"))
			results = searchRecipeByName(content);
		else
			results = searchRecipeByIngredient(content);

		// 2.Load the result view and jump
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

	/**
	 * Search recipe by using ingredient name
	 * 
	 * @param ingredientName
	 * @return
	 */
	public ArrayList<Recipe> searchRecipeByIngredient(String ingredientName) {
		ArrayList<Recipe> resultRecipes = new ArrayList<Recipe>();
		for (Recipe recipe : allRecipes.values()) {
			ArrayList<Ingredient> ingredients = recipe.getIngredients();
			for (Ingredient ingredient : ingredients) {
				if (ingredient.getName() == ingredientName)
					resultRecipes.add(recipe);
			}
		}
		return resultRecipes;
	}

	/**
	 * Search recipe by using recipe name
	 * 
	 * @param recipeName
	 * @return
	 */
	public ArrayList<Recipe> searchRecipeByName(String recipeName) {
		ArrayList<Recipe> results = new ArrayList<>();
		// Find all the results with given name
		Set recipeIds = allRecipes.keySet();
		for (Object recipeIdObj : recipeIds) {
			int id = (Integer) recipeIdObj;
			String name = allRecipes.get(id).getName();
			if (name.equals(recipeName)) {
				results.add(allRecipes.get(id));
			}
		}
		return results;
	}

	/**
	 * Jump to my recipe list
	 * @param e
	 */
	@FXML
	public void handleMyRecipeLabel(MouseEvent e) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MyRecipeView/MyRecipeView.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Jump to my favorite recipe list
	 * @param e
	 */
	@FXML
	public void handleMyFavoriteLabel(MouseEvent e) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MyFavoriteView/MyFavoriteView.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Handle the recommend recipe imageview and label
	 * jump to the recipe detail
	 * @param e
	 */
	@FXML
	public void handleRecommendation(MouseEvent e) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../RecipeView/RecipeView.fxml"));
		try {
			Scene scene = new Scene(fxmlLoader.load());
			RecipeViewController controller = fxmlLoader.getController();
			//set up the recommend recipe
			controller.setRecipeDetail(CookBook.getRecommandRecipe());
			
			Stage currentStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allRecipes = dbProcessor.fetchRecipe();

		// Set exit label
		exitLabel.setOnMouseClicked(e -> {
			Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			currentStage.close();
		});
	}

}
