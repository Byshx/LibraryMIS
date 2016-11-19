package edu.yangtzeu.lmis.bll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import edu.yangtzeu.lmis.dal.ConnectDB;
import edu.yangtzeu.lmis.dal.ReaderDAL;
import edu.yangtzeu.lmis.gui.Login;
import edu.yangtzeu.lmis.gui.UI_Factory;
import edu.yangtzeu.lmis.model.Reader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class IMP_ReaderOp extends LibraryBLL {

	// TopBar 菜单管理
	@FXML
	private MenuItem ReaderManagement;
	@FXML
	private MenuItem ReaderTypeOp;
	@FXML
	private MenuItem NewbookIn;
	@FXML
	private MenuItem LibInfoManagement;
	@FXML
	private MenuItem BorrowManagement;
	@FXML
	private MenuItem PowerManagement;
	@FXML
	private MenuItem PasswordModify;

	// Right组件
	@FXML
	private Button Right_Button_ConfirmRegister;
	@FXML
	private Button Right_Button_ConfirmChange;
	@FXML
	private Button Right_Button_Cancel;
	@FXML
	private TextField right_Text_CardID;
	@FXML
	private TextField right_Text_Name;
	@FXML
	private TextField right_Text_Password;
	@FXML
	private ChoiceBox<String> right_Text_Sex;
	@FXML
	private TextField right_Text_BorrowBooks;
	@FXML
	private TextField right_Text_Status;
	@FXML
	private TextField right_Text_Role;
	@FXML
	private ChoiceBox<String> right_Text_Type;
	@FXML
	private ChoiceBox<String> right_Text_Department;
	@FXML
	private TextField right_Text_Phone;
	@FXML
	private TextField right_Text_Email;
	@FXML
	private TextField right_Text_TimeOfRegisterCard;

	// Top组件
	@FXML
	private ChoiceBox<String> Top_Choice_Role;
	@FXML
	private ChoiceBox<String> Top_Choice_Department;
	@FXML
	private TextField Top_Choice_Name;

	// Table组件
	@FXML
	private TableView<Reader> Table;
	@FXML
	private TableColumn<Reader, String> Table_ID;
	@FXML
	private TableColumn<Reader, String> Table_Name;
	@FXML
	private TableColumn<Reader, String> Table_Sex;
	@FXML
	private TableColumn<Reader, String> Table_Type;
	@FXML
	private TableColumn<Reader, String> Table_Department;
	@FXML
	private TableColumn<Reader, String> Table_Phone;
	@FXML
	private TableColumn<Reader, String> Table_Email;
	@FXML
	private TableColumn<Reader, String> Table_Status;
	@FXML
	private TableColumn<Reader, String> Table_BorrowBooks;
	@FXML
	private TableColumn<Reader, String> Table_TimeOfRegistor;

	// 图片面板
	@FXML
	private Pane PhotoPane;
	/**
	 * 设置此输入流的目的是接受办理借阅卡时(而不是更新图片时)的图片
	 */
	private InputStream imageStream = null;

	// 获得数据库连接单例
	private ConnectDB connectDB = null;

	// 提示框
	private PopSelection selection = null;

	// 消息框
	private PopMessage message = null;

	// 展示组件平台，从Login中获取
	private Stage stage = null;

	// 获得返回单例
	private ReturnStack back = ReturnStack.getReturnInstance();

	// 内含静态方法，因此只创建一个实例
	@SuppressWarnings("unused")
	private FileChoose filechoose = null;

	/**
	 * 初始化
	 */
	@FXML
	public void initialize() {
		BindColumn();
		stage = Login.ShowPlatform;
		filechoose = new FileChoose();
		selection = new PopSelection();
		message = new PopMessage();
		connectDB = ConnectDB.getDBConnection();

		// 以下操作需要连接数据库
		set_Choice_Role(Top_Choice_Role);
		set_Choice_Department(Top_Choice_Department);
		set_Choice_Role(right_Text_Type);
		set_Choice_Department(right_Text_Department);
	}

	// TopBar事件响应

	@FXML
	public void ReaderManagement(ActionEvent event) {
		// 已在本页，无需判断权限
		back.addReturn(UI_Factory.ReaderOp, "读者管理", 0, 0);
		UI_Factory.ReaderOp.showUI(stage, "读者管理");
	}

	@FXML
	public void ReaderTypeOp(ActionEvent event) {
		// 已在本页，无需判断权限
		back.addReturn(UI_Factory.ReaderOp, "读者管理", 0, 0);
		UI_Factory.ReaderTypeOp.showUI(stage, "读者类别管理");
	}

	@FXML
	public void NewbookIn(ActionEvent event) {
		if (EnumAdminRole.BookManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.ReaderOp, "读者管理", 0, 0);
			IMP_BookManagement.setOps(0);
			UI_Factory.BookManagement.showUI(stage, "新书入库");
		} else {
			message.showMessage("提示", "您没有权限");
			message.showAndWait();
		}
	}

	@FXML
	public void LibInfoManagement(ActionEvent event) {
		if (EnumAdminRole.BookManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.ReaderOp, "读者管理", 0, 0);
			UI_Factory.BookSearch.showUI(stage, "图书信息管理");
		} else {
			message.showMessage("提示", "您没有权限");
			message.showAndWait();
		}
	}

	@FXML
	public void BorrowManagement(ActionEvent event) {
		if (!EnumAdminRole.LendManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			IMP_BookIO.setOps(1);
		}
		back.addReturn(UI_Factory.ReaderOp, "读者管理", 0, 0);
		UI_Factory.BookIO.showUI(stage, "借阅管理");
	}

	@FXML
	public void PowerManagement(ActionEvent event) {
		if (EnumAdminRole.SystemManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.ReaderOp, "读者管理", 0, 0);
			UI_Factory.UserPowerManagement.showUI(stage, "权限管理");
		} else {
			message.showMessage("提示", "您没有权限");
			message.showAndWait();
		}
	}

	@FXML
	public void PasswordModify(ActionEvent event) {
		Reader reader = IMP_Login.getLoginReader();
		IMP_PasswordChange passwordChange = new IMP_PasswordChange();
		passwordChange.display(reader.getRdPwd());
		// 如果是由按确定键关闭窗口，则去获取新密码
		if (passwordChange.getCloseWay()) {			
			ReaderDAL readerDAL = new ReaderDAL(connectDB);
			reader.setRdPwd(passwordChange.getNewPassword());
			readerDAL.setReader(reader);
			if (readerDAL.Update()) {
				message.showMessage("消息", "修改密码成功");
				message.showAndWait();
				return;
			}
			message.showMessage("消息", "修改密码失败");
			message.showAndWait();
		}
	}

	@FXML
	public void Left_Button_RegisterCard(MouseEvent event) {
		Right_Button_ConfirmRegister.disableProperty().set(false);
		Right_Button_ConfirmChange.disableProperty().set(true);
		Right_Button_Cancel.disableProperty().set(false);
		right_Text_CardID.clear();
		right_Text_Name.clear();
		right_Text_Password.clear();
		right_Text_Sex.getSelectionModel().clearSelection();
		right_Text_BorrowBooks.clear();
		right_Text_Status.clear();
		right_Text_Role.clear();
		right_Text_Type.getSelectionModel().clearSelection();
		right_Text_Department.getSelectionModel().clearSelection();
		right_Text_Phone.clear();
		right_Text_Email.clear();
		right_Text_TimeOfRegisterCard.clear();
		PhotoPane.getChildren().clear();
	}

	@FXML
	public void Left_Button_ChangeInfo(MouseEvent event) {
		Right_Button_ConfirmChange.disableProperty().set(false);
		Right_Button_ConfirmRegister.disableProperty().set(true);
		Right_Button_Cancel.disableProperty().set(false);
		CheckInfo();
	}

	@FXML
	public void Left_Button_LossRegistration(MouseEvent event) {
		Reader getReaderData = Table.getSelectionModel().getSelectedItem();
		if (getReaderData == null)
			return;
		selection.showMessage("警告", "确认挂失？");
		Optional<ButtonType> result = selection.showAndWait();
		if (result.get() == ButtonType.NO)
			return;
		getReaderData.setRdStatus("挂失");
		ReaderDAL readerDAL = new ReaderDAL(connectDB);
		readerDAL.setReader(getReaderData);
		if (readerDAL.Update()) {
			message.showMessage("消息", "挂失成功");
			message.showAndWait();
			return;
		}
		message.showMessage("消息", "挂失失败");
		message.showAndWait();
	}

	@FXML

	public void Left_Button_TerminateLossRegistration(MouseEvent event) {
		Reader getReaderData = Table.getSelectionModel().getSelectedItem();
		if (getReaderData == null)
			return;
		selection.showMessage("警告", "确认解除挂失？");
		Optional<ButtonType> result = selection.showAndWait();
		if (result.get() == ButtonType.NO)
			return;
		getReaderData.setRdStatus("有效");
		ReaderDAL readerDAL = new ReaderDAL(connectDB);
		readerDAL.setReader(getReaderData);
		if (readerDAL.Update()) {
			message.showMessage("消息", "已解除挂失");
			message.showAndWait();
			return;
		}
		message.showMessage("消息", "解除失败");
		message.showAndWait();
	}

	@FXML
	public void Left_Button_LogOut(MouseEvent event) {
		Reader getReaderData = Table.getSelectionModel().getSelectedItem();
		if (getReaderData == null)
			return;
		selection.showMessage("警告", "注销后借书证将无法使用");
		Optional<ButtonType> result = selection.showAndWait();
		if (result.get() == ButtonType.NO)
			return;
		getReaderData.setRdStatus("注销");
		ReaderDAL readerDAL = new ReaderDAL(connectDB);
		readerDAL.setReader(getReaderData);
		if (readerDAL.Update()) {
			message.showMessage("消息", "注销成功");
			message.showAndWait();
			return;
		}
		message.showMessage("消息", "注销失败");
		message.showAndWait();
	}

	@FXML
	public void Left_Button_Exit(MouseEvent event) {
		Platform.exit();
	}

	@FXML
	public void Right_Button_ConfirmRegister(MouseEvent event) {
		selection.showMessage("确认", "是否办理?");
		Optional<ButtonType> result = selection.showAndWait();
		if (result.get() == ButtonType.NO)
			return;
		UpdateNewInfo(0);
		imageStream = null;
	}

	@FXML
	public void Right_Button_ConfirmChange(MouseEvent event) {
		selection.showMessage("确认", "是否更改?");
		Optional<ButtonType> result = selection.showAndWait();
		if (result.get() == ButtonType.NO)
			return;
		UpdateNewInfo(2);
		imageStream = null;
	}

	@FXML
	public void Right_Button_Cancel(MouseEvent event) {
		if (!Right_Button_ConfirmRegister.isDisabled()) {
			right_Text_CardID.clear();
			right_Text_Name.clear();
			right_Text_Password.clear();
			right_Text_Sex.getSelectionModel().clearSelection();
			right_Text_BorrowBooks.clear();
			right_Text_Status.clear();
			right_Text_Role.clear();
			right_Text_Type.getSelectionModel().clearSelection();
			PhotoPane.getChildren().clear();
			right_Text_Department.getSelectionModel().clearSelection();
			right_Text_Phone.clear();
			right_Text_Email.clear();
			right_Text_TimeOfRegisterCard.clear();
		} else {
			CheckInfo();
		}
	}

	@FXML
	public void Right_Buttom_Scale(MouseEvent event) {
		if (!PhotoPane.getChildren().isEmpty()) {
			// 未完成
			ZoomImage zoomImage = new ZoomImage((ImageView) PhotoPane.getChildren().get(0));
			zoomImage.display();
		}
	}

	@FXML
	public void Right_Button_ImageFile(MouseEvent event) {
		File file = FileChoose.OpenFileLocation(stage, 1);
		if (file == null)
			return;
		PhotoPane.getChildren().clear();
		try {
			InputStream inputStream = new FileInputStream(file);
			Image photo = new Image(inputStream);
			ImageView imageView = new ImageView(photo);
			imageView.setFitWidth(200);
			imageView.setFitHeight(260);
			PhotoPane.getChildren().add(imageView);
			if (Table.getSelectionModel().getSelectedItem() != null) {
				Table.getSelectionModel().getSelectedItem().setRdPhoto(new FileInputStream(file));
			}
			imageStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void Top_Button_Search(MouseEvent event) {
		String type = "";
		String dept = "";
		if (Top_Choice_Role.getSelectionModel().getSelectedItem() != null) {
			connectDB.GetTable("SELECT rdType FROM Library.dbo.TB_ReaderType WHERE rdTypeName = '"
					+ Top_Choice_Role.getSelectionModel().getSelectedItem() + "'");
			ResultSet result = connectDB.getResult();
			try {
				if (result.next())
					type = "r.rdType = '" + result.getString(1) + "' AND";
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (Top_Choice_Department.getSelectionModel().getSelectedItem() != null) {
			dept = " r.rdDept = '" + Top_Choice_Department.getSelectionModel().getSelectedItem() + "' AND";
		}
		String sql = "SELECT * FROM Library.dbo.TB_Reader AS r,Library.dbo.TB_ReaderType AS rt WHERE " + type + dept
				+ " r.rdName LIKE '%" + Top_Choice_Name.getText() + "%' AND r.rdType = rt.rdType";
		connectDB.GetTable(sql);
		Table.getItems().clear();
		ResultSet resultSet = connectDB.getResult();
		ObservableList<Reader> data = FXCollections.observableArrayList();
		try {
			while (resultSet.next()) {
				Reader reader = new Reader();
				reader.setValue(resultSet);
				data.add(reader);
			}
			Table.setItems(data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void Top_Button_Excel(MouseEvent event) {
		String[] tiltes = { "借阅ID", "姓名", "性别", "类型", "单位", "电话", "邮箱", "注册日期", "状态", "已借书数目", "密码", "读者角色" };
		GenerateExcel excel = new GenerateExcel(Table, stage);
		excel.generate(tiltes, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, "读者信息表");
	}

	@FXML
	private void Top_Choice_Role(MouseEvent event) {
		Top_Choice_Role.getItems().clear();
		set_Choice_Role(Top_Choice_Role);
	}

	@FXML
	private void Top_Choice_Department(MouseEvent event) {
		Top_Choice_Department.getItems().clear();
		set_Choice_Department(Top_Choice_Department);
	}

	@FXML
	private void right_Text_Type(MouseEvent event) {
		right_Text_Type.getItems().clear();
		set_Choice_Role(right_Text_Type);
	}

	@FXML
	private void right_Text_Department(MouseEvent event) {
		right_Text_Department.getItems().clear();
		set_Choice_Department(right_Text_Department);
	}

	private void set_Choice_Role(ChoiceBox<String> choiceBox) {
		connectDB.GetTable("SELECT DISTINCT rdTypeName FROM Library.dbo.TB_ReaderType");
		ResultSet tmp = connectDB.getResult();
		try {
			while (tmp.next()) {
				choiceBox.getItems().add(tmp.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void set_Choice_Department(ChoiceBox<String> choiceBox) {
		connectDB.GetTable("SELECT DISTINCT rdDept FROM Library.dbo.TB_Reader");
		ResultSet tmp = connectDB.getResult();
		try {
			while (tmp.next()) {
				choiceBox.getItems().add(tmp.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void BindColumn() {
		Table_ID.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdID"));
		Table_Name.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdName"));
		Table_Sex.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdSex"));
		Table_Type.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdType"));
		Table_Department.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdDept"));
		Table_Phone.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdPhone"));
		Table_Email.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdEmail"));
		Table_Status.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdStatus"));
		Table_BorrowBooks.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdBorrowQty"));
		Table_TimeOfRegistor.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdDateReg"));
	}

	/**
	 * 将信息打印在右侧
	 */
	private void CheckInfo() {
		Reader getReaderData = Table.getSelectionModel().getSelectedItem();
		if (getReaderData != null) {
			right_Text_CardID.setText(getReaderData.getRdID());
			right_Text_Name.setText(getReaderData.getRdName());
			right_Text_Password.setText(getReaderData.getRdPwd());
			right_Text_Sex.getSelectionModel().select(getReaderData.getRdSex());
			right_Text_BorrowBooks.setText(getReaderData.getRdBorrowQty());
			right_Text_Status.setText(getReaderData.getRdStatus());
			EnumAdminRole enumAdminRole[] = EnumAdminRole.values();
			String role = null;
			if (getReaderData.getRdAdminRoles() != null) {
				// role = "" 否则 显示的值中含有null
				role = "";
				for (int i = 0; i < enumAdminRole.length; i++) {
					if (enumAdminRole[i].Cmp(null, Integer.parseInt(getReaderData.getRdAdminRoles()))) {
						role += enumAdminRole[i].getTypeName() + " ";
					}
				}
			}
			right_Text_Role.setText(role);
			connectDB.GetTable("SELECT rdTypeName FROM Library.dbo.TB_ReaderType WHERE rdType = '"
					+ getReaderData.getRdType() + "'");
			ResultSet resultSet = connectDB.getResult();
			try {
				if (resultSet.next()) {
					right_Text_Type.getSelectionModel().select(resultSet.getString(1));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PhotoPane.getChildren().clear();
			right_Text_Department.getSelectionModel().select(getReaderData.getRdDept());
			right_Text_Phone.setText(getReaderData.getRdPhone());
			right_Text_Email.setText(getReaderData.getRdEmail());
			right_Text_TimeOfRegisterCard.setText(getReaderData.getRdDateReg());

			connectDB.GetTable(
					"SELECT rdPhoto FROM Library.dbo.TB_Reader WHERE rdID = '" + getReaderData.getRdID() + "'");
			ResultSet image = connectDB.getResult();
			try {
				image.next();
				Blob inImage = image.getBlob(1);
				if (inImage == null) {
					return;
				}
				Image photo = new Image(inImage.getBinaryStream());
				ImageView imageView = new ImageView(photo);
				imageView.setFitWidth(200);
				imageView.setFitHeight(260);
				PhotoPane.getChildren().add(imageView);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 写入新信息
	/**
	 * ops为0办理 2 更改
	 * 
	 * @param message
	 * @param ops
	 */
	private void UpdateNewInfo(int ops) {
		String[] Message = { "办理成功", "办理失败", "更改成功", "更改失败" };
		ReaderDAL readerDAL = new ReaderDAL(ConnectDB.getDBConnection());
		if (ops == 0) {
			Reader reader = new Reader();
			readerDAL.setReader(reader);
			reader.setRdID(right_Text_CardID.getText());
			reader.setRdName(right_Text_Name.getText());
			reader.setRdPwd(right_Text_Password.getText());
			reader.setRdSex(right_Text_Sex.getSelectionModel().getSelectedItem());
			reader.setRdBorrowQty(right_Text_BorrowBooks.getText());
			reader.setRdStatus("有效");
			reader.setRdAdminRoles("0");
			connectDB.GetTable("SELECT rdType FROM Library.dbo.TB_ReaderType WHERE rdTypeName = '"
					+ right_Text_Type.getSelectionModel().getSelectedItem() + "'");
			ResultSet resultSet = connectDB.getResult();
			try {
				if (resultSet.next()) {
					reader.setRdType(resultSet.getString(1));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			reader.setRdPhoto(imageStream);
			reader.setRdDept(right_Text_Department.getSelectionModel().getSelectedItem());
			reader.setRdPhone(right_Text_Phone.getText());
			reader.setRdEmail(right_Text_Email.getText());
			reader.setRdDateReg(right_Text_TimeOfRegisterCard.getText());
			// 上传数据库

			if (readerDAL.Insert()) {
				message.showMessage("消息", Message[ops]);
				message.showAndWait();
			} else {
				message.showMessage("消息", Message[ops + 1]);
				message.showAndWait();
			}
		} else {
			readerDAL.setReader(Table.getSelectionModel().getSelectedItem());
			if (readerDAL.Update()) {
				message.showMessage("消息", Message[ops]);
				message.showAndWait();
			} else {
				message.showMessage("消息", Message[ops + 1]);
				message.showAndWait();
			}
		}
	}

}
