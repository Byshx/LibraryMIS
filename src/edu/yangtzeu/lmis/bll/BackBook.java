package edu.yangtzeu.lmis.bll;

import edu.yangtzeu.lmis.gui.Login;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BackBook {

	private boolean CLOSE_WINDOW_NORMAL = true;

	private boolean BOOK_ISLOST = false;

	private final static int FEE = 5; // �ӹ���

	private String Overday = null;

	private String BookPrice = null;

	public BackBook(String Overday, String BookPrice) {
		// TODO Auto-generated constructor stub
		this.Overday = Overday;
		this.BookPrice = BookPrice;
	}

	public void display() {
		Stage window = new Stage();
		window.setTitle("����");
		// modalityҪʹ��Modality.APPLICATION_MODEL
		window.initModality(Modality.APPLICATION_MODAL);
		// ���������ͼ��
		window.getIcons().add(Login.ShowPlatform.getIcons().get(0));
		window.initStyle(StageStyle.UNDECORATED);
		window.setOpacity(0.7);
		window.setMinWidth(300);
		window.setMinHeight(150);

		Label label1 = new Label("���ڷ���:");
		label1.setPadding(new Insets(0, 35, 0, 0));
		TextField t1 = new TextField();
		t1.setText(Overday == null ? "0" : Overday);
		t1.setEditable(false);

		Label label2 = new Label("�ܷ���:");
		label2.setPadding(new Insets(0, 50, 0, 0));
		TextField t2 = new TextField();
		t2.setText("0");
		t2.setEditable(false);

		HBox one = new HBox();
		one.setPadding(new Insets(20, 20, 20, 20));
		one.getChildren().addAll(label1, t1);

		HBox two = new HBox();
		two.setPadding(new Insets(20, 20, 20, 20));
		two.getChildren().addAll(label2, t2);

		ToggleGroup toggleGroup = new ToggleGroup();

		RadioButton r1 = new RadioButton("��");
		r1.setSelected(true);
		RadioButton r2 = new RadioButton("��ʧ");

		// ��Ϊһ�飬���ڵ�ѡ�ų�
		r1.setToggleGroup(toggleGroup);
		r2.setToggleGroup(toggleGroup);

		toggleGroup.selectedToggleProperty()
				.addListener((ObservableValue<? extends Toggle> ov, Toggle old_Toggle, Toggle new_Toggle) -> {
					if (r2.isSelected()) {
						BOOK_ISLOST = true;
						t2.setText(getPunish());
					} else {
						t2.setText("0");
						BOOK_ISLOST = false;
					}
				});

		HBox three = new HBox();
		three.setAlignment(Pos.CENTER);
		three.setPadding(new Insets(20, 20, 20, 20));
		three.setSpacing(10);
		three.getChildren().addAll(r1, r2);

		Label tip = new Label("*ע����ʧ���� = �鼮ԭ�� + �ӹ���(5Ԫ)");

		// ��ť
		Button ok = new Button("ȷ������");
		
		ok.setOnAction(e -> {
			// �����������жϴ���
			window.close();
		});

		Button close = new Button("ȡ������");
		close.setOnAction(e -> {
			// �����������жϴ���
			CLOSE_WINDOW_NORMAL = false;
			window.close();
		});

		HBox button = new HBox();
		button.setAlignment(Pos.CENTER);
		button.setSpacing(40);
		button.setPadding(new Insets(20, 20, 20, 20));
		button.getChildren().addAll(ok, close);

		VBox layout = new VBox();
		layout.setPadding(new Insets(20, 20, 20, 20));
		layout.getChildren().addAll(one, two, three, tip, button);
		layout.setAlignment(Pos.CENTER);
		// ���ñ���ɫ
		layout.setStyle("-fx-background: #000000;");

		Scene scene = new Scene(layout);
		window.setScene(scene);
		// ʹ��showAndWait()�ȴ���������ڣ������������main�е��Ǹ����ڲ�����Ӧ
		window.showAndWait();
	}

	/**
	 * ���ض�ʧ����
	 * 
	 * @return
	 */
	public String getPunish() {
		if (BOOK_ISLOST) {
			return Float.parseFloat(BookPrice) + FEE + "";
		}
		return "0";
	}

	/**
	 * �ж��Ƿ�ȡ������
	 * 
	 * @return
	 */
	public boolean getCloseWay() {
		return CLOSE_WINDOW_NORMAL;
	}

	public boolean isLost() {
		return BOOK_ISLOST;
	}

}
