package edu.yangtzeu.lmis.dal;

public abstract class AbstactDAL {
	public abstract boolean Insert();
	public abstract boolean Delete();
	public abstract boolean Update();
	public abstract boolean Search(String t_sql);	
}
