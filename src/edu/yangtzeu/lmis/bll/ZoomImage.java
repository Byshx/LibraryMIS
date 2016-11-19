package edu.yangtzeu.lmis.bll;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ZoomImage {

	@FXML
	private Pane ZoomImage;

	private ImageView photo = null;

	public ZoomImage(ImageView photo) {
		this.photo = photo;
	}

	public void display() {
		Stage window = new Stage();
		window.setTitle("查看大图");
		// modality要使用Modality.APPLICATION_MODEL
		// window.initModality(Modality.APPLICATION_MODAL);
		window.initStyle(StageStyle.TRANSPARENT);
		window.centerOnScreen();

		Pane pane = new Pane();
		pane.setPrefWidth(1400);
		pane.setPrefHeight(800);
		pane.setOpacity(0.7);
		pane.setStyle("-fx-background-color: #000000;");

		photo.setFitHeight(photo.getImage().getHeight() / 2);
		photo.setFitWidth(photo.getImage().getWidth() / 2);

		StackPane layout = new StackPane();
		layout.getChildren().add(pane);
		layout.getChildren().add(photo);
		StackPane.setAlignment(photo, Pos.CENTER);
		layout.setStyle("-fx-background-color: null;");
		// StackPane.setAlignment(pane, Pos.BOTTOM_CENTER);

		Scene scene = new Scene(layout, 1400, 800, Color.TRANSPARENT);
		scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode().equals(KeyCode.ESCAPE)) {
				window.close();
			}
		});
		window.setScene(scene);
		// 使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
		window.showAndWait();
	}

}
