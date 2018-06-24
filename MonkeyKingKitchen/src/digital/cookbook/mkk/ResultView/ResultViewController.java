package digital.cookbook.mkk.ResultView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.Ingredient;
import digital.cookbook.mkk.Recipe;
import digital.cookbook.mkk.User;
import digital.cookbook.mkk.MainPageView.MainPageController;
import digital.cookbook.mkk.MyFavoriteView.MyFavoriteController;
import digital.cookbook.mkk.MyRecipeView.MyRecipeController;
import digital.cookbook.mkk.RecipeView.RecipeViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * @author Zifeng Zhang, Sheng Ji, Xinyue Shi
 *
 */
public class ResultViewController implements Initializable {

	private ArrayList<Recipe> results;
	private Map<Integer, Recipe> allRecipes;
	private DBProcessor dbProcessor = new DBProcessor();
	private Map<Button, Recipe> itemAcesses;
	private User currentUser = CookBook.getCurrentUser();
	
	@FXML
	private TitledPane titlePane;

	@FXML
	private Label userLabel;

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
	private Label mainPageLabel;

	@FXML
	private Label logoutLabel;

	@FXML
	private VBox listVBox;

	@FXML
	private HBox item;

	@FXML
	private Label itemName;

	@FXML
	private Label itemDesc;

	@FXML
	private Button itemOpenBtn;

	@FXML
	private Label exitLabel;
	
	@FXML
	private ScrollPane scrollPane;

	/**
	 * Get searching result from main page and list it
	 * 
	 * @param results
	 */
	public void intializeResult(ArrayList<Recipe> results) {
		this.results = results;
		itemAcesses = new HashMap<>();
		
		//Add all the items(hbox = name label + description label + open button) to the Map
		for (Recipe recipe : results) {
			itemName = new Label(recipe.getName());
			itemDesc = new Label(recipe.getType() + ", Cooking time: " + recipe.getCookingTime() + ", Preparing time: "
					+ recipe.getPreparationTime() + ", Servings: " + recipe.getServings());
			itemOpenBtn = new Button("OPEN");

			item = new HBox();
			item.getChildren().add(itemName);
			item.getChildren().add(itemDesc);
			item.getChildren().add(itemOpenBtn);

			listVBox.getChildren().add(item);
			
			itemAcesses.put(itemOpenBtn, recipe);
		}
		
		//Register the listener for all the results items
		Set<Button> itemOpenBtns = itemAcesses.keySet();
		for (Button button : itemOpenBtns) {
			button.setOnAction(e -> {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("../RecipeView/RecipeView.fxml"));
				CookBook.setCurrentRecipe(itemAcesses.get(button));
				System.out.println(CookBook.getCurrentRecipe().getName());
				
				try {
					Scene scene = new Scene(fxmlLoader.load());
					RecipeViewController controller = fxmlLoader.getController();
					controller.setRecipeDetail(CookBook.getCurrentRecipe());
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					currentStage.setScene(scene);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
		}
	}

	/**
	 * Search in the result UI
	 * After searching in the main page, user can continue to search
	 * @param e
	 */
	public void handleSearchBtn(ActionEvent e) {
		
		listVBox.getChildren().clear();
		
		String name = searchTxtField.getText();
		ArrayList<Recipe> newResults = new ArrayList<>();
		String type = this.typeBtn.getText();
		
		if (type.equals("recipe") || type.equals("--select--")) {
			//get the recipe_id with given name
			ArrayList<Integer> recipeIds = dbProcessor.matchRecipeName(name);
			if(!recipeIds.isEmpty()) {
				for(Integer recipeId : recipeIds) {
					newResults.add(allRecipes.get(recipeId));
				}
			}
		}
		else {
			for (Recipe recipe : allRecipes.values()) {
				ArrayList<Ingredient> ingredients = recipe.getIngredients();
				for (Ingredient ingredient : ingredients) {
					if (ingredient.getName().equals(name))
						newResults.add(recipe);
				}
			}
		}
		
		intializeResult(newResults);
	}
	
	/**
	 * Jump to main page
	 * @param e
	 */
	@FXML
	public void handleMainPageBtn(MouseEvent e) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MainPageView/MainPageView.fxml"));
		try {
			Scene scene = new Scene(fxmlLoader.load());
			// set the name tag
			MainPageController controller = fxmlLoader.getController();
			controller.setUserTag(currentUser.getName());
			Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Jump to my recipe list
	 * @param e
	 */
	@FXML
	public void handleMyRecipeBtn(MouseEvent e) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MyRecipeView/MyRecipeView.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Jump to my favorite recipe list
	 * @param e
	 */
	@FXML
	public void handleMyFavoriteBtn(MouseEvent e) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MyFavoriteView/MyFavoriteView.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Initialize the variables for this UI
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allRecipes = dbProcessor.fetchRecipe();
		
		//exitLabel
		exitLabel.setOnMouseClicked(e->{
			Stage currentStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
			currentStage.close();
		});
		
		//Listen to the menuitems to change the searching type
		for(MenuItem menuItem : typeBtn.getItems()) {
			menuItem.setOnAction(e -> {
				String text = menuItem.getText();
				typeBtn.setText(text);
			});
		}

	}

}
