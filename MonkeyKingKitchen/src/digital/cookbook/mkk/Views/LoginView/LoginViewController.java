/**
 * 
 */
package digital.cookbook.mkk.Views.LoginView;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;

/**
 * @author Zifeng Zhang
 *
 */
public class LoginViewController implements Initializable {

	@FXML
	private Button registerBtn;

	@FXML
	private TextField loginUsernameTxtField;

	@FXML
	private Button loginBtn;

	@FXML
	private Tab registerTab;

	@FXML
	private PasswordField registerPasswdTxtField;

	@FXML
	private Tab loginTab;

	@FXML
	private TextField registerUsernameTxtField;

	@FXML
	private PasswordField loginPasswdTxtField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		loginBtn.setOnAction(e -> {
			
		});
		
	}

}
