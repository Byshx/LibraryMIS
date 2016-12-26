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

	// �ϲ� ���߲�ѯ

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

	// �ϲ� �����ѽ�ͼ���ѯTable

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

	// �в� ͼ���ѯ
	@FXML
	private TextField s_bkID;
	@FXML
	private TextField s_bkName;
	@FXML
	private Button bkID_Search;

	// ͼ���ѯTable

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

	// �²�Button

	@FXML
	private Button Bottom_button_lend;
	@FXML
	private Button Bottom_button_relend;
	@FXML
	private Button Bottom_button_gbBook;
	@FXML
	private Button Bottom_button_return;

	// ��ȡ���ݿ�����
	private ConnectDB connectDB = null;

	// ��ȡչʾƽ̨
	private Stage stage = null;

	// ��ʾ��
	private PopSelection selection = null;

	// ��Ϣ��
	private PopMessage message = null;

	private static int ops = 0;

	// ��÷��ص���
	private ReturnStack back = ReturnStack.getReturnInstance();

	/**
	 * ������ʱ�洢Reader��
	 */
	private Reader reader = new Reader();

	public void initialize() {
		connectDB = ConnectDB.getDBConnection();
		// ����Ƕ��ߣ�ʹ���Ĺ����ߵĹ���ʧЧ����ˢ�¶�����Ϣ
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
	 * ����Ȩ��<br/>
	 * �Ǹù��ܹ�����ֻ�ɲ���ͼ��<br/>
	 * 0Ϊ��Ȩ�� 1Ϊû��
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
			back.addReturn(UI_Factory.BookIO, "���Ĺ���", 0, ops);
			UI_Factory.ReaderOp.showUI(stage, "���߹���");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}
	}

	@Override
	public void ReaderTypeOp(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.LendCardManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookIO, "���Ĺ���", 0, ops);
			UI_Factory.ReaderTypeOp.showUI(stage, "����������");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}
	}

	@Override
	public void NewbookIn(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.BookManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookIO, "���Ĺ���", 0, ops);
			IMP_BookManagement.setOps(0);
			UI_Factory.BookManagement.showUI(stage, "�������");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}
	}

	@Override
	public void LibInfoManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.BookManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookIO, "���Ĺ���", 0, ops);
			UI_Factory.BookSearch.showUI(stage, "ͼ����Ϣ����");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}
	}

	@Override
	public void BorrowManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		// ���ڱ�ҳ����ʾȨ����֪
		back.addReturn(UI_Factory.BookIO, "���Ĺ���", 0, ops);
		UI_Factory.BookIO.showUI(stage, "���Ĺ���");
	}

	@Override
	public void PowerManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.SystemManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookIO, "���Ĺ���", 0, ops);
			UI_Factory.UserPowerManagement.showUI(stage, "Ȩ�޹���");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}
	}

	@Override
	public void PasswordModify(ActionEvent event) {
		// TODO Auto-generated method stub
		Reader reader = IMP_Login.getLoginReader();
		IMP_PasswordChange passwordChange = new IMP_PasswordChange();
		passwordChange.display(reader.getRdPwd());
		// ������ɰ�ȷ�����رմ��ڣ���ȥ��ȡ������
		if (passwordChange.getCloseWay()) {
			ReaderDAL readerDAL = new ReaderDAL(connectDB);
			reader.setRdPwd(passwordChange.getNewPassword());
			readerDAL.setReader(reader);
			if (readerDAL.Update()) {
				message.showMessage("��Ϣ", "�޸�����ɹ�");
				message.showAndWait();
				return;
			}
			message.showMessage("��Ϣ", "�޸�����ʧ��");
			message.showAndWait();
		}
	}

	// Button��Ӧ�¼�

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

	// ����
	@FXML
	private void Bottom_button_lend(MouseEvent event) {
		Borrow borrow = new Borrow();
		Book book = BookTable.getSelectionModel().getSelectedItem();
		if (reader == null || book == null)
			return;
		selection.showMessage("��Ϣ", "ȷ�Ͻ��ģ�");
		Optional<ButtonType> optional = selection.showAndWait();
		if (optional.get() == ButtonType.NO)
			return;
		connectDB.GetTable("SELECT bkStatus FROM Library.dbo.TB_Book WHERE bkID = '" + book.getBkID() + "'");
		ResultSet resultSet = connectDB.getResult();
		try {
			if (resultSet.next()) {
				if (!resultSet.getString(1).equals("�ڹ�")) {
					message.showMessage("��Ϣ", "�����Ѳ��ڹ���");
					message.showAndWait();
					return;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		if (!reader.getRdStatus().equals("��Ч")) {
			message.showMessage("��Ϣ", "����Ч�˻�");
			message.showAndWait();
			return;
		}
		if (Integer.parseInt(rdBorrowQty.getText()) + 1 > Integer.parseInt(CanLendQty.getText())) {
			message.showMessage("��Ϣ", "�Ѵﵽ�����������ޣ�");
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
					message.showMessage("��Ϣ", "���г���δ���鼮�����ܽ���");
					message.showAndWait();
					return;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ����˳��ţ����ݿ��Զ�����
		borrow.setBorrowID(null);
		borrow.setRdID(reader.getRdID());
		borrow.setBkID(book.getBkID());

		// ��ʼ�������Ϊ0
		borrow.setIdContinueTimes(0 + "");
		borrow.setIdDateOut(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(5, Integer.parseInt(CanLendDay.getText()));
		borrow.setIdDateRetPlan(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
		// �ڻ���ʱ���õ�ֵ
		borrow.setIdDateRetAct(null);
		borrow.setIdOverDay(null);
		borrow.setIdOverMoney(null);
		borrow.setIdPunishMoney(null);
		borrow.setIsHasReturn(false);

		// ��ȡ�ӵ�½���½�Ĺ���Ա��Ϣʵ��
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
				new String[] { "���" });

		if (borrowDAL.Insert() && readerDAL.Update() && succeed) {
			message.showMessage("��Ϣ", "���ĳɹ�");
			SearchRdInfo();
			message.showAndWait();
			// ˢ��BookTable
		} else {
			message.showMessage("��Ϣ", "����ʧ��");
			message.showAndWait();
		}
	}

	// ����
	@FXML
	private void Bottom_button_relend(MouseEvent event) {
		Borrow borrow = BorrowTable.getSelectionModel().getSelectedItem();
		if (borrow == null)
			return;
		selection.showMessage("��Ϣ", "ȷ�����裿");
		Optional<ButtonType> optional = selection.showAndWait();
		if (optional.get() == ButtonType.NO)
			return;
		connectDB.GetTable(
				"SELECT CanContinueTimes FROM Library.dbo.TB_ReaderType WHERE rdType = '" + reader.getRdType() + "'");
		ResultSet resultSet = connectDB.getResult();
		// ���������
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
			message.showMessage("��Ϣ", "�Ѵﵽ�����������");
			message.showAndWait();
			return;
		}
		if (!reader.getRdStatus().equals("��Ч")) {
			message.showMessage("��Ϣ", "����Ч�˻�");
			message.showAndWait();
			return;
		}
		// �����������һ
		borrow.setIdContinueTimes((Integer.parseInt(borrow.getIdContinueTimes()) + 1) + "");
		borrow.setIdDateOut(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(5, Integer.parseInt(CanLendDay.getText()));
		borrow.setIdDateRetPlan(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
		BorrowDAL borrowDAL = new BorrowDAL(connectDB);
		borrowDAL.setBorrow(borrow);

		if (borrowDAL.Update()) {
			message.showMessage("��Ϣ", "����ɹ�!");
			SearchRdInfo();
			message.showAndWait();
		} else {
			message.showMessage("��Ϣ", "����ʧ��!");
			message.showAndWait();
		}
	}

	// ��������
	@FXML
	private void Bottom_button_gbBook(MouseEvent event) {
		Borrow borrow = BorrowTable.getSelectionModel().getSelectedItem();
		if (borrow == null)
			return;
		// ��ȡͼ��۸�
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
		// ���������������
		BackBook backBook = new BackBook(borrow.getIdOverMoney(), bookPrice);
		backBook.display();
		if (!backBook.getCloseWay())
			return;
		else {
			// ���û������Ա����
			borrow.setOperatorRet(reader.getRdName());
			// ȷ�ϸ���
			boolean updateBookStatus = false;
			// ��״̬��Ϊ�ѻ�
			borrow.setIsHasReturn(true);
			if (backBook.isLost()) {
				borrow.setIdPunishMoney(backBook.getPunish());
				updateBookStatus = connectDB.UpdateTable(
						"Update Library.dbo.TB_Book SET bkStatus = ? WHERE bkID = '" + borrow.getBkID() + "'", null,
						null, new String[] { "��ʧ" });
			} else {
				updateBookStatus = connectDB.UpdateTable(
						"UPDATE Library.dbo.TB_Book SET bkStatus = ? WHERE bkID = '" + borrow.getBkID() + "'", null,
						null, new String[] { "�ڹ�" });
			}
			BorrowDAL borrowDAL = new BorrowDAL(connectDB);
			borrowDAL.setBorrow(borrow);

			reader.setRdBorrowQty((Integer.parseInt(reader.getRdBorrowQty()) - 1) + "");
			ReaderDAL readerDAL = new ReaderDAL(connectDB);
			readerDAL.setReader(reader);

			if (borrowDAL.Update() && readerDAL.Update() && updateBookStatus) {
				message.showMessage("��Ϣ", "����ɹ�");
				SearchRdInfo();
				message.showAndWait();
				// ˢ��BookTable
			} else {
				message.showMessage("��Ϣ", "����ʧ��");
				message.showAndWait();
			}
		}
	}

	// ������һҳ��
	@FXML
	private void Bottom_button_return(MouseEvent event) {
		Node node = null;
		if ((node = back.getReturn()) != null) {
			node.invokeReturn();
		}
	}

	/**
	 * 
	 * Ϊ������Ϣ��������һ��˽����<br/>
	 * Ŀ����Ϊ���ڶ���Ȩ��Ϊ��ʱ<br/>
	 * ֱ�ӵ��ô˷�����ˢ�¶�����Ϣ
	 */
	private void SearchRdInfo() {
		if (ops == 1) {
			// ��Ϊ���ߵ�½�����Զ����Ϊ��½��ID
			rdID.setText(IMP_Login.getLoginReader().getRdID());
		}
		if (rdID.getText().equals(""))
			return;
		// �����ݿ⴫�ݸ������󣬴��������������·���
		connectDB.UpdateTable("UPDATE Library.dbo.TB_Borrow SET rdID = '" + rdID.getText() + "' WHERE rdID = '"
				+ rdID.getText() + "'", null, null, new String[] {});
		// ����������:���߱� �������� �� ���ı�
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
				message.showMessage("��Ϣ", "δ�ҵ�");
				message.showAndWait();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void RenewBorrowTable() {
		// �ٴ���������<br/>
		// ��Ϊ��ʱ���߽�/��������������ѹ�ʱ
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
