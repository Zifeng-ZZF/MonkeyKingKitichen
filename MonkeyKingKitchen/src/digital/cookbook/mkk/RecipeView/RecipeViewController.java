package digital.cookbook.mkk.RecipeView;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

public class RecipeViewController {
    @FXML
    private TextArea ingredientsTxtArea;

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
}
