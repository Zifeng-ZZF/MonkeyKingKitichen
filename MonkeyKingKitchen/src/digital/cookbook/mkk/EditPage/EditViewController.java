package digital.cookbook.mkk.EditPage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditViewController implements Initializable {
	
    @FXML
    private TextArea stepsTxtArea;

    @FXML
    private TextField prepareTimeTxtField;

    @FXML
    private ComboBox<?> ingredientComBox;

    @FXML
    private TextField servingTxtField;

    @FXML
    private TextField cookTimeTxtField1;

    @FXML
    private TextField unitTxtField;

    @FXML
    private Label recipeName;

    @FXML
    private Button addProcessBtn;

    @FXML
    private TextField recipeNameTxtField;

    @FXML
    private TextArea IngredientsTxtArea;

    @FXML
    private TextField cookTimeTxtField;

    @FXML
    private TextField amountTxtField;

    @FXML
    private TextField processTxtField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
