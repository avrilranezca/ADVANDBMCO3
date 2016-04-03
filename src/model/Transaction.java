package model;

import java.sql.ResultSet;

public interface Transaction {
	
	public static int ISO_READ_UNCOMMITTED = 1;
	public static int ISO_READ_COMMITTED = 2;
	public static int ISO_REPEATABLE_READ = 3;
	public static int ISO_SERIALIZABLE = 4;
	
	public static int COMMIT = 10;
	public static int ROLLBACK = 20;

	
	public void setIsolationLevel(int iso_level);
	
	public int getIsolationLevel();
	
	public void beginTransaction();
	
	public ResultSet transactionBody(int number, int id, int value, boolean toLog);
	
	public void endTransaction(int action);
	

}
