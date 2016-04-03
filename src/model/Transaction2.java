package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnection;

public class Transaction2 implements Transaction{
	
	private int isolation_level;
	private DBConnection db;
	private Connection conn;
	private BufferedWriter bufferedWriter;
	
	public Transaction2() {
		db = new DBConnection();
		conn = db.getConnection();
	}

	@Override
	public void setIsolationLevel(int iso_level) {
		// TODO Auto-generated method stub
		System.out.println("Setting isolation level for transaction 2 to: " + iso_level);
		
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
		
		System.out.println("Transaction 2 begins");
		try {
			bufferedWriter = new BufferedWriter(new FileWriter("log.txt", true));
			bufferedWriter.write("<T2,Central,tbl,status=START>");
			bufferedWriter.newLine();
			bufferedWriter.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		String queryLock = "LOCK TABLES db_hpq_marinduque.hpq_mem WRITE;";
		
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
		}
		
	}

	@Override
	public ResultSet transactionBody(int number, int id, int value, boolean toLog) {
		// TODO Auto-generated method stub
		System.out.println("Transaction Body 2");
		
		return updateValueInTable(number, id, value, toLog);
		//insertIntoTable(number, 678);
	}

	@Override
	public void endTransaction(int action) {
		// TODO Auto-generated method stub
		System.out.println("End of transaction 2");
		
		try {
			
			String query = "?;";
			
			PreparedStatement ps = conn.prepareStatement(query);
			
			switch(action) {
				case COMMIT: //ps.setString(1, "COMMIT");
						conn.commit();
						try {
							bufferedWriter = new BufferedWriter(new FileWriter("log.txt", true));
							bufferedWriter.write("<T2,Central,tbl,status=COMMIT>");
							bufferedWriter.newLine();
							bufferedWriter.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					break;
				case ROLLBACK: //ps.setString(1, "ROLLBACK");
						conn.rollback();
					break;
				default: //ps.setString(1, "ROLLBACK");
						conn.rollback();
					break;
			}
			
			String unlockQuery = "UNLOCK TABLES;";
			PreparedStatement ps2 = conn.prepareStatement(unlockQuery);
			ps2.execute();
			
			//ps.executeQuery();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet updateValueInTable(int number, int id, int value, boolean toLog) {
		try {
			
			/*String query = "SELECT id, memno, age_yr FROM hpq_mem WHERE id = ?;";
			
			PreparedStatement ps1 = conn.prepareStatement(query);
			ps1.setInt(1, id);
			
			ResultSet rs = ps1.executeQuery();
			
			String oldValue = null;
			while(rs.next()) {
				System.out.println(number + " id: " + rs.getInt(1) + ", memno: " + rs.getInt(2) + ", age_yr: " + rs.getInt(3));
				oldValue = String.valueOf(rs.getInt(3));
			}*/
			
			String query2 = "UPDATE hpq_mem SET age_yr = ? WHERE memno = ?;";
			
			PreparedStatement ps = conn.prepareStatement(query2);
			ps.setInt(1, value);
			ps.setInt(2, id);
			
			
			//Uncomment when going to execute for real
			ps.executeUpdate();
			System.out.println("Updating...");
			System.out.println("Updated " + value + " to id: " + id);
			
			/*if(toLog) {
				try {
					bufferedWriter = new BufferedWriter(new FileWriter("log.txt", true));
					bufferedWriter.write("<T2,Central,tbl,col,id=" + id + ",oldValue=" + oldValue + ",newValue=" + value + ">");
					bufferedWriter.newLine();
					bufferedWriter.close();
					System.out.println("Writing...");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return queryTable(number);
	}

	/*public void insertIntoTable(int number, int value) {
		try {
			
			String query = "INSERT INTO tbl(col) VALUES (?)";
			
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, value);
			
			ps.executeUpdate();
			
			try {
				bufferedWriter.write("<T2, Central, tbl, col = " + value + ">");
				bufferedWriter.newLine();
				bufferedWriter.close();
				System.out.println("Writing...");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Inserting...");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
public ResultSet queryTable(int number) {
		
		try {
			
			String query = "SELECT id, memno, age_yr FROM hpq_mem WHERE id = 199036;";
			
			PreparedStatement ps = conn.prepareStatement(query);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				System.out.println(number + " id: " + rs.getInt(1) + ", memno: " + rs.getInt(2) + ", age_yr: " + rs.getInt(3));
			}
			
			return rs;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
