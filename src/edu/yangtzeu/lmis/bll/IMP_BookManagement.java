package edu.yangtzeu.lmis.bll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import edu.yangtzeu.lmis.bll.ReturnStack.Node;
import edu.yangtzeu.lmis.dal.BookDAL;
import edu.yangtzeu.lmis.dal.ConnectDB;
import edu.yangtzeu.lmis.dal.ReaderDAL;
import edu.yangtzeu.lmis.gui.Login;
import edu.yangtzeu.lmis.gui.UI_Factory;
import edu.yangtzeu.lmis.model.Book;
import edu.yangtzeu.lmis.model.Reader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class IMP_BookManagement extends LibraryBLL {

	// TopBar �˵�����
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

	// �������

	@FXML
	private TextField bkID;
	@FXML
	private TextField bkCode;
	@FXML
	private TextField bkName;
	@FXML
	private TextField bkAuthor;
	@FXML
	private TextField bkPress;
	@FXML
	private TextField bkDatePress;
	@FXML
	private TextField bkISBN;
	@FXML
	private TextField bkCatalog;
	@FXML
	private ChoiceBox<String> bkLanguage;
	@FXML
	private TextField bkPages;
	@FXML
	private TextField bkPrice;
	@FXML
	private TextField bkDateln;
	@FXML
	private TextField bkSum;
	@FXML
	private TextField bkStatus;

	// �в��鼮���

	@FXML
	private TextArea bkBrief;

	// �Ҳ����

	@FXML
	private Pane bkCover;

	// �²���ť

	@FXML
	private Button Bottom_button_add;
	@FXML
	private Button Bottom_button_modify;
	@FXML
	private Button Bottom_button_cancel;
	@FXML
	private Button Bottom_button_return;
	@FXML
	private Button add_bkCover;

	/**
	 * ��ȡչʾƽ̨
	 */
	private Stage stage = null;

	private ConnectDB connectDB = null;

	// ��ʾ��
	private PopSelection selection = null;

	// ��Ϣ��
	private PopMessage message = null;

	/**
	 * ���ڻ���ϴ�ͼƬ������
	 */
	private static InputStream image = null;

	/**
	 * ��ҳ��Ĳ�����ʽ <br/>
	 * 0 -- �������<br/>
	 * 1 -- ͼ����Ϣ�޸�<br/>
	 */
	private static int ops = 0;

	/**
	 * �޸���Ϣʱ��ȡ��ԭ����Ϣ
	 */
	private static String[] info = null;

	// ��÷��ص���
	private ReturnStack back = ReturnStack.getReturnInstance();

	/**
	 * ��ʼ��
	 */

	public void initialize() {
		connectDB = ConnectDB.getDBConnection();
		if (ops == 0) {
			Bottom_button_modify.setDisable(true);
			bkStatus.setText("�ڹ�");
			bkStatus.setDisable(true);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			bkDateln.setText(simpleDateFormat.format(new Date()));
		} else {
			Bottom_button_add.setDisable(true);
			// �޸Ĳ������޸��鼮��Ϣ�����������ɸ�
			bkSum.setDisable(true);
			bkDateln.setDisable(true);
			initialInfo();
		}
		stage = Login.ShowPlatform;
		message = new PopMessage();
		selection = new PopSelection();
	}

	/**
	 * ���ô�ҳ��Ĳ��� 0Ϊ������� 1Ϊͼ����Ϣ�޸�
	 */
	public static void setOps(int ops) {
		IMP_BookManagement.ops = ops;
	}

	public static void setInfo(String[] info) {
		IMP_BookManagement.info = info;
	}

	private void initialInfo() {
		// д��Ҫ�޸ĵ���Ϣ
		bkID.setText(info[0]);
		bkCode.setText(info[1]);
		bkName.setText(info[2]);
		bkAuthor.setText(info[3]);
		bkPress.setText(info[4]);
		bkDatePress.setText(info[5]);
		bkISBN.setText(info[6]);
		bkCatalog.setText(info[7]);
		bkLanguage.getSelectionModel().select(info[8]);
		bkPages.setText(info[9]);
		bkPrice.setText(info[10]);
		bkDateln.setText(info[11]);
		bkSum.setText(info[12]);
		bkBrief.setText(info[13]);
		bkStatus.setText(info[14]);
		connectDB.GetTable("SELECT bkCover FROM Library.dbo.TB_Book WHERE bkID = '" + bkID.getText() + "'");
		ResultSet image = connectDB.getResult();
		try {
			image.next();
			Blob inImage = image.getBlob(1);
			if (inImage != null) {
				Image photo = new Image(inImage.getBinaryStream());
				ImageView imageView = new ImageView(photo);
				imageView.setFitWidth(400);
				imageView.setFitHeight(540);
				bkCover.getChildren().add(imageView);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// TopBar�¼���Ӧ

	@FXML
	public void ReaderManagement(ActionEvent event) {
		if (EnumAdminRole.LendCardManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookManagement, "�������", ops, 0);
			UI_Factory.ReaderOp.showUI(stage, "���߹���");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}
	}

	@FXML
	public void ReaderTypeOp(ActionEvent event) {
		if (EnumAdminRole.LendCardManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookManagement, "�������", ops, 0);
			UI_Factory.ReaderTypeOp.showUI(stage, "����������");
		} else {
			message.showMessage("��ʾ", "��û��Ȩ��");
			message.showAndWait();
		}
	}

	@FXML
	public void NewbookIn(ActionEvent event) {
		// ���ڱ�ҳ�������ж�Ȩ��
		back.addReturn(UI_Factory.BookManagement, "�������", ops, 0);
		IMP_BookManagement.setOps(0);
		UI_Factory.BookManagement.showUI(stage, "�������");
	}

	@FXML
	public void LibInfoManagement(ActionEvent event) {
		back.addReturn(UI_Factory.BookManagement, "�������", ops, 0);
		UI_Factory.BookSearch.showUI(stage, "ͼ����Ϣ����");
	}

	@FXML
	public void BorrowManagement(ActionEvent event) {
		if (!EnumAdminRole.LendManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			IMP_BookIO.setOps(1);
		}
		back.addReturn(UI_Factory.BookManagement, "�������", ops, 0);
		UI_Factory.BookIO.showUI(stage, "���Ĺ���");
	}

	@FXML
	public void PowerManagement(ActionEvent event) {
		if (EnumAdminRole.SystemManager.Cmp(null, Integer.parseInt(IMP_Login.getLoginReader().getRdAdminRoles()))) {
			back.addReturn(UI_Factory.BookManagement, "ͼ����Ϣ����", ops, 0);
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

	@FXML
	private void Bottom_button_add(MouseEvent event) {
		selection.showMessage("��Ϣ", "ȷ�����?");
		Optional<ButtonType> optional = selection.showAndWait();
		if (optional.get() == ButtonType.NO)
			return;
		BookDAL bookDAL = new BookDAL(connectDB);
		Book book = Add_OR_Modify();
		book.setBkID(bkID.getText());
		bookDAL.setBook(book);
		int tempID = Integer.parseInt(bkID.getText());
		boolean succeed = true;
		for (int i = 0; i < Integer.parseInt(bkSum.getText()); i++) {
			if (!bookDAL.Insert()) {
				succeed = false;
				break;
			}
			// ͼ�����Ҫ��1
			book.setBkID((++tempID) + "");
		}
		if (succeed) {
			message.showMessage("��Ϣ", "���ɹ�");
			message.showAndWait();
			ClearAll();
		} else {
			message.showMessage("��Ϣ", "���ʧ��");
			message.showAndWait();
		}
	}

	@FXML
	private void Bottom_button_modify(MouseEvent event) {
		selection.showMessage("��Ϣ", "ȷ���޸�?");
		Optional<ButtonType> optional = selection.showAndWait();
		if (optional.get() == ButtonType.NO)
			return;
		BookDAL bookDAL = new BookDAL(connectDB);
		Book book = Add_OR_Modify();
		book.setBkID(bkID.getText());
		// �޸�ֻ�޸�ָ����һ����
		bookDAL.setBook(book);
		if (bookDAL.Update()) {
			message.showMessage("��Ϣ", "�޸ĳɹ�");
			message.showAndWait();
			Node node = null;
			if ((node = back.getReturn()) != null) {
				node.invokeReturn();
			}
		} else {
			message.showMessage("��Ϣ", "�޸�ʧ��");
			message.showAndWait();
		}
	}

	@FXML
	private void Bottom_button_cancel(MouseEvent event) {
		if (Bottom_button_add.isDisable())
			initialInfo();
		else
			ClearAll();
	}

	@FXML
	private void Bottom_button_return(MouseEvent event) {
		Node node = null;
		if ((node = back.getReturn()) != null) {
			node.invokeReturn();
		}
	}

	@FXML
	private void add_bkCover(MouseEvent event) {
		File file = FileChoose.OpenFileLocation(stage, 1);
		if (file == null)
			return;
		bkCover.getChildren().clear();
		try {
			InputStream in = new FileInputStream(file);
			Image photo = new Image(in);
			ImageView imageView = new ImageView(photo);
			imageView.setFitWidth(400);
			imageView.setFitHeight(539);
			bkCover.getChildren().add(imageView);
			image = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void ClearAll() {
		bkID.clear();
		bkCode.clear();
		bkName.clear();
		bkAuthor.clear();
		bkPress.clear();
		bkDatePress.clear();
		bkISBN.clear();
		bkCatalog.clear();
		bkLanguage.getSelectionModel().clearSelection();
		bkPages.clear();
		bkPrice.clear();
		bkDateln.clear();
		bkSum.clear();
		bkStatus.clear();
		bkBrief.clear();
		bkCover.getChildren().clear();
		image = null;
	}

	private Book Add_OR_Modify() {
		Book book = new Book();
		// �ڲ������ʱ��̬����
		// book.setBkID(bkID.getText());
		book.setBkCode(bkCode.getText());
		book.setBkName(bkName.getText());
		book.setBkAuthor(bkAuthor.getText());
		book.setBkPress(bkPress.getText());
		book.setBkDatePress(bkDatePress.getText());
		book.setBkISBN(bkISBN.getText());
		book.setBkCatalog(bkCatalog.getText());
		EnumBookLanguage enumBookLanguage[] = EnumBookLanguage.values();
		for (int i = 0; i < enumBookLanguage.length; i++) {
			if (enumBookLanguage[i].Cmp(bkLanguage.getSelectionModel().getSelectedItem(), -1)) {
				book.setBkLanguage(enumBookLanguage[i].getLanguageSign() + "");
				break;
			}
		}
		book.setBkPages(bkPages.getText());
		book.setBkPrice(bkPrice.getText());
		book.setBkDateIn(bkDateln.getText());
		book.setBkBrief(bkBrief.getText());
		book.setBkCover(image);
		book.setBkStatus(bkStatus.getText());
		return book;
	}
}
