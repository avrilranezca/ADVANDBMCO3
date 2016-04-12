package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnection;
import db.DBConnectionMSSQL;

public class Transaction1 implements Transaction{
	
	private int isolation_level;
	
	private Connection conn;
	private DBConnectionMSSQL db;
	private BufferedWriter bufferedWriter;
	
	public Transaction1() {
		db = new DBConnectionMSSQL();
		conn = db.getConnection();
		try {
			bufferedWriter = new BufferedWriter(new FileWriter("log.txt", true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void setIsolationLevel(int iso_level) {
		// TODO Auto-generated method stub
		System.out.println("Setting isolation level for transaction 1 to: " + iso_level);
		
		isolation_level = iso_level;
		
		try {
			
			String query = "SET SESSION TRANSACTION ISOLATION LEVEL ?;";
			
			PreparedStatement ps = conn.prepareStatement(query);
			
			switch(isolation_level) {
			case ISO_READ_UNCOMMITTED: //ps.setString(1, "READ UNCOMMITTED"); 
					conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				break;
			case ISO_READ_COMMITTED: //ps.setString(1, "READ COMMITTED");
					conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				break;
			case ISO_REPEATABLE_READ: //ps.setString(1, "REPEATABLE READ");
					conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			 	break;
			case ISO_SERIALIZABLE: //ps.setString(1, "SERIALIZABLE");
					conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				break;
			default: //ps.setString(1, "SERIALIZABLE");
					conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				break;
			}
			
			//ps.executeQuery();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public int getIsolationLevel() {
		return isolation_level;
	}
	
	@Override
	public void beginTransaction() {
		// TODO Auto-generated method stub
		System.out.println("Transaction 1 begins");
		
		/*String queryLock = "LOCK TABLES hpq_mem READ;";
		
		PreparedStatement ps;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(queryLock);
			ps.execute();
			ps = conn.prepareStatement("START TRANSACTION;");
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		System.out.println("After obtaining readLock");
		
	}

	@Override
	public ResultSet transactionBody(String type, int id, int value, int householdNum) {
		// TODO Auto-generated method stub
		
		System.out.println("Transaction Body 1");
		
		return queryTable(type);
		
	}

	@Override
	public void endTransaction(int action) {
		// TODO Auto-generated method stub
		
		/*String unlockQuery = "UNLOCK TABLES;";
		PreparedStatement ps2;
		try {
			ps2 = conn.prepareStatement(unlockQuery);
			ps2.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		System.out.println("End of transaction 1");
		
	}
	
	public ResultSet queryTable(String type) {
		
		try {
			
			String query = "SELECT id, memno, age_yr FROM hpq_mem WHERE id =";
			
			if("Palawan".equals(type)) {
				query += "16818;" ;
			}
			else if("Marinduque".equals(type)) {
				query += "199036;";
			}
			else if("Central".equals(type)) {
				query += "16818 OR id = 199036;";
			}
			
			PreparedStatement ps = conn.prepareStatement(query);
			
			ResultSet rs = ps.executeQuery();
			
			/*while(rs.next()) {
				System.out.println(number + " id: " + rs.getInt(1) + ", memno: " + rs.getInt(2) + ", age_yr: " + rs.getInt(3));
			}
			
			rs.first();*/
			
			return rs;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	

}
