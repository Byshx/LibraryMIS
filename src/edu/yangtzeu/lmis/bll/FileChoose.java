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
	 * *.xlsΪExcel2003�� *.xlsxΪExcel2007��
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
	 * Image����<br />
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
	 * ȡ�ô���ļ���Ŀ¼<br />
	 * typeΪ0 �ļ�����Ϊ.excel<br />
	 * typeΪ1�ļ�����Ϊ .JPG .JPEG .BMP .PNG .GIF
	 * 
	 * @return File
	 */
	public static File SaveFileLocation(Stage stage, int type) {
		fileChooser.setTitle("ѡ����·��");
		fileChooser.getExtensionFilters().clear();
		if (type == 0) {
			fileChooser.getExtensionFilters().addAll(Excel_extensionFilter);
		} else {
			fileChooser.getExtensionFilters().addAll(Image_extensionFilter);
		}
		return fileChooser.showSaveDialog(stage);
	}

	/**
	 * ȡ�û�ȡ�ļ���Ŀ¼<br />
	 * typeΪ0 �ļ�����Ϊ.excel<br />
	 * typeΪ1�ļ�����Ϊ .JPG .JPEG .BMP .PNG .GIF
	 * 
	 * @return File
	 */

	public static File OpenFileLocation(Stage stage, int type) {
		fileChooser.setTitle("ѡ����·��");
		fileChooser.getExtensionFilters().clear();
		if (type == 0) {
			fileChooser.getExtensionFilters().addAll(Excel_extensionFilter);
		} else {
			fileChooser.getExtensionFilters().addAll(Image_extensionFilter);
		}
		return fileChooser.showOpenDialog(stage);
	}

}
