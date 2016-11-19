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
	// 提示框
	private PopMessage message = null;

	/**
	 * 创建数据库连接单例
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
	 * 触发的登录事件
	 */
	private void LoginAction() {
		String admin = Admin.getText();
		String password = Password.getText();
		if (admin.equals("") || password.equals("")) {
			message.showMessage("消息", "输入不能为空");
			message.showAndWait();
		} else {
			if (!connectDB.getStatus()) {
				message.showMessage("消息", "未连接");
				message.showAndWait();
				return;
			}
			try {
				Integer.parseInt(admin);
			} catch (Exception e) {
				// TODO: handle exception
				message.showMessage("消息", "用户名为数字");
				message.showAndWait();
				return;
			}
			if (!connectDB.GetTable("SELECT * FROM Library.dbo.TB_Reader WHERE rdID = " + "'" + admin + "'")) {
				message.showMessage("消息", "登陆失败");
				message.showAndWait();
				return;
			}
			ResultSet result = connectDB.getResult();
			try {
				/**
				 * User Validation
				 */
				if (!result.next()) {
					message.showMessage("消息", "用户不存在");
					message.showAndWait();
					return;
				}

				/**
				 * Password Validation
				 */
				if (!result.getString(12).equals(password)) {
					message.showMessage("消息", "密码错误");
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
				// 未规定权限,安读者处理
				if (Login_Reader.getRdAdminRoles() == null) {
					IMP_BookIO.setOps(1);
					UI_Factory.BookIO.showUI(Login.ShowPlatform, "借阅管理");
				}

				else if (EnumAdminRole.LendCardManager.Cmp(null, Integer.parseInt(Login_Reader.getRdAdminRoles()))) {
					UI_Factory.ReaderOp.showUI(Login.ShowPlatform, "读者管理");
				} else if (EnumAdminRole.LendManager.Cmp(null, Integer.parseInt(Login_Reader.getRdAdminRoles()))) {
					UI_Factory.BookIO.showUI(Login.ShowPlatform, "借阅管理");
				} else if (EnumAdminRole.BookManager.Cmp(null, Integer.parseInt(Login_Reader.getRdAdminRoles()))) {
					UI_Factory.BookSearch.showUI(Login.ShowPlatform, "图书信息管理");
				} else if (EnumAdminRole.SystemManager.Cmp(null, Integer.parseInt(Login_Reader.getRdAdminRoles()))) {
					UI_Factory.UserPowerManagement.showUI(Login.ShowPlatform, "权限管理");
				} else if (EnumAdminRole.Reader.Cmp(null, Integer.parseInt(Login_Reader.getRdAdminRoles()))) {
					// 读者权限:受限制的借阅管理
					IMP_BookIO.setOps(1);
					UI_Factory.BookIO.showUI(Login.ShowPlatform, "借阅管理");
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
