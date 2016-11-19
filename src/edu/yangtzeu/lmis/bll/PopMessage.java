package edu.yangtzeu.lmis.bll;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class PopMessage extends Dialog<String> {

	/**
	 * 消息提示框
	 */

	public PopMessage() {
		getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
	}

	public void showMessage(String title, String message) {
		setTitle(title);
		getDialogPane().setContentText(message);
	}
	
}
