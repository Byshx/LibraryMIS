package edu.yangtzeu.lmis.gui;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public enum UI_Factory {

	LOGIN("Main.fxml"), ReaderOp("ReaderOp.fxml"),BookIO("BookIO.fxml")
	,BookManagement("BookManagement.fxml"),BookSearch("BookSearch.fxml"),
	ReaderTypeOp("ReaderTypeOp.fxml"),UserPowerManagement("UserPowerManagement.fxml");

	/**
	 * The URL of needed FXML
	 */
	private String URL_FXML;

	private UI_Factory(String URL_FXML) {
		this.URL_FXML = URL_FXML;
	}

	/**
	 * Use the FXML file to create new Scene
	 * 
	 * @return Scene
	 */
	private Scene getScene() {
		Parent root = null;
		Scene scene = null;
		try {
			root = FXMLLoader.load(getClass().getResource(URL_FXML));
			scene = new Scene(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scene;
	}

	/**
	 * Generate stage to show
	 * 
	 * @param stage
	 * @param stageName
	 * @return Stage
	 */

	public Stage showUI(Stage stage, String stageName) {
		stage.setScene(getScene());
		stage.centerOnScreen();
		stage.setTitle(stageName);
		stage.show();
		return stage;
	}
}
