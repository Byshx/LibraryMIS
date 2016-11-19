package edu.yangtzeu.lmis.bll;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FileChoose {

	private final static FileChooser fileChooser = new FileChooser();

	/**
	 * *.xls为Excel2003版 *.xlsx为Excel2007版
	 * 
	 */

	@SuppressWarnings("serial")
	private final static List<ExtensionFilter> Excel_extensionFilter = new ArrayList<ExtensionFilter>() {
		{
			add(new ExtensionFilter("Excel files(*.xlsx)", "*.xlsx"));
			add(new ExtensionFilter("Excel files(*.xls)", "*.xls"));
		}
	};

	/**
	 * Image类型<br />
	 * JPG JPEG BMP GIF PNG
	 */

	@SuppressWarnings("serial")
	private final static List<FileChooser.ExtensionFilter> Image_extensionFilter = new ArrayList<FileChooser.ExtensionFilter>() {
		{
			add(new FileChooser.ExtensionFilter("Image files(*.jpg)", "*.jpg"));
			add(new FileChooser.ExtensionFilter("Image files(*.png)", "*.png"));
			add(new FileChooser.ExtensionFilter("Image files(*.jpeg)", "*.jpeg"));
			add(new FileChooser.ExtensionFilter("Image files(*.gif)", "*.gif"));
			add(new FileChooser.ExtensionFilter("Image files(*.bmp)", "*.bmp"));
		}
	};

	/**
	 * 取得存放文件的目录<br />
	 * type为0 文件类型为.excel<br />
	 * type为1文件类型为 .JPG .JPEG .BMP .PNG .GIF
	 * 
	 * @return File
	 */
	public static File SaveFileLocation(Stage stage, int type) {
		fileChooser.setTitle("选择存放路径");
		fileChooser.getExtensionFilters().clear();
		if (type == 0) {
			fileChooser.getExtensionFilters().addAll(Excel_extensionFilter);
		} else {
			fileChooser.getExtensionFilters().addAll(Image_extensionFilter);
		}
		return fileChooser.showSaveDialog(stage);
	}

	/**
	 * 取得获取文件的目录<br />
	 * type为0 文件类型为.excel<br />
	 * type为1文件类型为 .JPG .JPEG .BMP .PNG .GIF
	 * 
	 * @return File
	 */

	public static File OpenFileLocation(Stage stage, int type) {
		fileChooser.setTitle("选择存放路径");
		fileChooser.getExtensionFilters().clear();
		if (type == 0) {
			fileChooser.getExtensionFilters().addAll(Excel_extensionFilter);
		} else {
			fileChooser.getExtensionFilters().addAll(Image_extensionFilter);
		}
		return fileChooser.showOpenDialog(stage);
	}

}
