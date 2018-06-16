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
import digital.cookbook.mkk.Recipe;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResultViewController implements Initializable {

	private ArrayList<Recipe> results;
	private Map<Integer, Recipe> allRecipes;
	private DBProcessor dbProcessor = new DBProcessor();
	private Map<Button, Recipe> itemAcesses;

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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
		}
	}

	//sdsd
	/**
	 * Search in the result UI
	 * After searching in the main page, user can continue to search
	 * @param e
	 */
	public void handleSearchBtn(ActionEvent e) {
		
		listVBox.getChildren().clear();
		
		String name = searchTxtField.getText();
		ArrayList<Recipe> newResults = new ArrayList<>();

		Set recipeIds = allRecipes.keySet();
		for (Object recipeIdObj : recipeIds) {
			int id = (Integer) recipeIdObj;
			String recipeName = allRecipes.get(id).getName();
			if (recipeName.equals(name)) {
				newResults.add(allRecipes.get(id));
			}
		}
		
		for (Recipe recipe : newResults) {
			itemName = new Label(recipe.getName());
			itemDesc = new Label(recipe.getType() + ", Cooking time: " + recipe.getCookingTime() + ", Preparing time: "
					+ recipe.getPreparationTime() + ", Servings: " + recipe.getServings());
			itemOpenBtn = new Button("OPEN");

			item = new HBox();
			item.getChildren().add(itemName);
			item.getChildren().add(itemDesc);
			item.getChildren().add(itemOpenBtn);

			listVBox.getChildren().add(item);
		}

		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allRecipes = dbProcessor.fetchRecipe();
		
		//exitLabel
		exitLabel.setOnMouseClicked(e->{
			Stage currentStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
			currentStage.close();
		});
		
	}

}
