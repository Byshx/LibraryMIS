package edu.yangtzeu.lmis.dal;

import edu.yangtzeu.lmis.model.Book;

public class BookDAL {

	private Book book = null;

	private ConnectDB connectDB = null;

	public BookDAL() {
		// TODO Auto-generated constructor stub
	}

	public BookDAL(ConnectDB connectDB) {
		this.connectDB = connectDB;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public boolean Insert() {
		String[] params = { book.getBkID(), book.getBkCode(), book.getBkName(), book.getBkAuthor(), book.getBkPress(),
				book.getBkDatePress(), book.getBkISBN(), book.getBkCatalog(), book.getBkLanguage(), book.getBkPages(),
				book.getBkPrice(), book.getBkDateIn(), book.getBkBrief(), book.getBkStatus() };
		return connectDB.InsertTable("INSERT INTO Library.dbo.TB_Book VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,null,?)",
				book.getBkCover(), 14, params);
	}

	public boolean Delete() {
		// TODO Auto-generated method stub
		return connectDB.DeleteTable("DELETE Library.dbo.TB_Book WHERE bkID = '" + book.getBkID() + "'");
	}

	public boolean Update() {
		// TODO Auto-generated method stub
		String[] params = { book.getBkID(), book.getBkCode(), book.getBkName(), book.getBkAuthor(), book.getBkPress(),
				book.getBkDatePress(), book.getBkISBN(), book.getBkCatalog(), book.getBkLanguage(), book.getBkPages(),
				book.getBkPrice(), book.getBkDateIn(), book.getBkBrief(), book.getBkStatus() };
		return connectDB.UpdateTable(
				"UPDATE Library.dbo.TB_Book SET bkID=?,bkCode=?,bkName=?,bkAuthor=?,bkPress=?,bkDatePress=?,bkISBN=?,bkCatalog=?,bkLanguage=?,bkPages=?,bkPrice=?,bkDateIn=?,bkBrief=?,bkStatus=? WHERE bkID = '"
						+ book.getBkID() + "'",
				"UPDATE Library.dbo.TB_Book SET bkCover=? WHERE bkID = '" + book.getBkID() + "'", book.getBkCover(),
				params);
	}

	public boolean Search(String t_sql) {
		// TODO Auto-generated method stub
		return connectDB.GetTable(t_sql);
	}
}
