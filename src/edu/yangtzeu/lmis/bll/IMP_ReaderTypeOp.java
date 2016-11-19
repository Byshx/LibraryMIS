package edu.yangtzeu.lmis.bll;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import edu.yangtzeu.lmis.bll.ReturnStack.Node;
import edu.yangtzeu.lmis.dal.ConnectDB;
import edu.yangtzeu.lmis.dal.ReaderDAL;
import edu.yangtzeu.lmis.dal.ReaderTypeDAL;
import edu.yangtzeu.lmis.gui.Login;
import edu.yangtzeu.lmis.gui.UI_Factory;
import edu.yangtzeu.lmis.model.Reader;
import edu.yangtzeu.lmis.model.ReaderType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class IMP_ReaderTypeOp extends LibraryBLL {

	// TopBar 菜单管理

	@FXML
	private MenuItem ReaderTypeManagement;
	@FXML
	private MenuItem ReaderTypeTypeOp;
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

	// 输入框

	@FXML
	private TextField rdID;
	@FXML
	private TextField rdType;
	@FXML
	private TextField CanLendQty;
	@FXML
	private TextField CanLendDay;
	@FXML
	private TextField CanContinueTimes;
	@FXML
	private TextField PunishRate;
	@FXML
	private TextField DateVaild;

	// Table框
	@FXML
	private TableView<ReaderType> Table;
	@FXML
	private TableColumn<ReaderType, String> tb_rdType;
	@FXML
	private TableColumn<ReaderType, String> tb_rdTypeName;
	@FXML
	private TableColumn<ReaderType, String> tb_rdCanLendQty;
	@FXML
	private TableColumn<ReaderType, String> tb_rdCanLendDay;
	@FXML
	private TableColumn<ReaderType, String> tb_rdCanLendTimes;
	@FXML
	private TableColumn<ReaderType, String> tb_rdPunishRate;
	@FXML
	private TableColumn<ReaderType, String> tb_rdVaild;

	// Button响应事件

	@FXML
	private Button Bottom_button_add;
	@FXML
	private Button Bottom_button_select;
	@FXML
	private Button Bottom_button_modify;
	@FXML
	private Button Bottom_button_delete;
	@FXML
	private Button Bottom_button_cancel;
	@FXML
	private Button Bottom_button_return;

	// 获取Application平台
	private Stage stage = null;

	private ConnectDB connectDB = null;

	// 选择框
	private PopSelection selection = null;

	// 消息框
	private PopMessage message = null;

	// 获得返回单例
	private ReturnStack back = ReturnStack.getReturnInstance();

	public void initialize() {
		connectDB = ConnectDB.getDBConnection();
		BindColumn();
		stage = Login.ShowPlatform;
		message = new PopMessage();
		selection = new PopSelection();
		Renew();
	}

	// TopBar事件响应

	@FXML
	public void ReaderManagement(ActionEvent event) {
		// 已在本页，无需判断权限
		back.addReturn(UI_Factory.ReaderTypeOp, "读者类别管理", 0, 0);
		UI_Factory.ReaderOp.showUI(stage, "读者管理");
	}

	@FXML
	public void ReaderTypeOp(ActionEvent event) {
		// 已在本页，无需判断权限
		back.addReturn(UI_Factory.ReaderTypeOp, "读者类别管理", 0, 0);
		UI_Factory.ReaderTypeOp.showUI(stage, "读者类别管理");
	}

	@FXML
	public void NewbookIn(ActionEvent event) {
		if (EnumAdminRole.BookManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.ReaderTypeOp, "读者类别管理", 0, 0);
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
			back.addReturn(UI_Factory.ReaderTypeOp, "读者类别管理", 0, 0);
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
		back.addReturn(UI_Factory.ReaderTypeOp, "读者类别管理", 0, 0);
		UI_Factory.BookIO.showUI(stage, "借阅管理");
	}

	@FXML
	public void PowerManagement(ActionEvent event) {
		if (EnumAdminRole.SystemManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.ReaderTypeOp, "读者类别管理", 0, 0);
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

	// 下部按钮事件响应
	@FXML
	private void Bottom_button_add(MouseEvent event) {
		selection.showMessage("消息", "确认添加?");
		Optional<ButtonType> optional = selection.showAndWait();
		if (optional.get() == ButtonType.NO)
			return;
		ReaderType readerType = new ReaderType();
		readerType.setRdType(rdID.getText());
		readerType.setRdTypeName(rdType.getText());
		readerType.setRdCanLendQty(CanLendQty.getText());
		readerType.setRdCanLendDay(CanLendDay.getText());
		readerType.setRdCanContinueTimes(CanContinueTimes.getText());
		readerType.setRdPunishRate(PunishRate.getText());
		readerType.setRdDateVaild(DateVaild.getText());
		ReaderTypeDAL readerTypeDAL = new ReaderTypeDAL(connectDB);
		readerTypeDAL.setReaderType(readerType);
		if (readerTypeDAL.Insert()) {
			message.showMessage("消息", "添加成功");
			message.showAndWait();
			Renew();
		} else {
			message.showMessage("消息", "添加失败");
			message.showAndWait();
		}
	}

	@FXML
	private void Bottom_button_select(MouseEvent event) {
		ReaderType readerType = Table.getSelectionModel().getSelectedItem();
		rdID.setText(readerType.getRdType());
		rdType.setText(readerType.getRdTypeName());
		CanLendQty.setText(readerType.getCanLendQty());
		CanLendDay.setText(readerType.getCanLendDay());
		CanContinueTimes.setText(readerType.getCanContinueTimes());
		PunishRate.setText(readerType.getPunishRate());
		DateVaild.setText(readerType.getDateVaild());
	}

	@FXML
	private void Bottom_button_modify(MouseEvent event) {
		selection.showMessage("消息", "确认修改?");
		Optional<ButtonType> optional = selection.showAndWait();
		if (optional.get() == ButtonType.NO)
			return;
		ReaderType readerType = new ReaderType();
		readerType.setRdType(rdID.getText());
		readerType.setRdTypeName(rdType.getText());
		readerType.setRdCanLendQty(CanLendQty.getText());
		readerType.setRdCanLendDay(CanLendDay.getText());
		readerType.setRdCanContinueTimes(CanContinueTimes.getText());
		readerType.setRdPunishRate(PunishRate.getText());
		readerType.setRdDateVaild(DateVaild.getText());
		ReaderTypeDAL readerTypeDAL = new ReaderTypeDAL(connectDB);
		readerTypeDAL.setReaderType(readerType);
		if (readerTypeDAL.Update()) {
			message.showMessage("消息", "修改成功");
			message.showAndWait();
			Renew();
		} else {
			message.showMessage("消息", "修改失败");
			message.showAndWait();
		}
	}

	@FXML
	private void Bottom_button_delete(MouseEvent event) {
		selection.showMessage("消息", "确认删除?");
		Optional<ButtonType> optional = selection.showAndWait();
		if (optional.get() == ButtonType.NO)
			return;
		ReaderType readerType = Table.getSelectionModel().getSelectedItem();
		ReaderTypeDAL readerTypeDAL = new ReaderTypeDAL(connectDB);
		readerTypeDAL.setReaderType(readerType);
		if (readerTypeDAL.Delete()) {
			message.showMessage("消息", "删除成功");
			message.showAndWait();
			Renew();
		} else {
			message.showMessage("消息", "删除失败");
			message.showAndWait();
		}
	}

	@FXML
	private void Bottom_button_cancel(MouseEvent event) {
		rdID.clear();
		rdType.clear();
		CanLendQty.clear();
		CanLendDay.clear();
		CanContinueTimes.clear();
		PunishRate.clear();
		DateVaild.clear();
	}

	@FXML
	private void Bottom_button_return(MouseEvent event) {
		Node node = null;
		if ((node = back.getReturn()) != null) {
			node.invokeReturn();
		}
	}

	public void addTableItem(ResultSet resultSet) {
		ObservableList<ReaderType> data = FXCollections.observableArrayList();
		try {
			while (resultSet.next()) {
				ReaderType readerType = new ReaderType();
				readerType.setValue(resultSet);
				data.add(readerType);
			}
			Table.setItems(data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 构建Bean映射
	 */
	private void BindColumn() {
		tb_rdType.setCellValueFactory(new PropertyValueFactory<ReaderType, String>("rdType"));
		tb_rdTypeName.setCellValueFactory(new PropertyValueFactory<ReaderType, String>("rdTypeName"));
		tb_rdCanLendQty.setCellValueFactory(new PropertyValueFactory<ReaderType, String>("CanLendQty"));
		tb_rdCanLendDay.setCellValueFactory(new PropertyValueFactory<ReaderType, String>("CanLendDay"));
		tb_rdCanLendTimes.setCellValueFactory(new PropertyValueFactory<ReaderType, String>("CanContinueTimes"));
		tb_rdPunishRate.setCellValueFactory(new PropertyValueFactory<ReaderType, String>("PunishRate"));
		tb_rdVaild.setCellValueFactory(new PropertyValueFactory<ReaderType, String>("DateVaild"));
	}

	/**
	 * 刷新表格
	 */
	private void Renew() {
		String sql = "SELECT * FROM Library.dbo.TB_ReaderType";
		connectDB.GetTable(sql);
		Table.getItems().clear();
		addTableItem(connectDB.getResult());
	}

}
