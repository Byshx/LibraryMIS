package edu.yangtzeu.lmis.bll;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Excel�����࣬ʹ��ǰ���ʵ����ʵ��<br/>
 * ExcelNodeFormat�ӿ�
 * 
 * @author ����
 *
 */

public class GenerateExcel {

	private TableView<?> tableView = null;
	private Stage stage = null;

	public GenerateExcel(TableView<?> tableView, Stage stage) {
		// TODO Auto-generated constructor stub
		this.tableView = tableView;
		this.stage = stage;
	}

	/**
	 * ����ɹ���ȡ������null<br/>
	 * ���ʧ�ܷ���ʧ��ԭ��(String)<br/>
	 * title��������<br/>
	 * printColumn������Ҫ�������<br/>
	 * fileNameΪ������ļ���
	 * 
	 * @param
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String generate(String[] title, int[] printColumn, String fileName) {
		if (tableView.getItems() == null)
			return "��������ձ�";
		File file = FileChoose.SaveFileLocation(stage, 0);
		if (file == null)
			return null;
		try {
			WritableWorkbook workbook = null;
			OutputStream outputStream = new FileOutputStream(file);
			workbook = Workbook.createWorkbook(outputStream);
			WritableSheet sheet = workbook.createSheet(fileName, 0);
			try {
				for (int i = 0; i < title.length; i++) {
					sheet.addCell(new Label(i, 0, title[i]));
				}
				ObservableList<?> list = tableView.getItems();
				try {
					for (int i = 0; i < list.size(); i++) {
						List<String> getList;
						// ���ͻ�ȡĳʵ���List<String>
						getList = (List<String>) list.get(i).getClass().getMethod("bindNode").invoke(list.get(i),
								new Object[0]);
						for (int j = 0; j < printColumn.length; j++) {
							sheet.addCell(new Label(printColumn[j], i + 1, getList.get(printColumn[j])));
						}
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				workbook.write();
				workbook.close();
				outputStream.close();
			} catch (RowsExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
