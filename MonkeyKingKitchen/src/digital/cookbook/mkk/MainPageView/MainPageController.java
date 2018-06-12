package digital.cookbook.mkk.MainPageView;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class MainPageController implements Initializable {

    @FXML
    private Label userLabel;

    @FXML
    private ImageView recommendImg;

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
    private Label recommendNameLabel;

    @FXML
    private Label logoutLabel;

    @FXML
    private Label briefStepLabel;

    @FXML
    private Label exitLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}


}
