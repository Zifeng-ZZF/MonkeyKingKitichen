package digital.cookbook.mkk.EditPage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.Ingredient;
import digital.cookbook.mkk.Recipe;
import digital.cookbook.mkk.User;
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
 * @author Xinyue Shi, Zifeng Zhang
 *
 */
public class EditViewController implements Initializable {
	
	private DBProcessor dbProcessor = new DBProcessor();
	private User currentUser = CookBook.getCurrentUser();
	private ArrayList<Ingredient> addedIngredients = new ArrayList<Ingredient>();
	
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
	 * Add an ingredient item to the recipe and show on the view
	 * @param e
	 */
	
	public void addBtnAction(ActionEvent e) {
			String name = ingredientCb.getSelectionModel().getSelectedItem().toString();
			boolean isAdded = false;
			//Warn the user if the ingredients have been added
			for(int i=0; i<addedIngredients.size();i++){
				if(addedIngredients.get(i).getName().equals(name)) {
				    Alert alert = new Alert(AlertType.ERROR);
					alert.titleProperty().set("Alert");
					alert.headerTextProperty().set("Duplicate Action");
					alert.contentTextProperty().set("You have already added this ingredient!");
					alert.showAndWait();
					isAdded = true;
					break;
				}
			}
			if(!isAdded) {
				double amount = Double.parseDouble(amountTxtField.getText());
				String unit = unitTxtField.getText();
				String processMethod = processTxtField.getText();
				Ingredient newIngredient = new Ingredient(name, amount, unit, processMethod);
				this.addedIngredients.add(newIngredient);
				
				String ingredientDesc = null;

				if(!processMethod.equals("")) 
					ingredientDesc = name+" * "+amount+" "+unit+" : "+processMethod;
				else 
					ingredientDesc = name+" * "+amount+" "+unit;
				
				HBox item = new HBox();
				Label ingredientTxt = new Label(ingredientDesc);
				Button removeBtn = new Button("REMOVE");
				item.getChildren().add(ingredientTxt);
				item.getChildren().add(removeBtn);
				
				ingredientVBox.getChildren().add(item);
				//Listen to every remove buttons
				removeBtn.setOnAction(ActionEvent->{
					ingredientVBox.getChildren().remove(item);
					addedIngredients.remove(newIngredient);
				});
			}
	}
	
	/**
	 * Save the ingredient
	 * @param e
	 */
	public void saveBtnAction(ActionEvent e) {
		//Retrieve info from the UI
		String name = recipeNameTxtField.getText();
		String type = typeTxtField.getText();
		int servings = Integer.parseInt(servingsTxtField.getText());
		Recipe recipe = new Recipe(name, type, servings);
		recipe.setPreparationTime(Integer.parseInt(prepareTimeTxtField.getText()));
		recipe.setCookingTime(Integer.parseInt(cookingTimeTxtField.getText()));
		String[] steps = stepsTxtArea.getText().split("\n");
		for(String step : steps)
			recipe.addPreparationStep(step);
		
		//Insert into DB and pop up informing
		dbProcessor.insertRecipe(recipe, 5);
		
		for(Ingredient ingredient : this.addedIngredients) {
			recipe.addIngredient(ingredient);
			ingredient.setRecipeId(recipe.getRecipeId());
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
	
	/**
	 * Canceling edit and return to my recipe list
	 * @param e
	 */
	public void cancelBtnAction(ActionEvent e) {
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<String> allIngredients = dbProcessor.fetchIngredients();
		for(String ingredientName: allIngredients)
			ingredientCb.getItems().add(ingredientName);
	}

}
