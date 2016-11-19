package edu.yangtzeu.lmis.bll;

import java.sql.ResultSet;
import java.sql.SQLException;
import edu.yangtzeu.lmis.dal.ConnectDB;
import edu.yangtzeu.lmis.gui.Login;
import edu.yangtzeu.lmis.gui.UI_Factory;
import edu.yangtzeu.lmis.model.Reader;
import javafx.application.Platform;
import javafx.fxml.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class IMP_Login {

	@FXML
	private TextField Admin;
	@FXML
	private PasswordField Password;
	@FXML
	private TextField Top_Choice_Name;
	// ��ʾ��
	private PopMessage message = null;

	/**
	 * �������ݿ����ӵ���
	 */
	private ConnectDB connectDB = ConnectDB.initInstance();

	private static Reader Login_Reader = null;

	public static Reader getLoginReader() {
		return Login_Reader;
	}

	public void initialize() {
		message = new PopMessage();
	}

	@FXML
	public void Button_Login(MouseEvent event) {
		LoginAction();
	}

	/**
	 * Cancel the application
	 * 
	 * @param event
	 * 
	 */
	@FXML
	public void Button_Exit(MouseEvent event) {
		Platform.exit();
	}

	@FXML
	public void TypedEnter(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			LoginAction();
	}

	/**
	 * �����ĵ�¼�¼�
	 */
	private void LoginAction() {
		String admin = Admin.getText();
		String password = Password.getText();
		if (admin.equals("") || password.equals("")) {
			message.showMessage("��Ϣ", "���벻��Ϊ��");
			message.showAndWait();
		} else {
			if (!connectDB.getStatus()) {
				message.showMessage("��Ϣ", "δ����");
				message.showAndWait();
				return;
			}
			try {
				Integer.parseInt(admin);
			} catch (Exception e) {
				// TODO: handle exception
				message.showMessage("��Ϣ", "�û���Ϊ����");
				message.showAndWait();
				return;
			}
			if (!connectDB.GetTable("SELECT * FROM Library.dbo.TB_Reader WHERE rdID = " + "'" + admin + "'")) {
				message.showMessage("��Ϣ", "��½ʧ��");
				message.showAndWait();
				return;
			}
			ResultSet result = connectDB.getResult();
			try {
				/**
				 * User Validation
				 */
				if (!result.next()) {
					message.showMessage("��Ϣ", "�û�������");
					message.showAndWait();
					return;
				}

				/**
				 * Password Validation
				 */
				if (!result.getString(12).equals(password)) {
					message.showMessage("��Ϣ", "�������");
					message.showAndWait();
					Password.setText("");
					return;
				}
				Login_Reader = new Reader();
				Login_Reader.setValue(connectDB.getResult());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				// δ�涨Ȩ��,�����ߴ���
				if (Login_Reader.getRdAdminRoles() == null) {
					IMP_BookIO.setOps(1);
					UI_Factory.BookIO.showUI(Login.ShowPlatform, "���Ĺ���");
				}

				else if (EnumAdminRole.LendCardManager.Cmp(null, Integer.parseInt(Login_Reader.getRdAdminRoles()))) {
					UI_Factory.ReaderOp.showUI(Login.ShowPlatform, "���߹���");
				} else if (EnumAdminRole.LendManager.Cmp(null, Integer.parseInt(Login_Reader.getRdAdminRoles()))) {
					UI_Factory.BookIO.showUI(Login.ShowPlatform, "���Ĺ���");
				} else if (EnumAdminRole.BookManager.Cmp(null, Integer.parseInt(Login_Reader.getRdAdminRoles()))) {
					UI_Factory.BookSearch.showUI(Login.ShowPlatform, "ͼ����Ϣ����");
				} else if (EnumAdminRole.SystemManager.Cmp(null, Integer.parseInt(Login_Reader.getRdAdminRoles()))) {
					UI_Factory.UserPowerManagement.showUI(Login.ShowPlatform, "Ȩ�޹���");
				} else if (EnumAdminRole.Reader.Cmp(null, Integer.parseInt(Login_Reader.getRdAdminRoles()))) {
					// ����Ȩ��:�����ƵĽ��Ĺ���
					IMP_BookIO.setOps(1);
					UI_Factory.BookIO.showUI(Login.ShowPlatform, "���Ĺ���");
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
