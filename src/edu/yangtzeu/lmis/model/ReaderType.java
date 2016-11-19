package edu.yangtzeu.lmis.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;

public class ReaderType extends AbstractModel{

	// ReaderType¸÷×Ö¶ÎÃû³Æ

	private SimpleStringProperty rdType = null;

	private SimpleStringProperty rdTypeName = null;

	private SimpleStringProperty CanLendQty = null;

	private SimpleStringProperty CanLendDay = null;

	private SimpleStringProperty CanContinueTimes = null;

	private SimpleStringProperty PunishRate = null;

	private SimpleStringProperty DateVaild = null;

	public void setValue(ResultSet resultSet) {
		ResultSet result = resultSet;
		try {
			setRdType(result.getString(1));
			setRdTypeName(result.getString(2));
			setRdCanLendQty(result.getString(3));
			setRdCanLendDay(result.getString(4));
			setRdCanContinueTimes(result.getString(5));
			setRdPunishRate(result.getString(6));
			setRdDateVaild(result.getString(7));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Setter

	public void setRdType(String rdType) {
		this.rdType = new SimpleStringProperty(rdType);
	}

	public void setRdTypeName(String rdTypeName) {
		this.rdTypeName = new SimpleStringProperty(rdTypeName);
	}

	public void setRdCanLendQty(String CanLendQty) {
		this.CanLendQty = new SimpleStringProperty(CanLendQty);
	}

	public void setRdCanLendDay(String CanLendDay) {
		this.CanLendDay = new SimpleStringProperty(CanLendDay);
	}

	public void setRdCanContinueTimes(String CanContinueTimes) {
		this.CanContinueTimes = new SimpleStringProperty(CanContinueTimes);
	}

	public void setRdPunishRate(String PunishRate) {
		this.PunishRate = new SimpleStringProperty(PunishRate);
	}

	public void setRdDateVaild(String DateVaild) {
		this.DateVaild = new SimpleStringProperty(DateVaild);
	}

	// Getter

	public String getRdType() {
		return rdType.get();
	}

	public String getRdTypeName() {
		return rdTypeName.get();
	}

	public String getCanLendQty() {
		return CanLendQty.get();
	}

	public String getCanLendDay() {
		return CanLendDay.get();
	}

	public String getCanContinueTimes() {
		return CanContinueTimes.get();
	}

	public String getPunishRate() {
		return PunishRate.get();
	}

	public String getDateVaild() {
		return DateVaild.get();
	}

}
