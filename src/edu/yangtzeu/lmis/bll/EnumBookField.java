package edu.yangtzeu.lmis.bll;

public enum EnumBookField {
	bkID("ID"), bkCode("�����"), bkName("����"), bkAuthor("����"), bkPress("������"), bkDatePress("��������"), bkISBN(
			"ISBN"), bkCatalog("�����"), bkLanguage("����"), bkPages("ҳ��"), bkPrice("�۸�"), bkDateIn("�������"), bkStatus("״̬");
	private String field;

	private EnumBookField(String field) {
		this.field = field;
	}

	public boolean Cmp(String field) {
		if (field.equals(this.field))
			return true;
		return false;
	}
}
