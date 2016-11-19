package edu.yangtzeu.lmis.bll;

public enum EnumBookField {
	bkID("ID"), bkCode("索书号"), bkName("书名"), bkAuthor("作者"), bkPress("出版社"), bkDatePress("出版日期"), bkISBN(
			"ISBN"), bkCatalog("分类号"), bkLanguage("语言"), bkPages("页数"), bkPrice("价格"), bkDateIn("入馆日期"), bkStatus("状态");
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
