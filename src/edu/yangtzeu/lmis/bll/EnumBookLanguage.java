package edu.yangtzeu.lmis.bll;

public enum EnumBookLanguage {

	Chinese("中文", 0), TraditionalChinese("中文繁体", 1), English("英文", 2), Japanese("日语", 3), Korean("韩语", 4), German("德语",
			5), French("法语", 6), Russian("俄语", 7), Italian("意大利语",
					8), Spanish("西班牙语", 9), Arabic("阿拉伯语", 10), Latin("拉丁语", 11), Portuguese("葡萄牙语", 12);

	private String languageName = "";

	private int sign = -1;

	private EnumBookLanguage(String languageName, int sign) {
		this.languageName = languageName;
		this.sign = sign;
	}

	public String getLanguageName() {
		return languageName;
	}

	public int getLanguageSign() {
		return sign;
	}

	/**
	 * 比较字段<br/>
	 * 比较名称时,Sign应为-1<br/>
	 * 比较标号时,name应为null
	 * 
	 * @param name
	 * @param Sign
	 * @return
	 */
	public boolean Cmp(String name, int Sign) {
		if (languageName.equals(name) || (Sign != -1 && Sign == sign))
			return true;
		return false;
	}

}
