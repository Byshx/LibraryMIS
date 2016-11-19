package edu.yangtzeu.lmis.bll;

import edu.yangtzeu.lmis.gui.Login;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class IMP_PasswordChange {

	// ��ʾ��
	private PopMessage message = new PopMessage();

	private String newPassword = null;

	/**
	 * �ж��û��Ƿ�ر����޸Ĵ��ڶ�û�и�������
	 */
	private boolean CLOSE_WINDOW_NORMAL = true;

	public void display(String password) {
		Stage window = new Stage();
		window.setTitle("�����޸�");
		// modalityҪʹ��Modality.APPLICATION_MODEL
		window.initModality(Modality.APPLICATION_MODAL);
		// ���������ͼ��
		window.getIcons().add(Login.ShowPlatform.getIcons().get(0));
		window.initStyle(StageStyle.UNDECORATED);
		window.setOpacity(0.7);
		window.setMinWidth(300);
		window.setMinHeight(150);

		Label label_oldPassword = new Label("������:");
		label_oldPassword.setPadding(new Insets(0, 35, 0, 0));
		PasswordField passwordField_oldPassword = new PasswordField();

		Label label_newPassword = new Label("������:");
		label_newPassword.setPadding(new Insets(0, 35, 0, 0));
		PasswordField passwordField_newPassword = new PasswordField();

		Label label_confirmPassword = new Label("ȷ������:");
		label_confirmPassword.setPadding(new Insets(0, 20, 0, 0));
		PasswordField passwordField_confirmPassword = new PasswordField();

		HBox one = new HBox();
		one.setPadding(new Insets(20, 20, 20, 20));
		one.getChildren().addAll(label_oldPassword, passwordField_oldPassword);

		HBox two = new HBox();
		two.setPadding(new Insets(20, 20, 20, 20));
		two.getChildren().addAll(label_newPassword, passwordField_newPassword);

		HBox three = new HBox();
		three.setPadding(new Insets(20, 20, 20, 20));
		three.getChildren().addAll(label_confirmPassword, passwordField_confirmPassword);

		// ��ť
		Button ok = new Button("ȷ��");
		ok.setOnAction(e -> {
			if (!passwordField_oldPassword.getText().equals(password)) {
				message.showMessage("��Ϣ", "�����������");
				message.showAndWait();
			} else {
				if (passwordField_newPassword.getText().equals("")) {
					message.showMessage("��Ϣ", "�����������룡");
					message.showAndWait();
				} else if (!passwordField_newPassword.getText().equals(passwordField_confirmPassword.getText())) {
					message.showMessage("��Ϣ", "�����������벻һ�£�");
					message.showAndWait();
				} else {
					newPassword = passwordField_newPassword.getText();
					window.close();
				}
			}
		});
		Button close = new Button("�ر�");
		close.setOnAction(e -> {
			CLOSE_WINDOW_NORMAL = false;
			window.close();
		});

		HBox button = new HBox();
		button.setAlignment(Pos.CENTER);
		button.setSpacing(40);
		button.setPadding(new Insets(20, 20, 20, 20));
		button.getChildren().addAll(ok, close);

		VBox layout = new VBox();
		layout.setPadding(new Insets(20, 20, 20, 20));
		layout.getChildren().addAll(one, two, three, button);
		layout.setAlignment(Pos.CENTER);
		// ���ñ���ɫ
		layout.setStyle("-fx-background: #000000;");

		Scene scene = new Scene(layout);
		window.setScene(scene);
		// ʹ��showAndWait()�ȴ���������ڣ������������main�е��Ǹ����ڲ�����Ӧ
		window.showAndWait();
	}

	/*
	 * ��ȡ������
	 */
	public String getNewPassword() {
		return newPassword;
	}

	public boolean getCloseWay() {
		return CLOSE_WINDOW_NORMAL;
	}
}
