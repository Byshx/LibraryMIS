package edu.yangtzeu.lmis.dal;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectDB implements Runnable {

	private final String UserName = "sa";
	private final String Password = "luochong";
	/**
	 * sa身份连接 localhost:127.0.0.1
	 */
	private String url = "jdbc:sqlserver://localhost:1433;databaseName=Library;user=" + UserName + ";password="
			+ Password;

	/**
	 * Declare the JDBC objects.
	 */
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private static ConnectDB singleton = null;
	private static boolean Status = false;

	/**
	 * initialize the connection
	 */
	public static ConnectDB initInstance() {
		singleton = new ConnectDB();
		new Thread(singleton).start();
		;
		return singleton;
	}

	private void initConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(url);
			// 连接成功
			System.out.println("连接成功");
			Status = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("连接超时");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Singleton Pattern
	 * 
	 * @return
	 */
	public static ConnectDB getDBConnection() {
		return singleton;
	}

	/**
	 * Use T-SQL language to operate DateBase and get data
	 * 
	 * @param t_sql
	 * @return boolean
	 */
	public boolean GetTable(String t_sql) {
		try {
			preparedStatement = connection.prepareStatement(t_sql);
			resultSet = preparedStatement.executeQuery();
			// 获取成功
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("获取数据失败");
			return false;
		}
	}

	public boolean UpdateTable(String t_sql, String image_sql, InputStream image, String[] param) {
		try {
			preparedStatement = connection.prepareStatement(t_sql);
			for (int i = 0; i < param.length; i++) {
				preparedStatement.setString(i + 1, param[i]);
			}
			preparedStatement.executeUpdate();
			if (image != null && !image.getClass().getName().equals("com.microsoft.sqlserver.jdbc.SimpleInputStream")) {
				preparedStatement = connection.prepareStatement(image_sql);
				preparedStatement.setBinaryStream(1, image);
				preparedStatement.executeUpdate();
			}			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("更新数据库失败");
			return false;
		}
	}

	public boolean InsertTable(String t_sql, InputStream image, int imageColumn, String[] param) {
		try {
			preparedStatement = connection.prepareStatement(t_sql);
			for (int i = 0; i < param.length; i++) {
				preparedStatement.setString(i + 1, param[i]);
			}
			if (image != null) {
				preparedStatement.setBinaryStream(imageColumn, image);
			}
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("插入数据库失败");
			return false;
		}
	}

	public boolean DeleteTable(String t_sql) {
		try {
			preparedStatement = connection.prepareStatement(t_sql);
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("删除数据库失败");
			return false;
		}
	}

	/**
	 * Get data
	 * 
	 * @param index
	 * @return String
	 */
	public ResultSet getResult() {
		return resultSet;
	}

	public boolean getStatus() {
		return Status;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		initConnection();
	}
}
