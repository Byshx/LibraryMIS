package edu.yangtzeu.lmis.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Borrow extends AbstractModel {

	// 借阅各字段信息
	/**
	 * 借书顺序号,Numeric(12,0) 数据库自动生成
	 */
	private SimpleStringProperty BorrowID = null;

	private SimpleStringProperty rdID = null;

	private SimpleStringProperty bkID = null;

	// Table需要，附加
	private SimpleStringProperty bkName = null;
	// Table需要，附加
	private SimpleStringProperty bkAuthor = null;

	private SimpleStringProperty IdContinueTimes = null;

	private SimpleStringProperty IdDateOut = null;

	private SimpleStringProperty IdDateRetPlan = null;

	private SimpleStringProperty IdDateRetAct = null;

	private SimpleStringProperty IdOverDay = null;

	private SimpleStringProperty IdOverMoney = null;

	private SimpleStringProperty IdPunishMoney = null;

	private SimpleBooleanProperty IsHasReturn = null;

	private SimpleStringProperty OperatorLend = null;

	private SimpleStringProperty OperatorRet = null;

	// Setter

	public void setBorrowID(String BorrowID) {
		this.BorrowID = new SimpleStringProperty(BorrowID);
	}

	public void setRdID(String rdID) {
		this.rdID = new SimpleStringProperty(rdID);
	}

	public void setBkID(String bkID) {
		this.bkID = new SimpleStringProperty(bkID);
	}

	public void setBkName(String bkName) {
		this.bkName = new SimpleStringProperty(bkName);
	}

	public void setBkAuthor(String bkAuthor) {
		this.bkAuthor = new SimpleStringProperty(bkAuthor);
	}

	public void setIdContinueTimes(String IdContinueTimes) {
		this.IdContinueTimes = new SimpleStringProperty(IdContinueTimes);
	}

	public void setIdDateOut(String IdDateOut) {
		this.IdDateOut = new SimpleStringProperty(IdDateOut);
	}

	public void setIdDateRetPlan(String IdDateRetPlan) {
		this.IdDateRetPlan = new SimpleStringProperty(IdDateRetPlan);
	}

	public void setIdDateRetAct(String IdDateRetAct) {
		this.IdDateRetAct = new SimpleStringProperty(IdDateRetAct);
	}

	public void setIdOverDay(String IdOverDay) {
		this.IdOverDay = new SimpleStringProperty(IdOverDay);
	}

	public void setIdOverMoney(String IdOverMoney) {
		this.IdOverMoney = new SimpleStringProperty(IdOverMoney);
	}

	public void setIdPunishMoney(String IdPunishMoney) {
		this.IdPunishMoney = new SimpleStringProperty(IdPunishMoney);
	}

	public void setIsHasReturn(boolean IsHasReturn) {
		this.IsHasReturn = new SimpleBooleanProperty(IsHasReturn);
	}

	public void setOperatorLend(String OperatorLend) {
		this.OperatorLend = new SimpleStringProperty(OperatorLend);
	}

	public void setOperatorRet(String OperatorRes) {
		this.OperatorRet = new SimpleStringProperty(OperatorRes);
	}

	// Getter

	public String getBorrowID() {
		return BorrowID.get();
	}

	public String getRdID() {
		return rdID.get();
	}

	public String getBkID() {
		return bkID.get();
	}

	public String getBkName() {
		return bkName.get();
	}

	public String getBkAuthor() {
		return bkAuthor.get();
	}

	public String getIdContinueTimes() {
		return IdContinueTimes.get();
	}

	public String getIdDateOut() {
		return IdDateOut.get();
	}

	public String getIdDateRetPlan() {
		return IdDateRetPlan.get();
	}

	public String getIdDateRetAct() {
		return IdDateRetAct.get();
	}

	public String getIdOverDay() {
		return IdOverDay.get();
	}

	public String getIdOverMoney() {
		return IdOverMoney.get();
	}

	public String getIdPunishMoney() {
		return IdPunishMoney.get();
	}

	public boolean getIsHasReturn() {
		return IsHasReturn.get();
	}

	public String getOperatorLend() {
		return OperatorLend.get();
	}

	public String getOperatorRet() {
		return OperatorRet.get();
	}

	@Override
	public void setValue(ResultSet resultSet) {
		// TODO Auto-generated method stub
		ResultSet result = resultSet;
		try {
			setBorrowID(result.getString(1));
			setRdID(result.getString(2));
			setBkID(result.getString(3));
			setIdContinueTimes(result.getString(4));
			setIdDateOut(result.getString(5));
			setIdDateRetPlan(resultSet.getString(6));
			setIdDateRetAct(result.getString(7));
			setIdOverDay(result.getString(8));
			setIdOverMoney(result.getString(9));
			setIdPunishMoney(result.getString(10));
			setIsHasReturn(result.getBoolean(11));
			setOperatorLend(result.getString(12));
			setOperatorRet(result.getString(13));
			if (result.getString("bkName") != null) {
				setBkName(result.getString("bkName"));
			}
			if (result.getString("bkAuthor") != null) {
				setBkAuthor(result.getString("bkAuthor"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
