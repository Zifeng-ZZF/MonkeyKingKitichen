package digital.cookbook.mkk.ResultView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import digital.cookbook.mkk.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ResultViewController implements Initializable{
	
	private ArrayList<Recipe> results;
	
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
    private Label exitLabel1;

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
    public void intializeResult(ArrayList<Recipe> results) {
    	this.results = results;
    	for (Recipe recipe : results) {
    		itemName = new Label(recipe.getName());
    		itemDesc = new Label(recipe.getType()+", Cooking time: " +
    				recipe.getCookingTime() + ", Preparing time: " + recipe.getPreparationTime() + ", Servings: "+recipe.getServings());
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
		// TODO Auto-generated method stub
		
	}

}
