package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionMSSQL {
	private static String url = "jdbc:sqlserver://localhost:1433;DatabaseName=hpq_palawan;IntegratedSecurity=true";
	private static String username = "root";
	private static String password = "crAter16";
	private static String schema = "db_hpq_palawan";
	private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	public DBConnectionMSSQL(){
		System.out.println("I am using mssql");
	}
	
	public Connection getConnection(){
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
}
