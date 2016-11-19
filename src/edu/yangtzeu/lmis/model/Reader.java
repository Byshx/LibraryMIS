package edu.yangtzeu.lmis.model;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.yangtzeu.lmis.bll.EnumAdminRole;
import edu.yangtzeu.lmis.bll.ExcelNodeFormat;
import javafx.beans.property.SimpleStringProperty;

public class Reader extends AbstractModel implements ExcelNodeFormat {

	private SimpleStringProperty rdID = null;

	private SimpleStringProperty rdName = null;

	private SimpleStringProperty rdSex = null;

	/**
	 * Reader's Type <br/>
	 * 10 教师<br />
	 * 20 本科生 <br/>
	 * 21 专科生 <br/>
	 * 30 硕士生 <br />
	 * 31 博士
	 */
	private SimpleStringProperty rdType = null;

	private SimpleStringProperty rdDept = null;

	private SimpleStringProperty rdPhone = null;

	private SimpleStringProperty rdEmail = null;

	/**
	 * 注册日期
	 */
	private SimpleStringProperty rdDateReg = null;

	private InputStream rdPhoto = null;

	/**
	 * 有效 挂失 注销
	 */
	private SimpleStringProperty rdStatus = null;

	/**
	 * 已借阅数量
	 */
	private SimpleStringProperty rdBorrowQty = null;

	private SimpleStringProperty rdPwd = null;

	/**
	 * 0 是读者 1 is Borrow Card Management 2 is 图书管理员 4 is 借阅管理员 8 是系统管理员
	 */
	private SimpleStringProperty rdAdminRoles = null;
	// 用于存储该角色的名称(制表使用)
	private SimpleStringProperty rolesName = null;

	/**
	 * 设置Reader实例中的各字段信息
	 */
	public void setValue(ResultSet resultSet) {
		ResultSet result = resultSet;
		try {
			setRdID(result.getString(1));
			setRdName(result.getString(2));
			setRdSex(result.getString(3));
			setRdType(result.getString(4));
			setRdDept(result.getString(5));
			setRdPhone(result.getString(6));
			setRdEmail(result.getString(7));
			setRdDateReg(result.getString(8));
			setRdPhoto(result.getBinaryStream(9));
			setRdStatus(result.getString(10));
			setRdBorrowQty(result.getString(11));
			setRdPwd(result.getString(12));
			setRdAdminRoles(result.getString(13));
			EnumAdminRole[] enumAdminRoles = EnumAdminRole.values();
			String role = "";
			for (int i = 0; i < enumAdminRoles.length; i++) {
				if (enumAdminRoles[i].Cmp(null, Integer.parseInt(getRdAdminRoles()))) {
					role = role + " " + enumAdminRoles[i].getTypeName();
				}
			}
			setRolesName(role);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getRdID() {
		// TODO Auto-generated method stub
		return rdID.get();
	}

	public String getRdName() {
		// TODO Auto-generated method stub
		return rdName.get();
	}

	public String getRdSex() {
		// TODO Auto-generated method stub
		return rdSex.get();
	}

	public String getRdType() {
		// TODO Auto-generated method stub
		return rdType.get();
	}

	public String getRdDept() {
		// TODO Auto-generated method stub
		return rdDept.get();
	}

	public String getRdPhone() {
		// TODO Auto-generated method stub
		return rdPhone.get();
	}

	public String getRdEmail() {
		// TODO Auto-generated method stub
		return rdEmail.get();
	}

	public String getRdDateReg() {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (rdDateReg.get().equals("")) {
			rdDateReg.set(df.format(new Date()));
		}
		return rdDateReg.get();
	}

	public InputStream getRdPhoto() {
		// TODO Auto-generated method stub
		return rdPhoto;
	}

	public String getRdStatus() {
		// TODO Auto-generated method stub
		return rdStatus.get();
	}

	public String getRdBorrowQty() {
		// TODO Auto-generated method stub
		return rdBorrowQty.get();
	}

	public String getRdPwd() {
		// TODO Auto-generated method stub
		return rdPwd.get();
	}

	public String getRolesName() {
		return rolesName.get();
	}

	public String getRdAdminRoles() {
		// TODO Auto-generated method stub
		return rdAdminRoles.get();
	}

	public void setRdID(String rdID) {
		// TODO Auto-generated method stub
		this.rdID = new SimpleStringProperty(rdID);
	}

	public void setRdName(String rdName) {
		// TODO Auto-generated method stub
		this.rdName = new SimpleStringProperty(rdName);
	}

	public void setRdSex(String rdSex) {
		// TODO Auto-generated method stub
		this.rdSex = new SimpleStringProperty(rdSex);
	}

	public void setRdType(String rdType) {
		// TODO Auto-generated method stub
		this.rdType = new SimpleStringProperty(rdType);
	}

	public void setRdDept(String rdDept) {
		// TODO Auto-generated method stub
		this.rdDept = new SimpleStringProperty(rdDept);
	}

	public void setRdPhone(String rdPhone) {
		// TODO Auto-generated method stub
		this.rdPhone = new SimpleStringProperty(rdPhone);
	}

	public void setRdEmail(String rdEmail) {
		// TODO Auto-generated method stub
		this.rdEmail = new SimpleStringProperty(rdEmail);
	}

	public void setRdDateReg(String rdDateReg) {
		// TODO Auto-generated method stub
		this.rdDateReg = new SimpleStringProperty(rdDateReg);
	}

	public void setRdPhoto(InputStream rdPhoto) {
		// TODO Auto-generated method stub
		this.rdPhoto = rdPhoto;
	}

	public void setRdStatus(String rdStatus) {
		// TODO Auto-generated method stub
		this.rdStatus = new SimpleStringProperty(rdStatus);
	}

	public void setRdBorrowQty(String rdBorrowQty) {
		// TODO Auto-generated method stub
		this.rdBorrowQty = new SimpleStringProperty(rdBorrowQty);
	}

	public void setRdPwd(String rdPwd) {
		// TODO Auto-generated method stub
		this.rdPwd = new SimpleStringProperty(rdPwd);
	}

	public void setRdAdminRoles(String rdAdminRoles) {
		// TODO Auto-generated method stub
		this.rdAdminRoles = new SimpleStringProperty(rdAdminRoles);
	}

	public void setRolesName(String rolesName) {
		this.rolesName = new SimpleStringProperty(rolesName);
	}

	/**
	 * 此方法可将属性顺序化，可使<br/>
	 * GenerateExcel实例生成Excel文件
	 * 
	 * @return List
	 */
	@Override
	public List<String> bindNode() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
		list.add(getRdID());
		list.add(getRdName());
		list.add(getRdSex());
		list.add(getRdType());
		list.add(getRdDept());
		list.add(getRdPhone());
		list.add(getRdEmail());
		list.add(getRdDateReg());
		list.add(getRdStatus());
		list.add(getRdBorrowQty());
		list.add(getRdPwd());
		list.add(getRdAdminRoles());
		return list;
	}

}
