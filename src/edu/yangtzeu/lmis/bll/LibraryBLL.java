package edu.yangtzeu.lmis.bll;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class LibraryBLL {
	
	// TopBarÊÂ¼þÏìÓ¦
	@FXML
	public abstract void ReaderManagement(ActionEvent event);

	@FXML
	public abstract void ReaderTypeOp(ActionEvent event);

	@FXML
	public abstract void NewbookIn(ActionEvent event);

	@FXML
	public abstract void LibInfoManagement(ActionEvent event);

	@FXML
	public abstract void BorrowManagement(ActionEvent event);

	@FXML
	public abstract void PowerManagement(ActionEvent event);

	@FXML
	public abstract void PasswordModify(ActionEvent event);
}
