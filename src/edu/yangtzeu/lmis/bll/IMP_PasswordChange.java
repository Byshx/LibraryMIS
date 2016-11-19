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

	// 提示框
	private PopMessage message = new PopMessage();

	private String newPassword = null;

	/**
	 * 判断用户是否关闭了修改窗口而没有更改密码
	 */
	private boolean CLOSE_WINDOW_NORMAL = true;

	public void display(String password) {
		Stage window = new Stage();
		window.setTitle("密码修改");
		// modality要使用Modality.APPLICATION_MODEL
		window.initModality(Modality.APPLICATION_MODAL);
		// 获得主程序图标
		window.getIcons().add(Login.ShowPlatform.getIcons().get(0));
		window.initStyle(StageStyle.UNDECORATED);
		window.setOpacity(0.7);
		window.setMinWidth(300);
		window.setMinHeight(150);

		Label label_oldPassword = new Label("旧密码:");
		label_oldPassword.setPadding(new Insets(0, 35, 0, 0));
		PasswordField passwordField_oldPassword = new PasswordField();

		Label label_newPassword = new Label("新密码:");
		label_newPassword.setPadding(new Insets(0, 35, 0, 0));
		PasswordField passwordField_newPassword = new PasswordField();

		Label label_confirmPassword = new Label("确认密码:");
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

		// 按钮
		Button ok = new Button("确定");
		ok.setOnAction(e -> {
			if (!passwordField_oldPassword.getText().equals(password)) {
				message.showMessage("消息", "密码输入错误！");
				message.showAndWait();
			} else {
				if (passwordField_newPassword.getText().equals("")) {
					message.showMessage("消息", "请输入新密码！");
					message.showAndWait();
				} else if (!passwordField_newPassword.getText().equals(passwordField_confirmPassword.getText())) {
					message.showMessage("消息", "两次输入密码不一致！");
					message.showAndWait();
				} else {
					newPassword = passwordField_newPassword.getText();
					window.close();
				}
			}
		});
		Button close = new Button("关闭");
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
		// 设置背景色
		layout.setStyle("-fx-background: #000000;");

		Scene scene = new Scene(layout);
		window.setScene(scene);
		// 使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
		window.showAndWait();
	}

	/*
	 * 获取新密码
	 */
	public String getNewPassword() {
		return newPassword;
	}

	public boolean getCloseWay() {
		return CLOSE_WINDOW_NORMAL;
	}
}
