package edu.yangtzeu.lmis.dal;

import edu.yangtzeu.lmis.model.Borrow;

public class BorrowDAL extends AbstactDAL {

	/**
	 * 数据库连接引用
	 */
	private ConnectDB connectDB = null;

	private Borrow borrow = null;

	public BorrowDAL(ConnectDB connectDB) {
		// TODO Auto-generated constructor stub
		this.connectDB = connectDB;
	}

	public void setBorrow(Borrow borrow) {
		this.borrow = borrow;
	}

	@Override
	public boolean Insert() {
		// TODO Auto-generated method stub
		// BorrowID 为标识列
		String[] params = { borrow.getRdID(), borrow.getBkID(), borrow.getIdContinueTimes(), borrow.getIdDateOut(),
				borrow.getIdDateRetPlan(), borrow.getIdDateRetAct(), borrow.getIdOverDay(), borrow.getIdOverMoney(),
				borrow.getIdPunishMoney(), String.valueOf(borrow.getIsHasReturn()), borrow.getOperatorLend(),
				borrow.getOperatorRet() };
		return connectDB.InsertTable("INSERT INTO Library.dbo.TB_Borrow VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", null, 0,
				params);
	}

	@Override
	public boolean Delete() {
		// TODO Auto-generated method stub
		return connectDB.DeleteTable("DELETE Library.dbo.TB_Borrow WHERE BorrowID = '" + borrow.getBorrowID() + "'");
	}

	@Override
	public boolean Update() {
		// TODO Auto-generated method stub
		String[] params = { borrow.getRdID(), borrow.getBkID(), borrow.getIdContinueTimes(), borrow.getIdDateOut(),
				borrow.getIdDateRetPlan(), borrow.getIdDateRetAct(), borrow.getIdOverDay(), borrow.getIdOverMoney(),
				borrow.getIdPunishMoney(), String.valueOf(borrow.getIsHasReturn()), borrow.getOperatorLend(),
				borrow.getOperatorRet() };
		return connectDB.UpdateTable(
				"UPDATE Library.dbo.TB_Borrow SET rdID =  ? , bkID = ?  , IdContinueTimes =   ?  , IdDateOut =  ? , IdDateRetPlan = ? , IdDateRetAct =  ?, IdOverDay =  ?,IdOverMoney = ?,IdPunishMoney = ?, IsHasReturn = ?,OperatorLend = ?, OperatorRet = ?  WHERE BorrowID = '"
						+ borrow.getBorrowID() + "'",
				"", null, params);
	}

	@Override
	public boolean Search(String t_sql) {
		// TODO Auto-generated method stub
		return connectDB.GetTable(t_sql);
	}

}
