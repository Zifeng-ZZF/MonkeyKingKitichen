package digital.cookbook.mkk.EditPage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.Ingredient;
import digital.cookbook.mkk.Recipe;
import digital.cookbook.mkk.User;
import digital.cookbook.mkk.MyRecipeView.MyRecipeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Edit page controller
 * 
 * @author Xinyue Shi, Zifeng Zhang
 *
 */
public class EditViewController implements Initializable {

	private DBProcessor dbProcessor = new DBProcessor();
	private User currentUser;
	private ArrayList<Ingredient> recipeIngredients = new ArrayList<>();
	private Map<Button, HBox> itemAcess = new HashMap<>();
	private Map<HBox, Ingredient> ingredientAccess = new HashMap<>();
	private ArrayList<Recipe> myRecipes = new ArrayList<>();
	
	private boolean servingValid = false;
	private boolean cookingTimeValid = false;
	private boolean prepTimeValid = false;
	private boolean amountValid = true;

	@FXML
	private Label EditLb;

	@FXML
	private Label recipeNameLb;

	@FXML
	private Label servingsLb;

	@FXML
	private Label preparaTimeLb;

	@FXML
	private Label cookingTimeLb;

	@FXML
	private Label typeLb;

	@FXML
	private Label ingredientsLb;

	@FXML
	private Button addBtn;

	@FXML
	private Button cancelBtn;

	@FXML
	private Button saveBtn;

	@FXML
	private TextField recipeNameTxtField;

	@FXML
	private TextField prepareTimeTxtField;

	@FXML
	private TextField cookingTimeTxtField;

	@FXML
	private TextField servingsTxtField;

	@FXML
	private TextField amountTxtField;

	@FXML
	private TextField processTxtField;

	@FXML
	private TextField typeTxtField;

	@FXML
	private TextField unitTxtField;

	@FXML
	private VBox ingredientVBox;

	@FXML
	private TextArea stepsTxtArea;

	@FXML
	private ComboBox<String> ingredientCb;

	/**
	 * Initialize the editing page of an existing recipe
	 * 
	 * @param recipe
	 */
	public void setRecipeDetails(Recipe recipe) {
		this.recipeNameTxtField.setText(recipe.getName());
		this.prepareTimeTxtField.setText(String.valueOf(recipe.getPreparationTime()));
		this.servingsTxtField.setText(String.valueOf(recipe.getServings()));
		this.servingsTxtField.setText(String.valueOf(recipe.getServings()));
		this.cookingTimeTxtField.setText(String.valueOf(recipe.getCookingTime()));

		String step = "";
		for (String str : recipe.getPreparationSetps()) {
			step += str;
		}
		this.stepsTxtArea.setText(step + "\n");

		for (Ingredient ingredient : recipe.getIngredients()) {
			addIngredientHBox(ingredient);
			recipeIngredients.add(ingredient);
		}
		
		// ingredients
		dbProcessor.deleteIngredient(recipe.getRecipeId());
		recipe.getIngredients().clear();
		
	}

	/**
	 * Add a ingredient to the interface
	 * 
	 * @param ingredient
	 */
	public void addIngredientHBox(Ingredient ingredient) {
		String ingredientDesc = ingredient.getName() + " * " + ingredient.getAmount() + " " + ingredient.getUnit() + " "
				+ ingredient.getProcessMethod();

		HBox item = new HBox();
		Label ingredientTxt = new Label(ingredientDesc);
		Button deleteBtn = new Button("DELETE");
		item.getChildren().add(ingredientTxt);
		item.getChildren().add(deleteBtn);

		ingredientVBox.getChildren().add(item);
		
		// Listen to every delete buttons
		itemAcess.put(deleteBtn, item);
		ingredientAccess.put(item, ingredient);
	
		setUpDeleteBtn();
	}

	/**
	 * Add an ingredient item to the recipe and show on the view
	 * 
	 * @param e
	 */
	@FXML
	public void addBtnAction(ActionEvent e) {
		String name = ingredientCb.getSelectionModel().getSelectedItem().toString();
		boolean isAdded = false;
		// Warn the user if the ingredients have been added
		for (int i = 0; i < recipeIngredients.size(); i++) {
			if (recipeIngredients.get(i).getName().equals(name)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.titleProperty().set("Alert");
				alert.headerTextProperty().set("Duplicate Action");
				alert.contentTextProperty().set("You have already added this ingredient!");
				alert.showAndWait();
				isAdded = true;
				break;
			}
		}
		if (!isAdded) {
			double amount = Double.parseDouble(amountTxtField.getText());
			String unit = unitTxtField.getText();
			String processMethod = processTxtField.getText();
			Ingredient newIngredient = new Ingredient(name, amount, unit, processMethod);
			this.recipeIngredients.add(newIngredient);
			addIngredientHBox(newIngredient);
		}
	}

	/**
	 * Save the ingredient
	 * 
	 * @param e
	 */
	@FXML
	public void saveBtnAction(ActionEvent e) {
		// Retrieve info from the UI
		String name = recipeNameTxtField.getText();
		String type = typeTxtField.getText();
		int servings = Integer.parseInt(servingsTxtField.getText());

		if (CookBook.getCurrentRecipe() != null) {

			// If is editing save operation
			Recipe currentRecipe = CookBook.getCurrentRecipe();
			currentRecipe.setName(name);
			currentRecipe.setType(type);
			currentRecipe.setServings(servings);
			currentRecipe.setPreparationTime(Integer.parseInt(prepareTimeTxtField.getText()));
			currentRecipe.setCookingTime(Integer.parseInt(cookingTimeTxtField.getText()));
			String[] steps = stepsTxtArea.getText().split("\n");
			currentRecipe.getPreparationSetps().clear();
			for (String step : steps)
				currentRecipe.addPreparationStep(step);

			// Update the recipe in recipetb
			dbProcessor.updateRecipe(currentRecipe);

			// Insert new ingredients and add to DB
			for (Ingredient ingredient : recipeIngredients) {
				// Endow ingredient with the current recipe id;
				currentRecipe.addIngredient(ingredient);
				dbProcessor.addIngredient(ingredient);
			}

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.titleProperty().set("Save");
			alert.headerTextProperty().set("Success");
			alert.contentTextProperty().set("You have successfully updated!");
			alert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {
					ingredientVBox.getChildren().clear();
					cancelBtnAction(e);
					CookBook.setCurrentRecipe(null);
				}
			});

		} else {
			// If it is creating recipe operation
			Recipe recipe = new Recipe(name, type, servings);
			recipe.setPreparationTime(Integer.parseInt(prepareTimeTxtField.getText()));
			recipe.setCookingTime(Integer.parseInt(cookingTimeTxtField.getText()));
			String[] steps = stepsTxtArea.getText().split("\n");
			for (String step : steps)
				recipe.addPreparationStep(step);

			// Insert into DB and pop up informing
			dbProcessor.insertRecipe(recipe, currentUser.getUid());

			for (Ingredient ingredient : recipeIngredients) {
				// Endow ingredient with the current recipe id;
				recipe.addIngredient(ingredient);
				dbProcessor.addIngredient(ingredient);
			}

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.titleProperty().set("Save");
			alert.headerTextProperty().set("Success");
			alert.contentTextProperty().set("You have successfully created a recipe!");
			alert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {
					cancelBtnAction(e);
				}
			});
		}
	}

	/**
	 * Canceling edit and return to my recipe list
	 * 
	 * @param e
	 */
	@FXML
	public void cancelBtnAction(ActionEvent e) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MyRecipeView/MyRecipeView.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
			CookBook.setCurrentRecipe(null);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Register the listeners for all the deleteBtn
	 */
	public void setUpDeleteBtn() {
		for(Button deleteBtn : itemAcess.keySet()) {
			HBox item = itemAcess.get(deleteBtn);
			Ingredient ingredient = ingredientAccess.get(item);
			deleteBtn.setOnAction(e -> {
				recipeIngredients.remove(ingredient);
				ingredientVBox.getChildren().remove(item);
			});
		}
	}
	
	/**
	 * Handle events while openning the page
	 * 1. Grab all the ingredients from the db
	 * 2. Grab all the recipe from the db and match the user's own recipe
	 * 3. Listeners from the focused properties for servings, cookingTime, preparationTime and amount
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		currentUser = CookBook.getCurrentUser();
		
		if(CookBook.getCurrentRecipe() != null) {
			saveBtn.setDisable(false);
			servingValid = true;
			cookingTimeValid = true;
			prepTimeValid = true;
			amountValid = true;
		}else
			saveBtn.setDisable(true);
			
		ArrayList<String> allIngredients = dbProcessor.fetchIngredients();
		for (String ingredientName : allIngredients)
			ingredientCb.getItems().add(ingredientName);
		
		
		Collection<Recipe> allRecipes = CookBook.getRecipesList().values();
		for(Recipe recipe : allRecipes) {
			if(recipe.getUid() == currentUser.getUid())
				myRecipes.add(recipe);
		}
		
		//Focused properties listener
		//Servings numeric input listener
		servingsTxtField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			String content = servingsTxtField.getText().trim();
			if(oldVal) {
				if(content.matches("[0-9]*")) {
					servingsTxtField.setStyle("-fx-border-color:black;"
							+ "-fx-border-width:0 0 1 0;"
							+ "-fx-background-color:#ffffff;");
					servingValid = true;
				}else {
					servingsTxtField.setStyle("-fx-border-color:red;"
							+ "-fx-border-width:0 0 1 0;"
							+ "-fx-text-fill:derive(red,.3);");
					servingValid = false;
				}
				
				if(servingValid && cookingTimeValid && prepTimeValid && amountValid)
					saveBtn.setDisable(false);
				else
					saveBtn.setDisable(true);
			}
		});
		
		//cookingTime numeric input listener
		cookingTimeTxtField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			String content = cookingTimeTxtField.getText().trim();
			if(oldVal) {
				if(content.matches("[0-9]*")) {
					cookingTimeTxtField.setStyle("-fx-border-color:black;"
							+ "-fx-border-width:0 0 1 0;"
							+ "-fx-background-color:#ffffff;");
					cookingTimeValid = true;
				}else {
					cookingTimeTxtField.setStyle("-fx-border-color:red;"
							+ "-fx-border-width:0 0 1 0;"
							+ "-fx-text-fill:derive(red,.3);");
					cookingTimeValid = false;
				}
				
				if(servingValid && cookingTimeValid && prepTimeValid && amountValid)
					saveBtn.setDisable(false);
				else
					saveBtn.setDisable(true);
			}
		});
		
		//preparation time numeric input listener
		prepareTimeTxtField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			String content = prepareTimeTxtField.getText().trim();
			if(oldVal) {
				if(content.matches("[0-9]*")) {
					prepareTimeTxtField.setStyle("-fx-border-color:black;"
							+ "-fx-border-width:0 0 1 0;"
							+ "-fx-background-color:#ffffff;");
					prepTimeValid = true;
				}else {
					prepareTimeTxtField.setStyle("-fx-border-color:red;"
							+ "-fx-border-width:0 0 1 0;"
							+ "-fx-text-fill:derive(red,.3);");
					prepTimeValid = false;
				}
				if(servingValid && cookingTimeValid && prepTimeValid && amountValid)
					saveBtn.setDisable(false);
				else
					saveBtn.setDisable(true);
			}
		});
		
		//amount numeric input listener
		amountTxtField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			String content = amountTxtField.getText().trim();
			if(oldVal) {
				if(content.matches("[0-9]*")) {
					amountTxtField.setStyle("-fx-border-color:black;"
							+ "-fx-border-width:0 0 1 0;"
							+ "-fx-background-color:#ffffff;");
					amountValid = true;
				}else {
					amountTxtField.setStyle("-fx-border-color:red;"
							+ "-fx-border-width:0 0 1 0;"
							+ "-fx-text-fill:derive(red,.3);");
					amountValid = false;
				}
				if(servingValid && cookingTimeValid && prepTimeValid && amountValid)
					saveBtn.setDisable(false);
				else
					saveBtn.setDisable(true);
			}
		});
	}

}
