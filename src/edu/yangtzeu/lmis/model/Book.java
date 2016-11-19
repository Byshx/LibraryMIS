package edu.yangtzeu.lmis.model;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.yangtzeu.lmis.bll.EnumBookLanguage;
import edu.yangtzeu.lmis.bll.ExcelNodeFormat;
import javafx.beans.property.SimpleStringProperty;

public class Book extends AbstractModel implements ExcelNodeFormat {

	// 图书各字段信息

	//图书序号 代表指定的、唯一的一本书
	private SimpleStringProperty bkID = null;
	//索书号 代表一种书
	private SimpleStringProperty bkCode = null;

	private SimpleStringProperty bkName = null;

	private SimpleStringProperty bkAuthor = null;

	private SimpleStringProperty bkPress = null;

	private SimpleStringProperty bkDatePress = null;

	private SimpleStringProperty bkISBN = null;

	private SimpleStringProperty bkCatalog = null;

	private SimpleStringProperty bkLanguage = null;
	// 书籍语言名称，根据表格需要额外添加属性
	private SimpleStringProperty bkLanguageName = null;

	private SimpleStringProperty bkPages = null;

	private SimpleStringProperty bkPrice = null;

	private SimpleStringProperty bkDateIn = null;

	private SimpleStringProperty bkBrief = null;

	private InputStream bkCover = null;

	private SimpleStringProperty bkStatus = null;

	// Setter

	public void setBkID(String bkID) {
		this.bkID = new SimpleStringProperty(bkID);
	}

	public void setBkCode(String bkCode) {
		this.bkCode = new SimpleStringProperty(bkCode);
	}

	public void setBkName(String bkName) {
		this.bkName = new SimpleStringProperty(bkName);
	}

	public void setBkAuthor(String bkAuthor) {
		this.bkAuthor = new SimpleStringProperty(bkAuthor);
	}

	public void setBkPress(String bkPress) {
		this.bkPress = new SimpleStringProperty(bkPress);
	}

	public void setBkDatePress(String bkDatePress) {
		this.bkDatePress = new SimpleStringProperty(bkDatePress);
	}

	public void setBkISBN(String bkISBN) {
		this.bkISBN = new SimpleStringProperty(bkISBN);
	}

	public void setBkCatalog(String bkCatalog) {
		this.bkCatalog = new SimpleStringProperty(bkCatalog);
	}

	public void setBkLanguage(String bkLanguage) {
		this.bkLanguage = new SimpleStringProperty(bkLanguage);
	}

	public void setBkLanguageName(String bkLanguageName) {
		this.bkLanguageName = new SimpleStringProperty(bkLanguageName);
	}

	public void setBkPages(String bkPages) {
		this.bkPages = new SimpleStringProperty(bkPages);
	}

	public void setBkPrice(String bkPrice) {
		this.bkPrice = new SimpleStringProperty(bkPrice);
	}

	public void setBkDateIn(String bkDateIn) {
		this.bkDateIn = new SimpleStringProperty(bkDateIn);
	}

	public void setBkBrief(String bkBrief) {
		this.bkBrief = new SimpleStringProperty(bkBrief);
	}

	public void setBkCover(InputStream bkCover) {
		this.bkCover = bkCover;
	}

	public void setBkStatus(String bkStatus) {
		this.bkStatus = new SimpleStringProperty(bkStatus);
	}

	// Getter

	public String getBkID() {
		return bkID.get();
	}

	public String getBkCode() {
		return bkCode.get();
	}

	public String getBkName() {
		return bkName.get();
	}

	public String getBkAuthor() {
		return bkAuthor.get();
	}

	public String getBkPress() {
		return bkPress.get();
	}

	public String getBkDatePress() {
		return bkDatePress.get();
	}

	public String getBkISBN() {
		return bkISBN.get();
	}

	public String getBkCatalog() {
		return bkCatalog.get();
	}

	public String getBkLanguage() {
		return bkLanguage.get();
	}

	public String getBkLanguageName() {
		return bkLanguageName.get();
	}

	public String getBkPages() {
		return bkPages.get();
	}

	public String getBkPrice() {
		return bkPrice.get();
	}

	public String getBkDateIn() {
		return bkDateIn.get();
	}

	public String getBkBrief() {
		return bkBrief.get();
	}

	public InputStream getBkCover() {
		return bkCover;
	}

	public String getBkStatus() {
		return bkStatus.get();
	}

	public void setValue(ResultSet resultSet) {
		ResultSet result = resultSet;
		try {
			setBkID(result.getString(1));
			setBkCode(result.getString(2));
			setBkName(result.getString(3));
			setBkAuthor(result.getString(4));
			setBkPress(result.getString(5));
			setBkDatePress(result.getString(6));
			setBkISBN(result.getString(7));
			setBkCatalog(result.getString(8));
			setBkLanguage(result.getString(9));
			EnumBookLanguage enumBookLanguage[] = EnumBookLanguage.values();
			for (int i = 0; i < enumBookLanguage.length; i++) {
				if (enumBookLanguage[i].Cmp(null, Integer.parseInt(result.getString(9)))) {
					setBkLanguageName(enumBookLanguage[i].getLanguageName());
					break;
				}
			}
			setBkPages(result.getString(10));
			setBkPrice(result.getString(11));
			setBkDateIn(result.getString(12));
			setBkBrief(result.getString(13));
			setBkCover(result.getBinaryStream(14));
			setBkStatus(result.getString(15));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<String> bindNode() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
		list.add(getBkID());
		list.add(getBkCode());
		list.add(getBkName());
		list.add(getBkAuthor());
		list.add(getBkPress());
		list.add(getBkDatePress());
		list.add(getBkISBN());
		list.add(getBkCatalog());
		list.add(getBkLanguage());
		list.add(getBkPages());
		list.add(getBkPrice());
		list.add(getBkDateIn());
		list.add(getBkStatus());
		return list;
	}
}
