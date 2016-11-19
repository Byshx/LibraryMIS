package edu.yangtzeu.lmis.dal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

public class Test {

	private static String url = "jdbc:sqlserver://localhost:1433;databaseName=中国图书馆分类法;user=sa;password=luochong";

	public static void main(String[] args) {
		String aim = "[B 哲学、宗教]";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection connection = DriverManager.getConnection(url);
			PreparedStatement statement = null;
			File file = new File("F:/桌面文件/中图法第四版B/B哲学宗教.txt");
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String[] out = new String[6];
			String line = "";
			int before = 0;
			out[0] = bufferedReader.readLine().trim();
			while ((line = bufferedReader.readLine()) != null) {
				int count = 0;
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == ' ')
						count++;
					else
						break;
				}
				int index = count / 2 - 1;
				if (index <= before) {
					statement = connection
							.prepareStatement("INSERT INTO [中国图书馆分类法].[dbo]." + aim + " VALUES(?,?,?,?,?,?)");
					for (int i = 0; i < out.length; i++) {
						statement.setString(i + 1, out[i]);
					}
					statement.executeUpdate();
					out[index] = line.trim();
					for (int i = index + 1; i < out.length; i++) {
						out[i] = null;
					}
				} else {
					out[index] = line.trim();
				}
				before = index;
			}
			bufferedReader.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
