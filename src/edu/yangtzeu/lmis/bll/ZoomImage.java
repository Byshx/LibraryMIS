package edu.yangtzeu.lmis.bll;

import edu.yangtzeu.lmis.gui.Login;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ZoomImage {

	@FXML
	private Pane ZoomImage;

	private ImageView photo = null;

	private PopMessage message = new PopMessage();

	public ZoomImage(ImageView photo) {
		this.photo = photo;
	}

	public void display() {
		Stage window = new Stage();
		window.setTitle("�鿴��ͼ");
		// modalityҪʹ��Modality.APPLICATION_MODEL
		window.initModality(Modality.APPLICATION_MODAL);
		// ���������ͼ��
		window.getIcons().add(Login.ShowPlatform.getIcons().get(0));
		window.initStyle(StageStyle.UNDECORATED);
		window.setOpacity(0.7);
		window.setMinWidth(1400);
		window.setMinHeight(668);

		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		window.setX((primScreenBounds.getWidth() - window.getWidth()) / 2);
		window.setY((primScreenBounds.getHeight() - window.getHeight()) / 2);

		Pane layout = new Pane();

		photo.setFitWidth(200);
		photo.setFitHeight(260);
		//photo.setLayoutX(window.getWidth() / 2 - photo.getFitWidth() / 2);
		//photo.setLayoutX(window.getHeight() / 2 - photo.getFitHeight() / 2);
		layout.getChildren().add(photo);

		// ���ñ���ɫ
		layout.setStyle("-fx-background: #000000;");
		Scene scene = new Scene(layout);
		scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode().equals(KeyCode.ESCAPE)) {
				window.close();
			}
		});

		window.setScene(scene);
		// ʹ��showAndWait()�ȴ���������ڣ������������main�е��Ǹ����ڲ�����Ӧ
		window.showAndWait();
	}

}
