package digital.cookbook.mkk.RecipeView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.Ingredient;
import digital.cookbook.mkk.PdfProcess;
import digital.cookbook.mkk.Recipe;
import digital.cookbook.mkk.User;
import digital.cookbook.mkk.MainPageView.MainPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class RecipeViewController implements Initializable{
	private DBProcessor dbp = new DBProcessor();
	private Recipe currentRecipe;
	private User currentUser;
	
    @FXML
    private TextArea ingredientsTxtArea;

    @FXML
    private TextField servingsTxtField;
    
    @FXML
    private Label servingsLb;

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
    
    public void setPreparationTimeLb() {
    	preparationTimeLb.setText("Preparation Time : "+String.valueOf(currentRecipe.getPreparationTime()));
    }
    
    public void setRateLb() {
    	rateLb.setText("Rate : "+String.valueOf(currentRecipe.getRate()));
    }
    
    public void setCookingTimeLb() {
    	cookingTimeLb.setText("Cooking Time : "+String.valueOf(currentRecipe.getCookingTime()));
    }
    
    public void setStepsTxtArea(){
    	ArrayList<String> steps = currentRecipe.getPreparationSetps();
    	String buffer = "";
    	for(String step : steps) {
             	buffer = buffer+step + "\r\n";	    
    	}
    	stepsTxtArea.setText(buffer);
    }
    
    public void setIngredients() {
    	ArrayList<Ingredient> ingredients = currentRecipe.getIngredients();
    	String text = "";
    	for(Ingredient ingredient : ingredients) {
    		String ingredientTxt = ingredient.getName()+" * "+ingredient.getAmount()+" "+ingredient.getUnit();
    		if(ingredient.getProcessMethod()!=null)
    			text = text+ingredientTxt+" : "+ingredient.getProcessMethod()+"\n\r";
    		else 
    			text = text+ingredientTxt+"\n\r";
    	}
    	ingredientsTxtArea.setText(text);
    }
    
    public void setServingsLb() {
    	servingsTxtField.setText(String.valueOf(currentRecipe.getServings()));
    }
    
    /**
	 * Change amount of preparation time, ingredients amount and cooking time
	 * according to servings
	 * 
	 * @param Servings
	 * @return the altered recipe
	 */
    public Recipe changeServings(int servings) {
    	Recipe recipe = this.currentRecipe;
    	int originalServings = currentRecipe.getServings();
		int preparationTime = servings * (currentRecipe.getPreparationTime()) / originalServings;
		int cookingTime = servings * (currentRecipe.getCookingTime()) / originalServings;
		recipe.setPreparationTime(preparationTime);
		recipe.setCookingTime(cookingTime);
		for (Object ingredientObj : currentRecipe.getIngredients()) {
			double amount = servings * (((Ingredient) ingredientObj).getAmount()) / originalServings;
			((Ingredient) ingredientObj).setAmount(amount);
		}
		return recipe;
    }
    
    /**
     * handle the performance after changing servings
     * based on method changeServings
     * @param e
     */
    public void changeServingsAciton(ActionEvent e) {
    	System.out.println("The number of Servings has been changed.");
    	int servings = Integer.parseInt(servingsTxtField.getText());
    	this.currentRecipe = changeServings(servings);
    	currentRecipe.setServings(servings);
    	setCookingTimeLb();
    	setPreparationTimeLb();
    	setIngredients();
    }
    
    /**
     * handle the "Export PDF" button
     * based on class PdfProcess
     * path is set to be chosen by the user
     * @param e
     */
    public void exportPdfAction(ActionEvent e) {
    	PdfProcess pdfp = new PdfProcess();
    	
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Save PDF");
    	File file = fileChooser.showSaveDialog(new Stage());
    	String path = file.getAbsolutePath();
    	
    	/**
    	 * check if user has typed the file extension
    	 */
    	String index = path.substring(path.length()-4);
    	if(index.equals(".pdf"))
    		pdfp.exportPDF(currentRecipe,file.getAbsolutePath());
    	else
    		pdfp.exportPDF(currentRecipe,file.getAbsolutePath()+".pdf");
    }
    
	public void addToFavoriteAction(ActionEvent e) {
    	ArrayList<Recipe> recipes = currentUser.getMyFavoriteList();
    	if(recipes.isEmpty()==true) {
    		System.out.println("success");
			dbp.insertIntoFavorite(currentRecipe, currentUser.getUid());
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
    				dbp.insertIntoFavorite(currentRecipe, currentUser.getUid());
    			}
    		}
    	}
    }
    
	public void returnToHomepage(ActionEvent e) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MainPageView/MainPageView.fxml"));
		try {
			Scene scene = new Scene(fxmlLoader.load());
			//set the name tag
			MainPageController controller = fxmlLoader.getController();
			controller.setUserTag(currentUser.getName());
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
	}
	
	public void rateAction() {
		
		double rate = rateSlider.getValue();
		dbp.insertRate(currentRecipe,currentUser.getUid(),rate);
		rateLb.setText(String.valueOf(currentRecipe.getRate()));
	}
	
    public void initialize(URL location, ResourceBundle resources) {
    	currentRecipe = CookBook.getCurrentRecipe();
    	currentUser = CookBook.getCurrentUser();
    	recipeNameLb.setText(currentRecipe.getName());
    	setRateLb();
    	setPreparationTimeLb();
    	setCookingTimeLb();
     	setStepsTxtArea();
     	setIngredients();
    }
}
