package digital.cookbook.mkk.MyRecipeView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.Recipe;
import digital.cookbook.mkk.User;
import digital.cookbook.mkk.EditPage.EditViewController;
import digital.cookbook.mkk.RecipeView.RecipeViewController;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class MyRecipeController implements Initializable {
	
	private ArrayList<Recipe> myRecipes = new ArrayList<>();
	private DBProcessor dBProcessor = new DBProcessor();
	private Map<Button, HBox> itemAccess = new HashMap<>();
	private Map<HBox, Recipe> recipeAccess = new HashMap<>();
	private User currentUser = CookBook.getCurrentUser();
	
	@FXML
	private Pane mainPane;
	@FXML
	private Text myFavoriteLabel;
	@FXML
	private Button deleteButton;
	@FXML
	private Button openButton;
	@FXML
	private Pane rightPane;
	@FXML
	private Label userNameLabel;
	@FXML
	private Button myRecipeButton;
	@FXML
	private Button mainPageButton;
	@FXML
	private Label mkkLogoLabel;
	@FXML
	private Button editButton;
	@FXML
	private Button createButton;
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
	private Button itemDeleteBtn;
	@FXML
	private VBox buttonItem;
	@FXML
	private Button itemEditBtn;

	@FXML
	public void deleteEvent(ActionEvent event) {
	
	}

	@FXML
	public void openEvent(ActionEvent event) {
		
	}

	/**
	 * Jump to my favorite recipe list
	 * @param e
	 */
	@FXML
	public void jumpToMyFavoriteEvent(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MyFavoriteView/MyFavoriteView.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Jump to main page
	 * @param event
	 */
	@FXML
	public void jumpToMainPageEvent(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MainPageView/MainPageView.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Jump to edit page view to create new recipe
	 * 
	 * @param event
	 */
	@FXML
	public void createEvent(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../EditPage/EditPageView.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Get my recipes from main page and list it
	 * 
	 * @param results
	 */
	public void setMyRecipeList(ArrayList<Recipe> myRecipes) {
		

		// Add all the items(hbox = name label + description label + button) to the Map
		for (Recipe recipe : myRecipes) {
			itemName = new Label(recipe.getName());
			itemDesc = new Label(recipe.getType() + ", Cooking time: " + recipe.getCookingTime() + ", Preparing time: "
					+ recipe.getPreparationTime() + ", Servings: " + recipe.getServings());
			itemOpenBtn = new Button("OPEN");
			itemDeleteBtn = new Button("DELETE");
			itemEditBtn = new Button("EDIT");

			item = new HBox();
			buttonItem = new VBox();
			item.getChildren().add(itemName);
			item.getChildren().add(itemDesc);
			buttonItem.getChildren().add(itemOpenBtn);
			buttonItem.getChildren().add(itemDeleteBtn);
			buttonItem.getChildren().add(itemEditBtn);
			
			item.getChildren().add(buttonItem);
			listVBox.getChildren().add(item);


			// Register the listener for all the buttons (open, edit and delete)
			// Open button
			itemOpenBtn.setOnAction(e -> {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("../RecipeView/RecipeView.fxml"));
				CookBook.setCurrentRecipe(recipe);
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

			// Edit Button
			itemEditBtn.setOnAction(e -> {

				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("../EditPage/EditPageView.fxml"));
				CookBook.setCurrentRecipe(recipe);
				try {
					Parent parent = fxmlLoader.load();
					EditViewController controller = fxmlLoader.getController();
					controller.setRecipeDetails(recipe);
					
					Scene scene = new Scene(parent);
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					currentStage.setScene(scene);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
			itemAccess.put(itemDeleteBtn, item);
			recipeAccess.put(item, recipe);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		
		//Get the user's own recipes
		Collection<Recipe> allRecipes = CookBook.getRecipesList().values();
		for (Recipe recipe : allRecipes) {
			if(recipe.getUid() == currentUser.getUid())
				myRecipes.add(recipe);
		}
		setMyRecipeList(myRecipes);
		
		//Add delete listener to the delete button
		for (Button itemDeleteBtn : itemAccess.keySet()) {
			HBox item = itemAccess.get(itemDeleteBtn);
			Recipe recipe = recipeAccess.get(item);
			itemDeleteBtn.setOnAction(e -> {
				listVBox.getChildren().remove(item);
				myRecipes.remove(recipe);
				dBProcessor.deleteRecipe(currentUser.getUid());
			});
		}
	}


}
