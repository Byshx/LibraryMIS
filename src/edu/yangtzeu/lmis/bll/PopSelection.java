package edu.yangtzeu.lmis.bll;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class PopSelection extends Dialog<ButtonType> {
	/**
	 * Ñ¡ÔñÌáÊ¾¿ò
	 */
	public PopSelection() {
		getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
	}

	public void showMessage(String title, String message) {
		setTitle(title);
		getDialogPane().setContentText(message);
	}

}
