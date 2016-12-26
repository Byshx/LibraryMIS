package edu.yangtzeu.lmis.bll;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

import edu.yangtzeu.lmis.bll.ReturnStack.Node;
import edu.yangtzeu.lmis.dal.BorrowDAL;
import edu.yangtzeu.lmis.dal.ConnectDB;
import edu.yangtzeu.lmis.dal.ReaderDAL;
import edu.yangtzeu.lmis.gui.Login;
import edu.yangtzeu.lmis.gui.UI_Factory;
import edu.yangtzeu.lmis.model.Book;
import edu.yangtzeu.lmis.model.Borrow;
import edu.yangtzeu.lmis.model.Reader;
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

public class IMP_BookIO extends LibraryBLL {

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

	// 上部 读者查询

	@FXML
	private TextField rdID;
	@FXML
	private TextField rdName;
	@FXML
	private TextField rdDept;
	@FXML
	private TextField rdType;
	@FXML
	private TextField CanLendQty;
	@FXML
	private TextField CanLendDay;
	@FXML
	private TextField rdBorrowQty;
	@FXML
	private Button rdSearch;

	// 上部 读者已借图书查询Table

	@FXML
	private TableView<Borrow> BorrowTable;
	@FXML
	private TableColumn<Borrow, String> Borrow_bkID;
	@FXML
	private TableColumn<Borrow, String> Borrow_bkName;
	@FXML
	private TableColumn<Borrow, String> Borrow_bkAuthor;
	@FXML
	private TableColumn<Borrow, String> Borrow_CanContinueTimes;
	@FXML
	private TableColumn<Borrow, String> Borrow_IdDateOut;
	@FXML
	private TableColumn<Borrow, String> Borrow_IdDateRetPlan;
	@FXML
	private TableColumn<Borrow, String> Borrow_IdOverDay;
	@FXML
	private TableColumn<Borrow, String> Borrow_IdOverMoney;

	// 中部 图书查询
	@FXML
	private TextField s_bkID;
	@FXML
	private TextField s_bkName;
	@FXML
	private Button bkID_Search;

	// 图书查询Table

	@FXML
	private TableView<Book> BookTable;
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

	// 下部Button

	@FXML
	private Button Bottom_button_lend;
	@FXML
	private Button Bottom_button_relend;
	@FXML
	private Button Bottom_button_gbBook;
	@FXML
	private Button Bottom_button_return;

	// 获取数据库连接
	private ConnectDB connectDB = null;

	// 获取展示平台
	private Stage stage = null;

	// 提示框
	private PopSelection selection = null;

	// 消息框
	private PopMessage message = null;

	private static int ops = 0;

	// 获得返回单例
	private ReturnStack back = ReturnStack.getReturnInstance();

	/**
	 * 用于临时存储Reader类
	 */
	private Reader reader = new Reader();

	public void initialize() {
		connectDB = ConnectDB.getDBConnection();
		// 如果是读者，使借阅管理者的功能失效，并刷新读者信息
		if (ops == 1) {
			rdSearch.setDisable(true);
			rdID.setDisable(true);
			Bottom_button_lend.setDisable(true);
			Bottom_button_relend.setDisable(true);
			Bottom_button_gbBook.setDisable(true);
			SearchRdInfo();
		}
		BindColumn();
		stage = Login.ShowPlatform;
		message = new PopMessage();
		selection = new PopSelection();
	}

	/**
	 * 设置权限<br/>
	 * 非该功能管理者只可查阅图书<br/>
	 * 0为有权限 1为没有
	 * 
	 * @param int
	 */
	public static void setOps(int ops) {
		IMP_BookIO.ops = ops;
	}

	@Override
	public void ReaderManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.LendCardManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookIO, "借阅管理", 0, ops);
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
			back.addReturn(UI_Factory.BookIO, "借阅管理", 0, ops);
			UI_Factory.ReaderTypeOp.showUI(stage, "读者类别管理");
		} else {
			message.showMessage("提示", "您没有权限");
			message.showAndWait();
		}
	}

	@Override
	public void NewbookIn(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.BookManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookIO, "借阅管理", 0, ops);
			IMP_BookManagement.setOps(0);
			UI_Factory.BookManagement.showUI(stage, "新书入库");
		} else {
			message.showMessage("提示", "您没有权限");
			message.showAndWait();
		}
	}

	@Override
	public void LibInfoManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.BookManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookIO, "借阅管理", 0, ops);
			UI_Factory.BookSearch.showUI(stage, "图书信息管理");
		} else {
			message.showMessage("提示", "您没有权限");
			message.showAndWait();
		}
	}

	@Override
	public void BorrowManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		// 已在本页，表示权限已知
		back.addReturn(UI_Factory.BookIO, "借阅管理", 0, ops);
		UI_Factory.BookIO.showUI(stage, "借阅管理");
	}

	@Override
	public void PowerManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.SystemManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookIO, "借阅管理", 0, ops);
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

	// Button响应事件

	@FXML
	public void rdSearch(MouseEvent event) {
		SearchRdInfo();
	}

	@FXML
	public void bkID_Search(MouseEvent event) {
		RenewBookTable("SELECT * FROM Library.dbo.TB_Book WHERE bkID LIKE '%" + s_bkID.getText() + "%'");
	}

	@FXML
	public void bkName_Search(MouseEvent event) {
		RenewBookTable("SELECT * FROM Library.dbo.TB_Book WHERE bkName LIKE '%" + s_bkName.getText() + "%'");
	}

	// 借阅
	@FXML
	private void Bottom_button_lend(MouseEvent event) {
		Borrow borrow = new Borrow();
		Book book = BookTable.getSelectionModel().getSelectedItem();
		if (reader == null || book == null)
			return;
		selection.showMessage("消息", "确认借阅？");
		Optional<ButtonType> optional = selection.showAndWait();
		if (optional.get() == ButtonType.NO)
			return;
		connectDB.GetTable("SELECT bkStatus FROM Library.dbo.TB_Book WHERE bkID = '" + book.getBkID() + "'");
		ResultSet resultSet = connectDB.getResult();
		try {
			if (resultSet.next()) {
				if (!resultSet.getString(1).equals("在馆")) {
					message.showMessage("消息", "该书已不在馆内");
					message.showAndWait();
					return;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		if (!reader.getRdStatus().equals("有效")) {
			message.showMessage("消息", "非有效账户");
			message.showAndWait();
			return;
		}
		if (Integer.parseInt(rdBorrowQty.getText()) + 1 > Integer.parseInt(CanLendQty.getText())) {
			message.showMessage("消息", "已达到借书数量上限！");
			message.showAndWait();
			return;
		}
		connectDB.GetTable("SELECT COUNT(*) FROM Library.dbo.TB_Borrow WHERE IdOverDay IS NULL AND rdID = '"
				+ reader.getRdID() + "'");
		resultSet = connectDB.getResult();
		try {
			if (resultSet.next()) {
				int OverNumber = Integer.parseInt(resultSet.getString(1));
				if (OverNumber > 0) {
					message.showMessage("消息", "含有超期未还书籍，不能借阅");
					message.showAndWait();
					return;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 借书顺序号，数据库自动生成
		borrow.setBorrowID(null);
		borrow.setRdID(reader.getRdID());
		borrow.setBkID(book.getBkID());

		// 初始续借次数为0
		borrow.setIdContinueTimes(0 + "");
		borrow.setIdDateOut(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(5, Integer.parseInt(CanLendDay.getText()));
		borrow.setIdDateRetPlan(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
		// 在还书时设置的值
		borrow.setIdDateRetAct(null);
		borrow.setIdOverDay(null);
		borrow.setIdOverMoney(null);
		borrow.setIdPunishMoney(null);
		borrow.setIsHasReturn(false);

		// 获取从登陆面登陆的管理员信息实例
		Reader Operator = IMP_Login.getLoginReader();
		borrow.setOperatorLend(Operator.getRdName());
		borrow.setOperatorRet(null);

		BorrowDAL borrowDAL = new BorrowDAL(connectDB);
		borrowDAL.setBorrow(borrow);

		reader.setRdBorrowQty((Integer.parseInt(reader.getRdBorrowQty()) + 1) + "");
		ReaderDAL readerDAL = new ReaderDAL(connectDB);
		readerDAL.setReader(reader);

		boolean succeed = connectDB.UpdateTable(
				"UPDATE Library.dbo.TB_Book SET bkStatus = ? WHERE bkID = '" + borrow.getBkID() + "'", null, null,
				new String[] { "借出" });

		if (borrowDAL.Insert() && readerDAL.Update() && succeed) {
			message.showMessage("消息", "借阅成功");
			SearchRdInfo();
			message.showAndWait();
			// 刷新BookTable
		} else {
			message.showMessage("消息", "借阅失败");
			message.showAndWait();
		}
	}

	// 续借
	@FXML
	private void Bottom_button_relend(MouseEvent event) {
		Borrow borrow = BorrowTable.getSelectionModel().getSelectedItem();
		if (borrow == null)
			return;
		selection.showMessage("消息", "确认续借？");
		Optional<ButtonType> optional = selection.showAndWait();
		if (optional.get() == ButtonType.NO)
			return;
		connectDB.GetTable(
				"SELECT CanContinueTimes FROM Library.dbo.TB_ReaderType WHERE rdType = '" + reader.getRdType() + "'");
		ResultSet resultSet = connectDB.getResult();
		// 可续借次数
		int CanContinueTimes = 0;
		try {
			if (resultSet.next()) {
				CanContinueTimes = Integer.parseInt(resultSet.getString("CanContinueTimes"));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (Integer.parseInt(borrow.getIdContinueTimes()) + 1 > CanContinueTimes) {
			message.showMessage("消息", "已达到可续借次数！");
			message.showAndWait();
			return;
		}
		if (!reader.getRdStatus().equals("有效")) {
			message.showMessage("消息", "非有效账户");
			message.showAndWait();
			return;
		}
		// 可续借次数加一
		borrow.setIdContinueTimes((Integer.parseInt(borrow.getIdContinueTimes()) + 1) + "");
		borrow.setIdDateOut(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(5, Integer.parseInt(CanLendDay.getText()));
		borrow.setIdDateRetPlan(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
		BorrowDAL borrowDAL = new BorrowDAL(connectDB);
		borrowDAL.setBorrow(borrow);

		if (borrowDAL.Update()) {
			message.showMessage("消息", "续借成功!");
			SearchRdInfo();
			message.showAndWait();
		} else {
			message.showMessage("消息", "续借失败!");
			message.showAndWait();
		}
	}

	// 还书用例
	@FXML
	private void Bottom_button_gbBook(MouseEvent event) {
		Borrow borrow = BorrowTable.getSelectionModel().getSelectedItem();
		if (borrow == null)
			return;
		// 获取图书价格
		connectDB.GetTable("SELECT bkPrice FROM Library.dbo.TB_Book WHERE bkID = '" + borrow.getBkID() + "'");
		ResultSet resultSet = connectDB.getResult();
		String bookPrice = null;
		try {
			if (resultSet.next()) {
				bookPrice = resultSet.getString("bkPrice");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 创建还书操作窗口
		BackBook backBook = new BackBook(borrow.getIdOverMoney(), bookPrice);
		backBook.display();
		if (!backBook.getCloseWay())
			return;
		else {
			// 设置还书操作员名称
			borrow.setOperatorRet(reader.getRdName());
			// 确认更新
			boolean updateBookStatus = false;
			// 将状态设为已还
			borrow.setIsHasReturn(true);
			if (backBook.isLost()) {
				borrow.setIdPunishMoney(backBook.getPunish());
				updateBookStatus = connectDB.UpdateTable(
						"Update Library.dbo.TB_Book SET bkStatus = ? WHERE bkID = '" + borrow.getBkID() + "'", null,
						null, new String[] { "遗失" });
			} else {
				updateBookStatus = connectDB.UpdateTable(
						"UPDATE Library.dbo.TB_Book SET bkStatus = ? WHERE bkID = '" + borrow.getBkID() + "'", null,
						null, new String[] { "在馆" });
			}
			BorrowDAL borrowDAL = new BorrowDAL(connectDB);
			borrowDAL.setBorrow(borrow);

			reader.setRdBorrowQty((Integer.parseInt(reader.getRdBorrowQty()) - 1) + "");
			ReaderDAL readerDAL = new ReaderDAL(connectDB);
			readerDAL.setReader(reader);

			if (borrowDAL.Update() && readerDAL.Update() && updateBookStatus) {
				message.showMessage("消息", "还书成功");
				SearchRdInfo();
				message.showAndWait();
				// 刷新BookTable
			} else {
				message.showMessage("消息", "还书失败");
				message.showAndWait();
			}
		}
	}

	// 返回上一页面
	@FXML
	private void Bottom_button_return(MouseEvent event) {
		Node node = null;
		if ((node = back.getReturn()) != null) {
			node.invokeReturn();
		}
	}

	/**
	 * 
	 * 为读者信息搜索设置一个私有类<br/>
	 * 目的是为了在读者权限为零时<br/>
	 * 直接调用此方法来刷新读者信息
	 */
	private void SearchRdInfo() {
		if (ops == 1) {
			// 若为读者登陆，则自动填充为登陆者ID
			rdID.setText(IMP_Login.getLoginReader().getRdID());
		}
		if (rdID.getText().equals(""))
			return;
		// 向数据库传递更新请求，触动触发器，更新罚款
		connectDB.UpdateTable("UPDATE Library.dbo.TB_Borrow SET rdID = '" + rdID.getText() + "' WHERE rdID = '"
				+ rdID.getText() + "'", null, null, new String[] {});
		// 连接三个表:读者表 读者类别表 和 借阅表
		connectDB.GetTable("SELECT * FROM Library.dbo.TB_Reader WHERE rdID = '" + rdID.getText() + "'");
		ResultSet resultSet = connectDB.getResult();

		try {
			if (resultSet.next()) {
				reader.setValue(resultSet);
			}
			connectDB.GetTable("SELECT * FROM Library.dbo.TB_ReaderType WHERE rdType = '" + reader.getRdType() + "'");
			ResultSet resultSet2 = connectDB.getResult();
			if (resultSet2.next()) {
				rdName.setText(resultSet.getString("rdName"));
				rdDept.setText(resultSet.getString("rdDept"));
				rdType.setText(resultSet2.getString("rdTypeName"));
				CanLendQty.setText(resultSet2.getString("CanLendQty"));
				CanLendDay.setText(resultSet2.getString("CanLendDay"));
				rdBorrowQty.setText(resultSet.getString("rdBorrowQty"));
				RenewBorrowTable();
			} else {
				message.showMessage("消息", "未找到");
				message.showAndWait();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void RenewBorrowTable() {
		// 再次请求数据<br/>
		// 因为有时读者借/还书后，现有数据已过时
		connectDB.GetTable("SELECT * FROM Library.dbo.TB_Borrow AS borrow,Library.dbo.TB_Book AS book WHERE rdID = '"
				+ reader.getRdID() + "' AND borrow.bkID = book.bkID AND borrow.IsHasReturn = 'False'");
		ResultSet resultSet = connectDB.getResult();
		ObservableList<Borrow> data = FXCollections.observableArrayList();
		try {
			while (resultSet.next()) {
				Borrow borrow = new Borrow();
				borrow.setValue(resultSet);
				data.add(borrow);
			}
			BorrowTable.setItems(data);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void RenewBookTable(String t_sql) {
		connectDB.GetTable(t_sql);
		ResultSet resultSet = connectDB.getResult();
		ObservableList<Book> data = FXCollections.observableArrayList();
		try {
			while (resultSet.next()) {
				Book book = new Book();
				book.setValue(resultSet);
				data.add(book);
			}
			BookTable.setItems(data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void BindColumn() {
		// BorrowTable

		Borrow_bkID.setCellValueFactory(new PropertyValueFactory<Borrow, String>("bkID"));
		Borrow_bkName.setCellValueFactory(new PropertyValueFactory<Borrow, String>("bkName"));
		Borrow_bkAuthor.setCellValueFactory(new PropertyValueFactory<Borrow, String>("bkAuthor"));
		Borrow_CanContinueTimes.setCellValueFactory(new PropertyValueFactory<Borrow, String>("IdContinueTimes"));
		Borrow_IdDateOut.setCellValueFactory(new PropertyValueFactory<Borrow, String>("IdDateOut"));
		Borrow_IdDateRetPlan.setCellValueFactory(new PropertyValueFactory<Borrow, String>("IdDateRetPlan"));
		Borrow_IdOverDay.setCellValueFactory(new PropertyValueFactory<Borrow, String>("IdOverDay"));
		Borrow_IdOverMoney.setCellValueFactory(new PropertyValueFactory<Borrow, String>("IdOverMoney"));

		// BookTable

		r_bkID.setCellValueFactory(new PropertyValueFactory<Book, String>("bkID"));
		r_bkCode.setCellValueFactory(new PropertyValueFactory<Book, String>("bkCode"));
		r_bkName.setCellValueFactory(new PropertyValueFactory<Book, String>("bkName"));
		r_bkAuthor.setCellValueFactory(new PropertyValueFactory<Book, String>("bkAuthor"));
		r_bkPress.setCellValueFactory(new PropertyValueFactory<Book, String>("bkPress"));
		r_bkDatePress.setCellValueFactory(new PropertyValueFactory<Book, String>("bkDatePress"));
		r_bkISBN.setCellValueFactory(new PropertyValueFactory<Book, String>("bkISBN"));
		r_bkCatalog_No.setCellValueFactory(new PropertyValueFactory<Book, String>("bkCatalog"));
		r_bkLanguage.setCellValueFactory(new PropertyValueFactory<Book, String>("bkLanguage"));
		r_bkPages.setCellValueFactory(new PropertyValueFactory<Book, String>("bkPages"));
		r_bkPrice.setCellValueFactory(new PropertyValueFactory<Book, String>("bkPrice"));
		r_bkDateIn.setCellValueFactory(new PropertyValueFactory<Book, String>("bkDateIn"));
		r_bkStatus.setCellValueFactory(new PropertyValueFactory<Book, String>("bkStatus"));
	}

}
