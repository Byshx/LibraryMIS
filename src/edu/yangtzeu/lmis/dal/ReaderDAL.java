package edu.yangtzeu.lmis.dal;

import edu.yangtzeu.lmis.model.Reader;

public class ReaderDAL extends AbstactDAL {

	private Reader reader = null;

	public ReaderDAL() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get the handle of connect
	 */
	private ConnectDB cdb = null;

	public ReaderDAL(ConnectDB cdb) {
		// TODO Auto-generated constructor stub
		this.cdb = cdb;
	}

	public void setReader(Reader reader) {
		this.reader = reader;
	}
	// Four main methods

	public boolean Insert() {
		// TODO Auto-generated method stub
		String[] params = { reader.getRdID(), reader.getRdName(), reader.getRdSex(), reader.getRdType(),
				reader.getRdDept(), reader.getRdPhone(), reader.getRdEmail(), reader.getRdDateReg(),
				reader.getRdStatus(), reader.getRdBorrowQty(), reader.getRdPwd(), reader.getRdAdminRoles() };
		return cdb.InsertTable("INSERT Library.dbo.TB_Reader VALUES(?,?,?,?,?,?,?,?,null,?,?,?,?)", reader.getRdPhoto(),
				9, params);
	}

	public boolean Delete() {
		// TODO Auto-generated method stub
		return cdb.DeleteTable("DELETE Library.dbo.TB_Reader WHERE rdID = '" + reader.getRdID() + "'");
	}

	public boolean Update() {
		// TODO Auto-generated method stub
		String[] params = { reader.getRdID(), reader.getRdName(), reader.getRdSex(), reader.getRdType(),
				reader.getRdDept(), reader.getRdPhone(), reader.getRdEmail(), reader.getRdDateReg(),
				reader.getRdStatus(), reader.getRdBorrowQty(), reader.getRdPwd(), reader.getRdAdminRoles() };
		return cdb.UpdateTable(
				"UPDATE Library.dbo.TB_Reader SET rdID=?,rdName=?,rdSex=?,rdType=?,rdDept=?,rdPhone=?,rdEmail=?,rdDateReg=?,rdStatus =?,rdBorrowQty=?,rdPwd=?,rdAdminRoles=? WHERE rdID = '"
						+ reader.getRdID() + "'",
				"UPDATE Library.dbo.TB_Reader SET rdPhoto=? WHERE rdID = '" + reader.getRdID() + "'",
				reader.getRdPhoto(), params);
	}

	public boolean Search(String t_sql) {
		// TODO Auto-generated method stub
		return cdb.GetTable(t_sql);
	}

}
