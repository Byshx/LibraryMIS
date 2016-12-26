package edu.yangtzeu.lmis.bll;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import edu.yangtzeu.lmis.bll.ReturnStack.Node;
import edu.yangtzeu.lmis.dal.ConnectDB;
import edu.yangtzeu.lmis.dal.ReaderDAL;
import edu.yangtzeu.lmis.gui.Login;
import edu.yangtzeu.lmis.gui.UI_Factory;
import edu.yangtzeu.lmis.model.Reader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class IMP_UserPowerManagement extends LibraryBLL {
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

	// Table
	@FXML
	private TableView<Reader> Table;
	@FXML
	private TableColumn<Reader, String> Table_ID;
	@FXML
	private TableColumn<Reader, String> Table_Name;
	@FXML
	private TableColumn<Reader, String> Table_Type;
	@FXML
	private TableColumn<Reader, String> Table_Department;
	@FXML
	private TableColumn<Reader, String> Table_Phone;
	@FXML
	private TableColumn<Reader, String> Table_Power;

	// Top��ѡ��

	@FXML
	private ChoiceBox<String> Top_Choice_AdminRole;
	@FXML
	private ChoiceBox<String> Top_Choice_Department;
	@FXML
	private TextField Top_Choice_Name;

	// �Ҳ����

	@FXML
	private TextField rdID;
	@FXML
	private TextField rdDept;
	@FXML
	private TextField rdType;
	@FXML
	private TextField rdPhone;

	// ͼƬPane

	@FXML
	private Pane ImagePane;

	// RadioButton

	@FXML
	private RadioButton LendCardManager;
	@FXML
	private RadioButton LendManager;
	@FXML
	private RadioButton BookManager;
	@FXML
	private RadioButton SystemManager;

	// ��ȡ���ݿ�����
	private ConnectDB connectDB = null;

	// ��ʾ��
	private PopSelection selection = null;

	// ��Ϣ��
	private PopMessage message = null;

	// չʾ���ƽ̨����Login�л�ȡ
	private Stage stage = null;

	// ��÷���ջ
	private ReturnStack back = ReturnStack.getReturnInstance();

	/**
	 * ������ʱ�����û�����Ĳ����ֶ�</br>
	 * 
	 */
	private String tempSQL = "";

	// ������ʱ�洢ѡ�е�Readerʵ��
	private Reader tempReader = null;

	public void initialize() {
		connectDB = ConnectDB.getDBConnection();
		BindColumn();
		set_Choice_AdminRoles(Top_Choice_AdminRole);
		set_Choice_Department(Top_Choice_Department);
		message = new PopMessage();
		selection = new PopSelection();
		stage = Login.ShowPlatform;
	}

	@Override
	public void ReaderManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.LendCardManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.UserPowerManagement, "Ȩ�޹���", 0, 0);
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
			back.addReturn(UI_Factory.UserPowerManagement, "Ȩ�޹���", 0, 0);
			UI_Factory.ReaderTypeOp.showUI(stage, "����������");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}
	}

	@Override
	public void NewbookIn(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.LendCardManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.UserPowerManagement, "Ȩ�޹���", 0, 0);
			UI_Factory.BookManagement.showUI(stage, "�������");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}
	}

	@Override
	public void LibInfoManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.LendCardManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.UserPowerManagement, "Ȩ�޹���", 0, 0);
			UI_Factory.BookSearch.showUI(stage, "ͼ����Ϣ����");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}

	}

	@Override
	public void BorrowManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		if (EnumAdminRole.LendCardManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.UserPowerManagement, "Ȩ�޹���", 0, 0);
			UI_Factory.BookIO.showUI(stage, "���Ĺ���");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}
	}

	@Override
	public void PowerManagement(ActionEvent event) {
		// TODO Auto-generated method stub
		// ���ڱ�ҳ����������Ȩ��
		back.addReturn(UI_Factory.UserPowerManagement, "Ȩ�޹���", 0, 0);
		UI_Factory.UserPowerManagement.showUI(stage, "Ȩ�޹���");
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

	// Button ��Ӧ�¼�
	@FXML
	private void Top_Button_Search(MouseEvent event) {
		String sql = "SELECT * FROM Library.dbo.TB_Reader WHERE ";
		if (!Top_Choice_AdminRole.getSelectionModel().isEmpty()) {
			EnumAdminRole enumAdminRole[] = EnumAdminRole.values();
			for (int i = 0; i < enumAdminRole.length; i++) {
				if (enumAdminRole[i].Cmp(Top_Choice_AdminRole.getSelectionModel().getSelectedItem(), -1)) {
					sql = sql + "rdAdminRoles & " + enumAdminRole[i].getTypeNumber() + " = "
							+ enumAdminRole[i].getTypeNumber() + " AND ";
					break;
				}
			}
		}
		if (Top_Choice_Department.getSelectionModel().getSelectedItem() != null) {
			sql = sql + "rdDept ='" + Top_Choice_Department.getSelectionModel().getSelectedItem() + "' AND ";
		}
		sql = sql + "rdName LIKE '%" + Top_Choice_Name.getText() + "%'";
		System.out.println(sql);
		tempSQL = sql;
		connectDB.GetTable(sql);
		ResultSet resultSet = connectDB.getResult();
		ObservableList<Reader> data = FXCollections.observableArrayList();
		try {
			while (resultSet.next()) {
				Reader reader = new Reader();
				reader.setValue(resultSet);
				data.add(reader);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Table.getItems().clear();
		Table.setItems(data);
	}

	@FXML
	private void Top_Button_Excel(MouseEvent event) {
		String[] title = { "ID", "����", "����", "Ժϵ", "�绰", "Ȩ��" };
		GenerateExcel generateExcel = new GenerateExcel(Table, stage);
		generateExcel.generate(title, new int[] { 0, 1, 3, 4, 5, 11 }, "�û�Ȩ�ޱ�");
	}

	@FXML
	private void Right_Button_Select(MouseEvent event) {
		Reader reader = Table.getSelectionModel().getSelectedItem();
		tempReader = reader;
		if (reader == null) {
			return;
		}
		rdID.setText(reader.getRdID());
		rdDept.setText(reader.getRdDept());
		rdPhone.setText(reader.getRdPhone());
		int rolesNumber = Integer.parseInt(reader.getRdAdminRoles());
		connectDB.GetTable(
				"SELECT rdTypeName FROM Library.dbo.TB_ReaderType WHERE rdType = '" + reader.getRdType() + "'");
		ResultSet resultSet = connectDB.getResult();
		try {
			if (resultSet.next()) {
				rdType.setText(resultSet.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ��RadioButton��Ϊԭλ
		LendCardManager.setSelected(false);
		LendManager.setSelected(false);
		BookManager.setSelected(false);
		SystemManager.setSelected(false);

		if (EnumAdminRole.LendCardManager.Cmp(null, rolesNumber)) {
			LendCardManager.setSelected(true);
		}
		if (EnumAdminRole.LendManager.Cmp(null, rolesNumber)) {
			LendManager.setSelected(true);
		}
		if (EnumAdminRole.BookManager.Cmp(null, rolesNumber)) {
			BookManager.setSelected(true);
		}
		if (EnumAdminRole.SystemManager.Cmp(null, rolesNumber)) {
			SystemManager.setSelected(true);
		}
		connectDB.GetTable("SELECT rdPhoto FROM Library.dbo.TB_Reader WHERE rdID = '" + reader.getRdID() + "'");
		ResultSet image = connectDB.getResult();
		try {
			if (image.next()) {
				Blob inImage = image.getBlob(1);
				if (inImage == null) {
					return;
				}
				Image photo = new Image(inImage.getBinaryStream());
				ImageView imageView = new ImageView(photo);
				imageView.setFitWidth(240);
				imageView.setFitHeight(300);
				ImagePane.getChildren().add(imageView);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void Right_Button_ConfirmChange(MouseEvent event) {
		if (tempReader == null)
			return;
		selection.showMessage("ȷ��", "ȷ�ϸ���Ȩ��?");
		Optional<ButtonType> result = selection.showAndWait();
		if (result.get() == ButtonType.NO)
			return;
		int admin = 0;
		if (LendCardManager.isSelected()) {
			admin += EnumAdminRole.LendCardManager.getTypeNumber();
		}
		if (LendManager.isSelected()) {
			admin += EnumAdminRole.LendManager.getTypeNumber();
		}
		if (BookManager.isSelected()) {
			admin += EnumAdminRole.BookManager.getTypeNumber();
		}
		if (SystemManager.isSelected()) {
			admin += EnumAdminRole.SystemManager.getTypeNumber();
		}
		tempReader.setRdAdminRoles(admin + "");
		ReaderDAL readerDAL = new ReaderDAL(connectDB);
		readerDAL.setReader(tempReader);
		if (readerDAL.Update()) {
			message.showMessage("��Ϣ", "���ĳɹ�");
			RefreshTable();
			message.showAndWait();
			return;
		}
		message.showMessage("��Ϣ", "����ʧ��");
		message.showAndWait();

	}

	@FXML
	private void Right_Button_Cancel(MouseEvent event) {
		if (tempReader == null) {
			return;
		}
		rdID.setText(tempReader.getRdID());
		rdDept.setText(tempReader.getRdDept());
		rdPhone.setText(tempReader.getRdPhone());
		int rolesNumber = Integer.parseInt(tempReader.getRdAdminRoles());
		connectDB.GetTable(
				"SELECT rdTypeName FROM Library.dbo.TB_ReaderType WHERE rdType = '" + tempReader.getRdType() + "'");
		ResultSet resultSet = connectDB.getResult();
		try {
			if (resultSet.next()) {
				rdType.setText(resultSet.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (EnumAdminRole.LendCardManager.Cmp(null, rolesNumber)) {
			LendCardManager.setSelected(true);
		}
		if (EnumAdminRole.LendManager.Cmp(null, rolesNumber)) {
			LendManager.setSelected(true);
		}
		if (EnumAdminRole.BookManager.Cmp(null, rolesNumber)) {
			BookManager.setSelected(true);
		}
		if (EnumAdminRole.SystemManager.Cmp(null, rolesNumber)) {
			SystemManager.setSelected(true);
		}
	}

	@FXML
	private void Right_Button_Return(MouseEvent event) {
		Node node = null;
		if ((node = back.getReturn()) != null) {
			node.invokeReturn();
		}
	}

	private void BindColumn() {
		Table_ID.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdID"));
		Table_Name.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdName"));
		Table_Type.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdType"));
		Table_Department.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdDept"));
		Table_Phone.setCellValueFactory(new PropertyValueFactory<Reader, String>("rdPhone"));
		Table_Power.setCellValueFactory(new PropertyValueFactory<Reader, String>("rolesName"));
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

	private void set_Choice_AdminRoles(ChoiceBox<String> choiceBox) {
		EnumAdminRole enumAdminRole[] = EnumAdminRole.values();
		for (int i = 0; i < enumAdminRole.length; i++) {
			choiceBox.getItems().add(enumAdminRole[i].getTypeName());
		}
	}

	/**
	 * ˢ�¹���Ա�б�
	 */
	private void RefreshTable() {
		connectDB.GetTable(tempSQL);
		ResultSet resultSet = connectDB.getResult();
		ObservableList<Reader> data = FXCollections.observableArrayList();
		try {
			while (resultSet.next()) {
				Reader reader = new Reader();
				reader.setValue(resultSet);
				data.add(reader);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Table.setItems(data);
	}

}
