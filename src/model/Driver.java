package model;

import java.util.Scanner;

public class Driver
{
	final static int PORT = 9876;
	/*public static void main(String[] args)
	{
		MainGUI mainGUI = new MainGUI(new Controller());
	}*/
	
	public static void main(String[] args){
		
		// Starting the Server

		Controller con = new Controller("Palawan");
		Server SER = new Server(con, PORT);
		Thread X = new Thread(SER);
		X.start();	// Runs the server process
		
		con.add("10.100.215.7", "Central");
		con.add("192.168.43.201", "Marinduque");
		//MainGUI mainGUI = new MainGUI(con);
		
		Display D = new Display(con);
		con.setDisplay(D);
		
		Scanner sc = new Scanner(System.in);
		String message;
		while((message = sc.nextLine()) != null ) {
			//con.readGlobal();
			//con.sendMessage(message);
			if(message.equals("Marinduque")) {
				con.writeGlobal(Transaction.ISO_SERIALIZABLE, "Marinduque", 1, 2);
			}
			else if(message.equals("Palawan")) {
				con.writeGlobal(Transaction.ISO_SERIALIZABLE, "Palawan", 1, 2);
			}
			else if(message.equals("Read")) {
				con.readGlobal();
			}
		}
		
		
	}
}
