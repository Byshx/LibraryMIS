package edu.yangtzeu.lmis.bll;

import java.util.Stack;

import edu.yangtzeu.lmis.gui.Login;
import edu.yangtzeu.lmis.gui.UI_Factory;

public class ReturnStack {
	// 创建单例
	private static ReturnStack singleton = null;

	private Stack<Node> stack = new Stack<>();

	public void addReturn(UI_Factory UI, String title, int ops1, int ops2) {
		stack.push(new Node(UI, title, ops1, ops2));
		// 返回数组中的值大于5，需要清除数组头的界面信息
		if (stack.size() > 5) {
			stack.remove(0);
		}

	}

	/**
	 * 获得上一个界面信息
	 * 
	 * @return Node
	 */
	public Node getReturn() {
		return stack.isEmpty() ? null : stack.pop();
	}

	public static ReturnStack initInstance() {
		singleton = new ReturnStack();
		return singleton;
	}

	/**
	 * 获取单例
	 * 
	 * @return
	 */
	public static ReturnStack getReturnInstance() {
		return singleton;
	}

	class Node {
		/*
		 * 上个界面UI枚举
		 */
		private UI_Factory UI;

		private String title;

		private int ops1 = 0;

		private int ops2 = 0;

		public Node(UI_Factory UI, String title, int ops1, int ops2) {
			// TODO Auto-generated constructor stub
			this.UI = UI;
			// 用于返回新书入库界面的设置变量
			this.ops1 = ops1;
			this.ops2 = ops2;
		}

		public void invokeReturn() {
			if (UI.equals(UI_Factory.BookManagement)) {
				IMP_BookManagement.setOps(ops1);
			}
			if (UI.equals(UI_Factory.BookIO)) {
				IMP_BookIO.setOps(ops2);
			}
			UI.showUI(Login.ShowPlatform, title);
		}
	}
}
