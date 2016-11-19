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

	// TopBar �˵�����

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

	// �����

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

	// Table��
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

	// Button��Ӧ�¼�

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

	// ��ȡApplicationƽ̨
	private Stage stage = null;

	private ConnectDB connectDB = null;

	// ѡ���
	private PopSelection selection = null;

	// ��Ϣ��
	private PopMessage message = null;

	// ��÷��ص���
	private ReturnStack back = ReturnStack.getReturnInstance();

	public void initialize() {
		connectDB = ConnectDB.getDBConnection();
		BindColumn();
		stage = Login.ShowPlatform;
		message = new PopMessage();
		selection = new PopSelection();
		Renew();
	}

	// TopBar�¼���Ӧ

	@FXML
	public void ReaderManagement(ActionEvent event) {
		// ���ڱ�ҳ�������ж�Ȩ��
		back.addReturn(UI_Factory.ReaderTypeOp, "����������", 0, 0);
		UI_Factory.ReaderOp.showUI(stage, "���߹���");
	}

	@FXML
	public void ReaderTypeOp(ActionEvent event) {
		// ���ڱ�ҳ�������ж�Ȩ��
		back.addReturn(UI_Factory.ReaderTypeOp, "����������", 0, 0);
		UI_Factory.ReaderTypeOp.showUI(stage, "����������");
	}

	@FXML
	public void NewbookIn(ActionEvent event) {
		if (EnumAdminRole.BookManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.ReaderTypeOp, "����������", 0, 0);
			IMP_BookManagement.setOps(0);
			UI_Factory.BookManagement.showUI(stage, "�������");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}
	}

	@FXML
	public void LibInfoManagement(ActionEvent event) {
		if (EnumAdminRole.BookManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.ReaderTypeOp, "����������", 0, 0);
			UI_Factory.BookSearch.showUI(stage, "ͼ����Ϣ����");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}
	}

	@FXML
	public void BorrowManagement(ActionEvent event) {
		if (!EnumAdminRole.LendManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			IMP_BookIO.setOps(1);
		}
		back.addReturn(UI_Factory.ReaderTypeOp, "����������", 0, 0);
		UI_Factory.BookIO.showUI(stage, "���Ĺ���");
	}

	@FXML
	public void PowerManagement(ActionEvent event) {
		if (EnumAdminRole.SystemManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.ReaderTypeOp, "����������", 0, 0);
			UI_Factory.UserPowerManagement.showUI(stage, "Ȩ�޹���");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}
	}

	@FXML
	public void PasswordModify(ActionEvent event) {
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

	// �²���ť�¼���Ӧ
	@FXML
	private void Bottom_button_add(MouseEvent event) {
		selection.showMessage("��Ϣ", "ȷ�����?");
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
			message.showMessage("��Ϣ", "��ӳɹ�");
			message.showAndWait();
			Renew();
		} else {
			message.showMessage("��Ϣ", "���ʧ��");
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
		selection.showMessage("��Ϣ", "ȷ���޸�?");
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
			message.showMessage("��Ϣ", "�޸ĳɹ�");
			message.showAndWait();
			Renew();
		} else {
			message.showMessage("��Ϣ", "�޸�ʧ��");
			message.showAndWait();
		}
	}

	@FXML
	private void Bottom_button_delete(MouseEvent event) {
		selection.showMessage("��Ϣ", "ȷ��ɾ��?");
		Optional<ButtonType> optional = selection.showAndWait();
		if (optional.get() == ButtonType.NO)
			return;
		ReaderType readerType = Table.getSelectionModel().getSelectedItem();
		ReaderTypeDAL readerTypeDAL = new ReaderTypeDAL(connectDB);
		readerTypeDAL.setReaderType(readerType);
		if (readerTypeDAL.Delete()) {
			message.showMessage("��Ϣ", "ɾ���ɹ�");
			message.showAndWait();
			Renew();
		} else {
			message.showMessage("��Ϣ", "ɾ��ʧ��");
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
	 * ����Beanӳ��
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
	 * ˢ�±��
	 */
	private void Renew() {
		String sql = "SELECT * FROM Library.dbo.TB_ReaderType";
		connectDB.GetTable(sql);
		Table.getItems().clear();
		addTableItem(connectDB.getResult());
	}

}
