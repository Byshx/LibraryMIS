package edu.yangtzeu.lmis.bll;

import java.util.Stack;

import edu.yangtzeu.lmis.gui.Login;
import edu.yangtzeu.lmis.gui.UI_Factory;

public class ReturnStack {
	// ��������
	private static ReturnStack singleton = null;

	private Stack<Node> stack = new Stack<>();

	public void addReturn(UI_Factory UI, String title, int ops1, int ops2) {
		stack.push(new Node(UI, title, ops1, ops2));
		// ���������е�ֵ����5����Ҫ�������ͷ�Ľ�����Ϣ
		if (stack.size() > 5) {
			stack.remove(0);
		}

	}

	/**
	 * �����һ��������Ϣ
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
	 * ��ȡ����
	 * 
	 * @return
	 */
	public static ReturnStack getReturnInstance() {
		return singleton;
	}

	class Node {
		/*
		 * �ϸ�����UIö��
		 */
		private UI_Factory UI;

		private String title;

		private int ops1 = 0;

		private int ops2 = 0;

		public Node(UI_Factory UI, String title, int ops1, int ops2) {
			// TODO Auto-generated constructor stub
			this.UI = UI;
			// ���ڷ�����������������ñ���
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
