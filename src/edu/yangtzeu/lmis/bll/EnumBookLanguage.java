package edu.yangtzeu.lmis.bll;

public enum EnumBookLanguage {

	Chinese("����", 0), TraditionalChinese("���ķ���", 1), English("Ӣ��", 2), Japanese("����", 3), Korean("����", 4), German("����",
			5), French("����", 6), Russian("����", 7), Italian("�������",
					8), Spanish("��������", 9), Arabic("��������", 10), Latin("������", 11), Portuguese("��������", 12);

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
	 * �Ƚ��ֶ�<br/>
	 * �Ƚ�����ʱ,SignӦΪ-1<br/>
	 * �Ƚϱ��ʱ,nameӦΪnull
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
