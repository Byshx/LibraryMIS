package edu.yangtzeu.lmis.bll;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import edu.yangtzeu.lmis.bll.ReturnStack.Node;
import edu.yangtzeu.lmis.dal.BookDAL;
import edu.yangtzeu.lmis.dal.ConnectDB;
import edu.yangtzeu.lmis.dal.ReaderDAL;
import edu.yangtzeu.lmis.gui.Login;
import edu.yangtzeu.lmis.gui.UI_Factory;
import edu.yangtzeu.lmis.model.Book;
import edu.yangtzeu.lmis.model.Reader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class IMP_BookSearch extends LibraryBLL {

	// TopBar

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

	// 简单查询部分
	@FXML
	private ChoiceBox<String> FieldType;
	@FXML
	private TextField FieldContext;
	@FXML
	private Button Search_Simple;

	// 高级查询部分
	@FXML
	private TextField bkName;
	@FXML
	private TextField bkAuthor;
	@FXML
	private TextField bkBrief;
	@FXML
	private TextField bkPress;
	@FXML
	private TextField bkCatalog_No;
	@FXML
	private TextField bkDatePress;
	@FXML
	private Button Search_Advanced;

	// 下部Button
	@FXML
	private Button Bottom_button_modify;
	@FXML
	private Button Bottom_button_delete;
	@FXML
	private Button Bottom_button_excel;
	@FXML
	private Button Bottom_button_return;

	// BookTable

	@FXML
	private TableView<Book> Table;

	@FXML
	private TableColumn<Book, String> r_bkID;
	@FXML
	private TableColumn<Book, String> r_bkCode;
	@FXML
	private TableColumn<Book, String> r_bkName;
	@FXML
	private TableColumn<Book, String> r_bkAuthor;
	@FXML
	private TableColumn<Book, String> r_bkPress;
	@FXML
	private TableColumn<Book, String> r_bkDatePress;
	@FXML
	private TableColumn<Book, String> r_bkISBN;
	@FXML
	private TableColumn<Book, String> r_bkCatalog_No;
	@FXML
	private TableColumn<Book, String> r_bkLanguage;
	@FXML
	private TableColumn<Book, String> r_bkPages;
	@FXML
	private TableColumn<Book, String> r_bkPrice;
	@FXML
	private TableColumn<Book, String> r_bkDateIn;
	@FXML
	private TableColumn<Book, String> r_bkStatus;

	// 获取数据库连接实例
	private ConnectDB connectDB = null;

	// 获取展示平台
	private Stage stage = null;

	// 提示框
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
	}

	@Override
	public void ReaderManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.LendCardManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookIO, "图书信息管理", 0, 0);
			UI_Factory.ReaderOp.showUI(stage, "读者管理");
		} else {
			message.showMessage("提示", "您没有权限");
			message.showAndWait();
		}
	}

	@Override
	public void ReaderTypeOp(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.LendCardManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookIO, "图书信息管理", 0, 0);
			UI_Factory.ReaderTypeOp.showUI(stage, "读者类型管理");
		} else {
			message.showMessage("提示", "您没有权限");
			message.showAndWait();
		}
	}

	@Override
	public void NewbookIn(ActionEvent event) {
		// TODO Auto-generated method stub
		back.addReturn(UI_Factory.BookIO, "图书信息管理", 0, 0);
		IMP_BookManagement.setOps(0);
		UI_Factory.BookManagement.showUI(stage, "新书入库");
	}

	@Override
	public void LibInfoManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		// 已在本页，无需判断权限
		back.addReturn(UI_Factory.BookIO, "图书信息管理", 0, 0);
		UI_Factory.BookManagement.showUI(stage, "图书信息管理");
	}

	@Override
	public void BorrowManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		if (!EnumAdminRole.LendManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			IMP_BookIO.setOps(1);
		}
		back.addReturn(UI_Factory.BookSearch, "图书信息管理", 0, 0);
		UI_Factory.BookIO.showUI(stage, "借阅管理");
	}

	@Override
	public void PowerManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.SystemManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookIO, "图书信息管理", 0, 0);
			UI_Factory.UserPowerManagement.showUI(stage, "权限管理");
		} else {
			message.showMessage("提示", "您没有权限");
			message.showAndWait();
		}
	}

	@Override
	public void PasswordModify(ActionEvent event) {
		// TODO Auto-generated method stub
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

	// 简单查询Button
	@FXML
	public void Search_Simple(MouseEvent event) {
		if (FieldType.getSelectionModel().isEmpty())
			return;
		String field = "";
		EnumBookField[] enumBookFields = EnumBookField.values();
		for (int i = 0; i < enumBookFields.length; i++) {
			if (enumBookFields[i].Cmp(FieldType.getSelectionModel().getSelectedItem())) {
				field = enumBookFields[i].name();
				break;
			}
		}
		connectDB.GetTable(
				"SELECT * FROM Library.dbo.TB_Book WHERE " + field + " LIKE '%" + FieldContext.getText() + "%'");
		Table.getItems().clear();
		ResultSet resultSet = connectDB.getResult();
		addItems(resultSet);
	}

	// 高级查询Button
	@FXML
	public void Search_Advanced(MouseEvent event) {
		connectDB.GetTable("SELECT * FROM Library.dbo.TB_Book WHERE bkName LIKE '%" + bkName.getText()
				+ "%' AND bkAuthor LIKE '%" + bkAuthor.getText() + "%' AND bkBrief LIKE '%" + bkBrief.getText()
				+ "%' AND bkPress LIKE '%" + bkPress.getText() + "%' AND bkCatalog LIKE '%" + bkCatalog_No.getText()
				+ "%' AND bkDatePress LIKE '%" + bkDatePress.getText() + "%'");
		Table.getItems().clear();
		ResultSet resultSet = connectDB.getResult();
		addItems(resultSet);
	}

	// 下部Button事件响应
	@FXML
	public void Bottom_button_modify(MouseEvent event) {
		back.addReturn(UI_Factory.BookSearch, "图书信息管理", 0, 0);
		// ops设置为1 使添加按钮失效
		IMP_BookManagement.setOps(1);
		Book book = Table.getSelectionModel().getSelectedItem();
		if (book == null)
			return;
		connectDB.GetTable(
				"SELECT COUNT(*) FROM Library.dbo.TB_Book GROUP BY bkCode HAVING bkCode = '" + book.getBkCode() + "'");
		ResultSet resultSet = connectDB.getResult();
		int bookCount = 0;
		try {
			if (resultSet.next()) {
				bookCount = Integer.parseInt(resultSet.getString(1));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] info = { book.getBkCode(), book.getBkID(), book.getBkName(), book.getBkAuthor(), book.getBkPress(),
				book.getBkDatePress(), book.getBkISBN(), book.getBkCatalog(), book.getBkLanguageName(),
				book.getBkPages(), book.getBkPrice(), book.getBkDateIn(), bookCount + "", book.getBkBrief(),
				book.getBkStatus() };
		IMP_BookManagement.setInfo(info);
		UI_Factory.BookManagement.showUI(stage, "修改图书信息");
	}

	@FXML
	public void Bottom_button_delete(MouseEvent event) {
		Book book = Table.getSelectionModel().getSelectedItem();
		if (book == null)
			return;
		selection.showMessage("警告", "确认删除？");
		Optional<ButtonType> result = selection.showAndWait();
		if (result.get() == ButtonType.NO)
			return;
		BookDAL bookDAL = new BookDAL(connectDB);
		bookDAL.setBook(book);
		if (bookDAL.Delete()) {
			message.showMessage("消息", "删除成功");
			message.showAndWait();
			// 刷新BookTable
		} else {
			message.showMessage("消息", "删除失败");
			message.showAndWait();
		}
	}

	@FXML
	public void Bottom_button_excel(MouseEvent event) {
		GenerateExcel generateExcel = new GenerateExcel(Table, stage);
		String[] params = { "ID", "索书号", "书名", "作者", "出版社", "出版日期", "ISBN", "分类号", "语种", "页数", "价格", "入馆日期", "状态" };
		generateExcel.generate(params, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, "图书表");
	}

	@FXML
	public void Bottom_button_return(MouseEvent event) {
		Node node = null;
		if ((node = back.getReturn()) != null) {
			node.invokeReturn();
		}
	}

	private void BindColumn() {
		r_bkID.setCellValueFactory(new PropertyValueFactory<Book, String>("bkID"));
		r_bkCode.setCellValueFactory(new PropertyValueFactory<Book, String>("bkCode"));
		r_bkName.setCellValueFactory(new PropertyValueFactory<Book, String>("bkName"));
		r_bkAuthor.setCellValueFactory(new PropertyValueFactory<Book, String>("bkAuthor"));
		r_bkPress.setCellValueFactory(new PropertyValueFactory<Book, String>("bkPress"));
		r_bkDatePress.setCellValueFactory(new PropertyValueFactory<Book, String>("bkDatePress"));
		r_bkISBN.setCellValueFactory(new PropertyValueFactory<Book, String>("bkISBN"));
		r_bkCatalog_No.setCellValueFactory(new PropertyValueFactory<Book, String>("bkCatalog"));
		r_bkLanguage.setCellValueFactory(new PropertyValueFactory<Book, String>("bkLanguageName"));
		r_bkPages.setCellValueFactory(new PropertyValueFactory<Book, String>("bkPages"));
		r_bkPrice.setCellValueFactory(new PropertyValueFactory<Book, String>("bkPrice"));
		r_bkDateIn.setCellValueFactory(new PropertyValueFactory<Book, String>("bkDateIn"));
		r_bkStatus.setCellValueFactory(new PropertyValueFactory<Book, String>("bkStatus"));
	}

	private void addItems(ResultSet resultSet) {
		ResultSet result = resultSet;
		ObservableList<Book> data = FXCollections.observableArrayList();
		try {
			while (result.next()) {
				Book book = new Book();
				book.setValue(resultSet);
				data.add(book);
			}
			Table.setItems(data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
