package digital.cookbook.mkk.RecipeView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.PdfProcess;
import digital.cookbook.mkk.Recipe;
import java.io.IOException;
import java.util.ArrayList;

import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.Ingredient;
import digital.cookbook.mkk.PdfProcess;
import digital.cookbook.mkk.Recipe;
import digital.cookbook.mkk.User;
import digital.cookbook.mkk.MainPageView.MainPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller for recipe view
 * @author Zifeng Zhang, Sheng Ji, Xinyue Shi
 *
 */
public class RecipeViewController implements Initializable{
	private DBProcessor dbProcessor = new DBProcessor();
	private Recipe currentRecipe;
	private User currentUser;
	private PdfProcess pdfProcessor;
	
    @FXML
    private TextField servingsTxtField;
    
    @FXML
    private Label servingsLb;

	@FXML
	private TextArea ingredientsTxtArea;

	@FXML
	private Label rateLb;

	@FXML
	private Label recipeNameLb;

	@FXML
	private Slider rateSlider;

	@FXML
	private TextArea stepsTxtArea;

	@FXML
	private Button addToFavoriteBtn;

	@FXML
	private Button toHomepageBtn;

	@FXML
	private Label preparationTimeLb;

	@FXML
	private Label cookingTimeLb;

	@FXML
	private Button exportPdfBtn;
	
    @FXML
    private Label rateOnLb;


	/**
	 * Button to export the pdf
	 * 
	 * @param e
	 */
	@FXML
	public void exportPDF(ActionEvent e) {
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();

		File dirFile = new File("C:/Users/User/Desktop");
		String recipeName = CookBook.getCurrentRecipe().getName();

		fileChooser.setTitle("export PDF");
		fileChooser.setInitialDirectory(dirFile);
		fileChooser.setInitialFileName(recipeName);
		File selectedFile = fileChooser.showSaveDialog(stage);

		pdfProcessor.exportPDF(currentRecipe, selectedFile.getAbsolutePath() + ".pdf");
	}
	
	/**
	 * Input all the detail of the currentRecipe into the UI
	 */
	@FXML
	public void setRecipeDetail(Recipe recipe) {
		this.recipeNameLb.setText(recipe.getName());
		this.servingsTxtField.setText(""+recipe.getServings());
		this.preparationTimeLb.setText("preparation time : " + recipe.getPreparationTime());
		this.cookingTimeLb.setText("cooking time : " + recipe.getCookingTime());
		setIngredients(recipe);
		setSteps(recipe);
	}  
    
    /**
     * Update or insert the ingredient descrption
     */
	@FXML
    public void setIngredients(Recipe recipe) {
    	ArrayList<Ingredient> ingredients = recipe.getIngredients();
    	String ingredientDesc = "";
    	for(Ingredient ingredient : recipe.getIngredients()) {
    		String ingredientTxt = ingredient.getName()+" * "+ingredient.getAmount()+" "+ingredient.getUnit();
			ingredientDesc += (ingredient.getName()+" * "+ingredient.getAmount()+"  "+ingredient.getUnit()+"  "+ingredient.getProcessMethod());
    	}
    	ingredientsTxtArea.setText(ingredientDesc);
    }
	
	/**
	 * Update or insert the reicpe steps
	 * @param recipe
	 */
	public void setSteps(Recipe recipe) {
		ArrayList<String> steps = recipe.getPreparationSetps();
		String step = "";
		for (String str : steps) {
			step += str;
		}
		this.stepsTxtArea.setText(step+"\n");
	}
    
    /**
	 * Change amount of preparation time, ingredients amount and cooking time
	 * according to servings
	 * 
	 * @param Servings
	 * @return the altered recipe
	 */
    public void handleChangeServings(ActionEvent e) {
    	int originalServings = currentRecipe.getServings();
    	//Get current servings
    	int servings = Integer.parseInt(servingsTxtField.getText());
    	//Change times
		int preparationTime = servings * (currentRecipe.getPreparationTime()) / originalServings;
		int cookingTime = servings * (currentRecipe.getCookingTime()) / originalServings;
		this.preparationTimeLb.setText("preparation time : "+preparationTime);
		this.cookingTimeLb.setText("cooking time : "+cookingTime);
		//Change ingredients
		String ingredientDesc = "";
		for (Ingredient ingredient : currentRecipe.getIngredients()) {
			double amount = servings * ingredient.getAmount() / originalServings;
			ingredientDesc += (ingredient.getName()+" * "+amount+"  "+ingredient.getUnit()+"  "+ingredient.getProcessMethod());
		}
		this.ingredientsTxtArea.setText(ingredientDesc);
    }
    

    /**
     * Add recipe to favorite
     * @param e
     */
	public void addToFavoriteAction(ActionEvent e) {
    	ArrayList<Recipe> recipes = currentUser.getMyFavoriteList();
    	if(recipes.isEmpty()==true) {
    		System.out.println("success");
			dbProcessor.insertIntoFavorite(currentRecipe, currentUser.getUid());
    	}
    	else {
    		for(Recipe recipe : recipes) {
    			
    			/**
    			 * check if user has added this recipe
    			 * to the favorite list
    			 */
    			if(recipe.getRecipeId() == currentRecipe.getRecipeId()) {
    				Alert alert = new Alert(AlertType.WARNING);
    				alert.setTitle("Alert");
    				alert.setHeaderText("Duplicate Action");
    				alert.setContentText("You have already added this recipe to your favorite list!");
    				alert.showAndWait();
    			}
    			else {
    				System.out.println("success");
    				dbProcessor.insertIntoFavorite(currentRecipe, currentUser.getUid());
    			}
    		}
    	}
    }
    
	/**
	 * Return to the main page
	 * @param e
	 */
	public void returnToHomepage(ActionEvent e) {
		//Clear currentRecipe
		CookBook.setCurrentRecipe(null);
		
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MainPageView/MainPageView.fxml"));
		try {
			Scene scene = new Scene(fxmlLoader.load());
			//set the name tag
			MainPageController controller = fxmlLoader.getController();
			controller.setUserTag(currentUser.getName());
			// get the event button (Login button)'s window, which is also a stage
			Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			currentStage.setScene(scene);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	/**
	 * Rate on the recipe
	 */
	public void rateAction() {
		double rate = rateSlider.getValue();
		dbProcessor.insertRate(currentRecipe,currentUser.getUid(),rate);
		rateLb.setText(String.valueOf(currentRecipe.getRate()));
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pdfProcessor = new PdfProcess();
		currentRecipe = CookBook.getCurrentRecipe();
		currentUser = CookBook.getCurrentUser();
	}
}
