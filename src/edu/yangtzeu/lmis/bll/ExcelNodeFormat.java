package edu.yangtzeu.lmis.bll;

import java.util.List;
/**
 * 此接口可使类属性按顺序绑定<br/>
 * 实现此接口的实体,可传递到GenerateExcel<br/>
 * 实例获得生成Excel文件功能
 * @author 罗翀
 *
 */
public interface ExcelNodeFormat {
	public List<String> bindNode();
}
