package digital.cookbook.mkk.RecipeView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.DBProcessor;
import digital.cookbook.mkk.PdfProcess;
import digital.cookbook.mkk.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class RecipeViewController implements Initializable {
	
	private Recipe currentRecipe;

	private PdfProcess pdfProcessor;

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
	public void setRecipeDetail() {

		this.recipeNameLb.setText(currentRecipe.getName());
	
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pdfProcessor = new PdfProcess();
		currentRecipe = CookBook.getCurrentRecipe();
	}
}
