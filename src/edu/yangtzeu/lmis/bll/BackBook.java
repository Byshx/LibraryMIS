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

	private final static int FEE = 5; // 加工费

	private String Overday = null;

	private String BookPrice = null;

	public BackBook(String Overday, String BookPrice) {
		// TODO Auto-generated constructor stub
		this.Overday = Overday;
		this.BookPrice = BookPrice;
	}

	public void display() {
		Stage window = new Stage();
		window.setTitle("还书");
		// modality要使用Modality.APPLICATION_MODEL
		window.initModality(Modality.APPLICATION_MODAL);
		// 获得主程序图标
		window.getIcons().add(Login.ShowPlatform.getIcons().get(0));
		window.initStyle(StageStyle.UNDECORATED);
		window.setOpacity(0.7);
		window.setMinWidth(300);
		window.setMinHeight(150);

		Label label1 = new Label("超期罚款:");
		label1.setPadding(new Insets(0, 35, 0, 0));
		TextField t1 = new TextField();
		t1.setText(Overday == null ? "0" : Overday);
		t1.setEditable(false);

		Label label2 = new Label("总罚款:");
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

		RadioButton r1 = new RadioButton("无");
		r1.setSelected(true);
		RadioButton r2 = new RadioButton("丢失");

		// 设为一组，用于单选排斥
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

		Label tip = new Label("*注：丢失罚款 = 书籍原价 + 加工费(5元)");

		// 按钮
		Button ok = new Button("确定还书");
		
		ok.setOnAction(e -> {
			// 非正常流程中断窗口
			window.close();
		});

		Button close = new Button("取消还书");
		close.setOnAction(e -> {
			// 非正常流程中断窗口
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
		// 设置背景色
		layout.setStyle("-fx-background: #000000;");

		Scene scene = new Scene(layout);
		window.setScene(scene);
		// 使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
		window.showAndWait();
	}

	/**
	 * 返回丢失罚金
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
	 * 判断是否取消还书
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
