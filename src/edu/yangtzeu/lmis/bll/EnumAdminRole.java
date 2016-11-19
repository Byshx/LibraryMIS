package edu.yangtzeu.lmis.bll;

public enum EnumAdminRole {
	Reader("读者", 0), LendCardManager("借书证管理员", 1), BookManager("图书管理员", 2), LendManager("借阅管理员",
			4), SystemManager("系统管理员", 8);

	private String Type = null;

	private int number = 0;

	private EnumAdminRole(String Type, int number) {
		this.Type = Type;
		this.number = number;
	}

	public String getTypeName() {
		return Type;
	}

	public int getTypeNumber() {
		return number;
	}

	/**
	 * 比较枚举是否相同，如果比较Type则number置-1；<br />
	 * 如果比较number，则Type为NULL
	 * 
	 * @param Type
	 * @param number
	 * @return boolean
	 */

	public boolean Cmp(String Type, int number) {
		if (Type != null && (Type.contains(this.Type)) || (this.number & number) == this.number)
			return true;
		return false;
	}
}
