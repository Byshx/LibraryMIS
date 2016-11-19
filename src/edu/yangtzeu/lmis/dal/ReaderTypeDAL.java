package edu.yangtzeu.lmis.dal;

import edu.yangtzeu.lmis.model.ReaderType;

public class ReaderTypeDAL extends AbstactDAL {

	/**
	 * 数据库连接引用
	 */
	private ConnectDB cdb = null;

	private ReaderType readerType = null;

	public ReaderTypeDAL() {
		// TODO Auto-generated constructor stub
	}

	public ReaderTypeDAL(ConnectDB cdb) {
		this.cdb = cdb;
	}

	public void setReaderType(ReaderType readerType) {
		this.readerType = readerType;
	}

	@Override
	public boolean Insert() {
		// TODO Auto-generated method stub
		String[] params = { readerType.getRdType(), readerType.getRdTypeName(), readerType.getCanLendQty(),
				readerType.getCanLendDay(), readerType.getCanContinueTimes(), readerType.getPunishRate(),
				readerType.getDateVaild() };
		return cdb.InsertTable("INSERT Library.dbo.TB_ReaderType VALUES(?,?,?,?,?,?,?)", null, 0, params);
	}

	@Override
	public boolean Delete() {
		// TODO Auto-generated method stub
		return cdb.DeleteTable("DELETE Library.dbo.TB_ReaderType WHERE rdType = '" + readerType.getRdType() + "'");
	}

	@Override
	public boolean Update() {
		// TODO Auto-generated method stub
		String[] params = { readerType.getRdType(), readerType.getRdTypeName(), readerType.getCanLendQty(),
				readerType.getCanLendDay(), readerType.getCanContinueTimes(), readerType.getPunishRate(),
				readerType.getDateVaild() };
		return cdb.UpdateTable(
				"UPDATE Library.dbo.TB_ReaderType SET rdType = ?, rdTypeName =  ? , CanLendQty = ?  , CanLendDay =   ?  , CanContinueTimes =  ?  , PunishRate =  ?, DateVaild =  ?   WHERE rdType =  '"
						+ readerType.getRdType() + "'",
				"", null, params);
	}

	@Override
	public boolean Search(String t_sql) {
		// TODO Auto-generated method stub
		return cdb.GetTable(t_sql);
	}

}
