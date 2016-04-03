package model;

import java.awt.FlowLayout;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.rowset.CachedRowSetImpl;

public class Controller
{
	// database manager?
	String type;
	Node central;
	Node marin;
	Node palawan;
	final static int Port = 1234;
	
	private static volatile boolean READ_RESULT = false;
	private static volatile boolean IS_FROM_CENTRAL = false;
	private ResultSet moreData;
	
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
	
	public ResultSet getMoreData() {
		return moreData;
	}
	
	public void setMoreData(ResultSet moreData) {
		this.moreData = moreData;
	}
	
	public boolean getReadResultFlag() {
		return READ_RESULT;
	}
	
	public void setReadResultFlag(boolean f) {
		this.READ_RESULT = f;
	}
	
	public static boolean isIS_FROM_CENTRAL() {
		return IS_FROM_CENTRAL;
	}

	public static void setIS_FROM_CENTRAL(boolean iS_FROM_CENTRAL) {
		IS_FROM_CENTRAL = iS_FROM_CENTRAL;
	}

	public void readGlobal() {
		if(type.equals("Palawan")) {
			new Thread(new SEND(new Message("Palawan", "READ"))).start();
			System.out.println("After sending <Palawan>(READ)");
			
			while(!READ_RESULT){};
			
			System.out.println("After infinite loop");
			
			if(IS_FROM_CENTRAL) {
				System.out.println("I should get everything from here");
				
				try {
					while(moreData.next()) {
						System.out.println(moreData.getInt(1) + " " + moreData.getInt(2) + " " + moreData.getInt(3));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else {
				System.out.println("This data is only from Marinduque, I should also query my database");
				
				try {
					while(moreData.next()) {
						System.out.println(moreData.getInt(1) + " " + moreData.getInt(2) + " " + moreData.getInt(3));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println("After if else");
			
			READ_RESULT = false;
			IS_FROM_CENTRAL = false;
			
		}
		else if(type.equals("Marinduque")) {
			new Thread (new SEND(new Message("Marinduque", "READ"))).start();
			while(!READ_RESULT);

			if(IS_FROM_CENTRAL) {
				System.out.println("I should get everything from here");

				try {
					while(moreData.next()) {
						System.out.println(moreData.getInt(1) + " " + moreData.getInt(2) + " " + moreData.getInt(3));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				System.out.println("This data is only from Palawan, I should also query my database");

				try {
					while(moreData.next()) {
						System.out.println(moreData.getInt(1) + " " + moreData.getInt(2) + " " + moreData.getInt(3));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(type.equals("Central")) {
			// i already have all of the data
		}
	}
	
	public void sendMessage(Message message) {
		new Thread(new SEND(message)).start();
	}
	
	// Send POST notification
	//write message, process to whom to send the message
	public class SEND implements Runnable
	{
		private Message item;
		
		public SEND(Message item) {
			this.item = item;
			System.out.println("Received from readglobal message");
		}
		
		@Override
		public void run() {
			System.out.println("SEND (start)");
			Socket s;
			
			/*try {
				s = new Socket(marin.getIpadd(), Port);
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
			
			String sender = item.getSender();
			String command = item.getCommand();
			
			if("READ".equals(command)) {
				if("Palawan".equals(sender)) {
					try{
						s = new Socket(central.getIpadd(), Port);
						s.setSoTimeout(2000);
						ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
						pw.writeObject(item);
						pw.flush();
						s.close();
					}
					catch(Exception e) {
						e.printStackTrace();
						System.out.println("Read request from palawan to central failed");
						
						try {
							s = new Socket(marin.getIpadd(), Port);
							s.setSoTimeout(2000);
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.writeObject(item);
							pw.flush();
							s.close();
							System.out.println("I sent to marinduque");
						} catch(Exception e1) {
							e1.printStackTrace();
							System.out.println("Read request from palawan to marinduque failed");
						}
					}
				}
				else if ("Marinduque".equals(sender)) {
					try{
						s = new Socket(central.getIpadd(), Port);
						s.setSoTimeout(2000);
						ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
						pw.writeObject(item);
						pw.flush();
						s.close();
					}
					catch(Exception e) {
						e.printStackTrace();
						System.out.println("Read request from marinduque to central failed");
						
						try {
							s = new Socket(palawan.getIpadd(), Port);
							s.setSoTimeout(2000);
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.writeObject(item);
							pw.flush();
							s.close();
						} catch(Exception e1) {
							e1.printStackTrace();
							System.out.println("Read request from marinduque to palawan failed");
						}
					}
				}
			}
			else if("READRESPONSE".equals(command)) {
				String originalSender = item.getOriginalSender();
				if("Palawan".equals(sender)) {
					if("Marinduque".equals(originalSender)) {
						try {
							s = new Socket(marin.getIpadd(), Port);
							s.setSoTimeout(2000);
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.writeObject(item);
							pw.flush();
							s.close();
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
					else if("Central".equals(originalSender)) {
						System.out.println("I doubt central will be the original sender of a read request");
					}
				}
				else if("Central".equals(sender)) {
					if("Palawan".equals(originalSender)) {
						try {
							s = new Socket(palawan.getIpadd(), Port);
							s.setSoTimeout(2000);
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.writeObject(item);
							pw.flush();
							s.close();
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
					else if("Marinduque".equals(originalSender)) {
						try {
							s = new Socket(marin.getIpadd(), Port);
							s.setSoTimeout(2000);
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.writeObject(item);
							pw.flush();
							s.close();
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
					
				}
				else if("Marinduque".equals(sender)) {
					if("Palawan".equals(originalSender)) {
						try {
							s = new Socket(palawan.getIpadd(), Port);
							s.setSoTimeout(2000);
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.writeObject(item);
							pw.flush();
							s.close();
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
					else if("Central".equals(originalSender)) {
						System.out.println("I doubt central will be the original sender of a read request");
					}
				}
			}
			
			
			/*if("Palawan".equals(sender)) {
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
			}*/
			
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
		
		
	 }
	
		public void readResponseAction(Message message) {
			
			String sender = message.getSender();
			String command = message.getCommand();
			CachedRowSetImpl data = message.getData();
			
			System.out.println("In readresponseaction");
			
			if(type.equals("Palawan")) {
				if(sender.equals("Marinduque")) {
					
				}
				else if(sender.equals("Central")) {
					IS_FROM_CENTRAL = true;
				}
				moreData = data;
				READ_RESULT = true;
			}
			else if(type.equals("Central")) {
				
			}
			else if(type.equals("Marinduque")) {
				if(sender.equals("Palawan")) {
					
				}
				
				else if(sender.equals("Central")) {
					IS_FROM_CENTRAL = true;
				}
				moreData = data;
				READ_RESULT = true;
			}
			
		}

	// Receive POST notification
		/*public void ReadingAction(String ip, byte [] bytes, String senderip)
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
            Thread thread2 = new Thread(t2);
            
			System.out.println(ip + " " +  message);
			/*thread1.start();
			thread2.start();
			
			try {
				cb.await();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (BrokenBarrierException e1) {
				e1.printStackTrace();
			}
			
			System.out.println("ReadAction (end)");
		}*/
		

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
