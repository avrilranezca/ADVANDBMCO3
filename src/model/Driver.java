package model;

import java.util.Scanner;

public class Driver
{
	final static int PORT = 1234;
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
		
		con.add("192.168.1.33", "Central");
		con.add("192.168.1.144", "Marinduque");
		//MainGUI mainGUI = new MainGUI(con);
		
		Scanner sc = new Scanner(System.in);
		String message;
		while((message = sc.nextLine()) != null ) {
			con.readGlobal();
			//con.sendMessage(message);
		}
		 
	}
}
