package model;

import java.awt.FlowLayout;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Controller
{
	// database manager?
	String type;
	Node central;
	Node marin;
	Node palawan;
	final static int Port = 2345;
	
	public Node getCentral() {
		return central;
	}
	
	public Node getMarin() {
		return marin;
	}
	
	public Node getPalawan() {
		return palawan;
	}
	
	public Controller(String type)
	{
		this.type = type;
		palawan = new Node();
		marin = new Node();
		central = new Node();
		// instantiate database manager
	}
	
	public String getType()
	{
		return type;
	}
	
	public void add(String ip, String name)
	{
		if(name.equals("Marinduque")) {
			marin.setIpadd(ip);
			marin.setName(name);
		} else if(name.equals("Palawan")) {
			palawan.setIpadd(ip);
			palawan.setName(name);
		} else if(name.equals("Central")) {
			central.setIpadd(ip);
			central.setName(name);
		}
	}
	
	// Send POST notification
	//write message, process to whom to send the message
	public void SEND(String message)
	{
		System.out.println("SEND (start)");
		Socket s;
		
		/*try {
			s = new Socket(central.getIpadd(), Port);
			PrintWriter pw = new PrintWriter(s.getOutputStream());
			pw.println(message);
			pw.flush();
			s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		String sender = message.substring(message.indexOf('<') + 1, message.indexOf('>'));
		String command = message.substring(message.indexOf('(') + 1, message.indexOf(')'));
		
		if("Palawan".equals(sender)) {
			try{
				s = new Socket(central.getIpadd(), Port);
				PrintWriter pw = new PrintWriter(s.getOutputStream());
				pw.println(message);
				pw.flush();
				s.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else if("Central".equals(sender)) {
			try{
				s = new Socket(palawan.getIpadd(), Port);
				PrintWriter pw = new PrintWriter(s.getOutputStream());
				pw.println(message);
				pw.flush();
				s.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		/*try {
			SOCK = new Socket(central.getIpadd(), Port);					// Open socket
			PrintWriter OUT = new PrintWriter(SOCK.getOutputStream());
			OUT.println("\"READ\" " + message + "\0"); 					// Send message
			OUT.flush();		
			SOCK.close(); 												// Close socket	

		}
		catch (Exception e) {
			e.printStackTrace();
			if(type.equals("Marinduque")) {
				try {
					SOCK = new Socket(palawan.getIpadd(), Port);
					PrintWriter OUT = new PrintWriter(SOCK.getOutputStream());
					OUT.println("\"READ\" " + message + "\0"); 					// Send message
					OUT.flush();		
					SOCK.close(); 
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				try {
					SOCK = new Socket(marin.getIpadd(), Port);
					PrintWriter OUT = new PrintWriter(SOCK.getOutputStream());
					OUT.println("\"READ\" " + message + "\0"); 					// Send message
					OUT.flush();		
					SOCK.close(); 
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		} */
		System.out.println("SEND (end)");
	 }

	// Receive POST notification
		public void ReadingAction(String ip, byte [] bytes, String senderip)
		{
			System.out.println("ReadAction (start)");
			
			// Convert bytes to string
			String string = "";
			try {
				string = new String(bytes, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.out.println("ERROR READING ACTION");
			}
			
			// Get message (until NULL or EOF)
			String message = getUntilSpaceOrNull(string, 'e');
			
			
			// If character after message is null, display regular message
			System.out.println(ip + " " +  message);
			
			//you can process things here for message
			
			/*final CyclicBarrier cb = new CyclicBarrier(2);				
            Transaction t = new Transaction(cb, TransactionInterface.READ_UNCOMMITTED, TransactionInterface.COMMIT);
            t.addAction(new writeAction(t, 1, 1, 5));
            //t.addAction(new writeAction(t, 2, 1, 5));
            //t.addAction(new readAction(t, 2, 2));
			Thread thread1 = new Thread(t);
            Transaction t2 = new Transaction(cb, TransactionInterface.READ_UNCOMMITTED, TransactionInterface.COMMIT);
            t2.addAction(new writeAction(t, 2, 1, 5));
            Thread thread2 = new Thread(t2);*/
            
			System.out.println(ip + " " +  message);
			/*thread1.start();
			thread2.start();
			
			try {
				cb.await();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (BrokenBarrierException e1) {
				e1.printStackTrace();
			}*/
			
			System.out.println("ReadAction (end)");
		}
		

		// Returns string cut off at either space, null or eof, depending on c
		public String getUntilSpaceOrNull(String bytesinstring, char c){
			System.out.println("getUntilSpaceOrNull");
			
			int i = 0;											// Character index
			char [] bytesinchar = bytesinstring.toCharArray();	// String converted to char array
			 
			// Space or null
			if(c == 'b')
				while(bytesinchar[i] != ' ' && bytesinchar[i] != '\0')
					i++;
			  
			// Null
			else if(c == 'n')
				while(bytesinchar[i] != '\0')
					i++;
			  
			// Space
			else if(c == 's')
				while(bytesinchar[i] != ' ')
					i++;
			  
			// Null or EOF
			else if(c == 'e')
				while(bytesinchar[i] != '\0' && bytesinchar[i] != '\u001a')
					i++;
			  
			// Return cut up string
			return bytesinstring.substring(0, i);
		}
}
