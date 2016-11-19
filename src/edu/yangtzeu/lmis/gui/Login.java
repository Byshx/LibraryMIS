package edu.yangtzeu.lmis.gui;

import edu.yangtzeu.lmis.bll.PopMessage;
import edu.yangtzeu.lmis.bll.ReturnStack;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Login extends Application {

	/**
	 * The platform to show components
	 */
	public static Stage ShowPlatform = null;

	/**
	 * When you failed/succeed to do something,you will get message <br/>
	 * For example<br/>
	 * <code>message.showMessage("消息","密码错误");</code>
	 */
	private PopMessage message = new PopMessage();
	/**
	 * 创建返回记录类
	 */
	public ReturnStack back = ReturnStack.initInstance();

	/**
	 * Show the UI
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		ShowPlatform = primaryStage;
		primaryStage.getIcons().add(new Image(Login.class.getResourceAsStream("icon.png")));
		primaryStage.initStyle(StageStyle.UNIFIED);
		UI_Factory.LOGIN.showUI(primaryStage, "登陆");
		message.initOwner(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
