package digital.cookbook.mkk.MyFavoriteView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;


import digital.cookbook.mkk.CookBook;
import digital.cookbook.mkk.RecipeView.RecipeViewController;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.layout.VBox;

import javafx.scene.layout.Pane;

public class MyFavoriteController {
	@FXML
	private Pane mainPane;
	@FXML
	private Text myFavor;
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
	private VBox listVBox;


	@FXML
	public void deleteRecipeEvent(ActionEvent event) {
		
	}

	@FXML
	public void openRecipeEvent(ActionEvent event) {
		
	}
	
	/**
	 * Jump to my recipe list
	 * @param event
	 */
	@FXML
	public void jumpToMyRecipeEvent(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../MyRecipeView/MyRecipeView.fxml"));
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


}
