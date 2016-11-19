package edu.yangtzeu.lmis.bll;

public enum EnumAdminRole {
	Reader("����", 0), LendCardManager("����֤����Ա", 1), BookManager("ͼ�����Ա", 2), LendManager("���Ĺ���Ա",
			4), SystemManager("ϵͳ����Ա", 8);

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
	 * �Ƚ�ö���Ƿ���ͬ������Ƚ�Type��number��-1��<br />
	 * ����Ƚ�number����TypeΪNULL
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
