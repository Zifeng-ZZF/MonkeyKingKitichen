package digital.cookbook.mkk.MyRecipeView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.layout.VBox;

import javafx.scene.layout.Pane;

public class MyRecipeController {
	@FXML
	private Pane mainPane;
	@FXML
	private Text myFavoriteLabel;
	@FXML
	private Button deleteButton;
	@FXML
	private Button openButton;
	@FXML
	private Pane rightPane;
	@FXML
	private Label userNameLabel;
	@FXML
	private Button myRecipeButton;
	@FXML
	private Button mainPageButton;
	@FXML
	private Label mkkLogoLabel;
	@FXML
	private Button editButton;
	@FXML
	private Button createButton;
	@FXML
	private VBox listVBox;


	@FXML
	public void deleteEvent(ActionEvent event) {
	
	}

	@FXML
	public void openEvent(ActionEvent event) {
		
	}

	/**
	 * Jump to my favorite recipe list
	 * @param e
	 */
	@FXML
	public void jumpToMyFavoriteEvent(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MyFavoriteView/MyFavoriteView.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Jump to main page
	 * @param event
	 */
	@FXML
	public void jumpToMainPageEvent(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MainPageView/MainPageView.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	
	/**
	 * Jump to edit page view
	 * @param event
	 */
	@FXML
	public void editEvent(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../EidtPage/EditPageView.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setScene(scene);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@FXML
	public void createEvent(ActionEvent event) {
		
	}
}
