package digital.cookbook.mkk.MyFavoriteView;

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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.Recipe;
import digital.cookbook.mkk.User;
import digital.cookbook.mkk.RecipeView.RecipeViewController;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * 
 * @author Zhibin Xin, Zifeng Zhang
 *
 */
public class MyFavoriteController implements Initializable {

	private ArrayList<Recipe> myFavorites;
	private User currentUser;
	private DBProcessor dBProcessor = new DBProcessor();
	private Map<Button, HBox> itemAccess = new HashMap<>();
	private Map<HBox, Recipe> recipeAccess = new HashMap<>();

	@FXML
	private Pane mainPane;
	@FXML
	private Text myFavor;
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

	/**
	 * Jump to my recipe list
	 * 
	 * @param event
	 */
	@FXML
	public void jumpToMyRecipeEvent(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MyRecipeView/MyRecipeView.fxml"));
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
	 * 
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
	 * Get my favorite recipes from main page and list it
	 * 
	 * @param results
	 */
	public void setMyFavoriteList(ArrayList<Recipe> myFavorites) {

		// Add all the items(hbox = name label + description label + button) to the Map
		for (Recipe recipe : myFavorites) {
			itemName = new Label(recipe.getName());
			itemDesc = new Label(recipe.getType() + ", Cooking time: " + recipe.getCookingTime() + ", Preparing time: "
					+ recipe.getPreparationTime() + ", Servings: " + recipe.getServings());
			itemOpenBtn = new Button("OPEN");
			itemDeleteBtn = new Button("DELETE");

			item = new HBox();
			buttonItem = new VBox();
			item.getChildren().add(itemName);
			item.getChildren().add(itemDesc);
			buttonItem.getChildren().add(itemOpenBtn);
			buttonItem.getChildren().add(itemDeleteBtn);
			item.getChildren().add(buttonItem);

			listVBox.getChildren().add(item);

			// Register the listener for all the buttons (open and delete)
			// Open Button
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

			itemAccess.put(itemDeleteBtn, item);
			recipeAccess.put(item, recipe);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.currentUser = CookBook.getCurrentUser();
		this.myFavorites = dBProcessor.fetchFavorite(CookBook.getCurrentUser().getUid());
		setMyFavoriteList(myFavorites);
		
		for (Button itemDeleteBtn : itemAccess.keySet()) {
			HBox item = itemAccess.get(itemDeleteBtn);
			Recipe recipe = recipeAccess.get(item);
			itemDeleteBtn.setOnAction(e -> {
				listVBox.getChildren().remove(item);
				myFavorites.remove(recipe);
				dBProcessor.deleteFavoriteRecipe(recipe.getRecipeId(), currentUser.getUid());
			});
		}
	}

}
