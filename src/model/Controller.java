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
	final static int Port = 9876;
	
	private static volatile boolean READ_RESULT = false;
	private static volatile boolean WRITE_RESULT = false;
	private static volatile boolean IS_FROM_CENTRAL = false;
	private static volatile boolean NEIGHBOR_WRITE_SUCCESSFUL = false;
	private ResultSet moreData;
	private Display d;
	private ResultSet tempRS1;
	private ResultSet tempRS2;
	
	public Node getCentral() {
		return central;
	}
	
	public Node getMarin() {
		return marin;
	}
	
	
	public Node getPalawan() {
		return palawan;
	}
	
	public void setDisplay(Display dis){
		d = dis;
	}
	
	public String getOtherType() {
		return otherType();
	}
	
	public String otherType(){
		if (type.equals("Palawan"))
			return "Marinduque";
		if (type.equals("Marinduque"))
			return "Palawan";
		else return "Central";
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
				
				printData(moreData);
				
			}
			else {
				System.out.println("This data is only from Marinduque, I should also query my database");
				
				printData(moreData);
				
				Transaction1 transaction = new Transaction1();
				transaction.setIsolationLevel(Transaction.ISO_SERIALIZABLE);
        		transaction.beginTransaction();
        		ResultSet rs = transaction.transactionBody(type, 0, 0, 0);
        		transaction.endTransaction(Transaction.COMMIT);
        		
        		
        		printData(rs);
        		//take moreData and rs
				
				
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

				printData(moreData);
				
				//take all from moreData
			}
			else {
				System.out.println("This data is only from Palawan, I should also query my database");

				printData(moreData);
				
				Transaction1 transaction = new Transaction1();
				transaction.setIsolationLevel(Transaction.ISO_SERIALIZABLE);
        		transaction.beginTransaction();
        		ResultSet rs = transaction.transactionBody(type, 0, 0, 0);
        		transaction.endTransaction(Transaction.COMMIT);
        		
        		printData(rs);
        		
        		//take data from rs and moreData
			}
			
			READ_RESULT = false;
			IS_FROM_CENTRAL = false;
		
			
		}
		else if(type.equals("Central")) {
			// i already have all of the data
			
			Transaction1 transaction = new Transaction1();
			transaction.setIsolationLevel(Transaction.ISO_SERIALIZABLE);
    		transaction.beginTransaction();
    		ResultSet rs = transaction.transactionBody(type, 0, 0, 0);
    		transaction.endTransaction(Transaction.COMMIT);
    		
    		printData(rs);
    		
    		//take from rs
    		READ_RESULT = false;
			IS_FROM_CENTRAL = false;
		
		}
	}
	
	public synchronized void writeGlobal(int iso_level, String targetLocation, int id, int value) {
		
		if(targetLocation.equals("Palawan")) {
			
			if(/*type.equals("Palawan") || */type.equals("Central")) {
				Transaction2 transaction = new Transaction2();
				transaction.setIsolationLevel(iso_level);
				transaction.beginTransaction();
				ResultSet rs = transaction.transactionBody(type, id, value, 16818);
				
				new Thread(new SEND(new Message(targetLocation, "UPDATE", type, "id=" + id + ",value=" + value))).start();
				
				while(!WRITE_RESULT);
				
				if(NEIGHBOR_WRITE_SUCCESSFUL) {
					transaction.endTransaction(Transaction.COMMIT);
					System.out.println("I committed");
				}
				else {
					transaction.endTransaction(Transaction.ROLLBACK);
					System.out.println("I rolled back");
				}
				
				printData(rs);
			}
			else if(type.equals("Marinduque") || type.equals("Palawan")) {
				new Thread(new SEND(new Message(targetLocation, "UPDATE", type, "id=" + id + ",value=" + value))).start();
			}
		}
		else if(targetLocation.equals("Marinduque")) {
			if(/*type.equals("Marinduque") || */type.equals("Central")) {
				Transaction2 transaction = new Transaction2();
				transaction.setIsolationLevel(iso_level);
				transaction.beginTransaction();
				ResultSet rs = transaction.transactionBody(type, id, value, 199036);
				
				new Thread(new SEND(new Message(targetLocation, "UPDATE", type, "id=" + id + ",value=" + value))).start();
				
				while(!WRITE_RESULT);
				
				if(NEIGHBOR_WRITE_SUCCESSFUL) {
					transaction.endTransaction(Transaction.COMMIT);
					System.out.println("I committed");
				}
				else {
					transaction.endTransaction(Transaction.ROLLBACK);
					System.out.println("I rolled back");
				}
				
				printData(rs);
			}
			else if(type.equals("Palawan") || type.equals("Marinduque")) {
				new Thread(new SEND(new Message(targetLocation, "UPDATE", type, "id=" + id + ",value=" + value))).start();
			}
		}
		
		System.out.println("End of global write");
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
			else if("UPDATE".equals(command)) {
				String originalSender = item.getOriginalSender();
				String targetLocation = item.getSender();
				//new Thread(new SEND(new Message(targetLocation, "UPDATE", type, "id=" + id + ",value=" + value))).start();
				if(originalSender.equals("Palawan")) {
					if(targetLocation.equals("Palawan")) {
						try {
							s = new Socket(central.getIpadd(), Port);
							s.setSoTimeout(2000);
							
							System.out.println("About to send tocentral update, target Location is Palawan");
							
							item.setCommand("TOCENTRALUPDATE");
							
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.writeObject(item);
							pw.flush();
							s.close();
							
							System.out.println("Command: " + item.getCommand());
							
							System.out.println("Send to central update");
						}
						catch(Exception e) {
							System.out.println("Updating data of Palawan in Central from Palawan failed");
							e.printStackTrace();
							NEIGHBOR_WRITE_SUCCESSFUL = false;
							WRITE_RESULT = true;
						}
					}
					else if(targetLocation.equals("Marinduque")){
						try {
							s = new Socket(central.getIpadd(), Port);
							s.setSoTimeout(2000);
							
							System.out.println("About to send tocentral update, target Location is Marinduque");
							
							item.setCommand("TOCENTRALUPDATE");
							
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.writeObject(item);
							pw.flush();
							s.close();
							
							System.out.println("Command: " + item.getCommand());
							
							System.out.println("Send to central update");
							
						}
						catch(Exception e) {
							System.out.println("Cannot update to Marinduque, Central is closed, transaction aborted");
							e.printStackTrace();

							NEIGHBOR_WRITE_SUCCESSFUL = false;
							WRITE_RESULT = true;
						}
					}
				}
				else if(originalSender.equals("Marinduque")) {
					if(targetLocation.equals("Marinduque")) {
						try {
							s = new Socket(central.getIpadd(), Port);
							s.setSoTimeout(2000);
							
							item.setCommand("TOCENTRALUPDATE");
							
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.writeObject(item);
							pw.flush();
							s.close();
						}
						catch(Exception e) {
							e.printStackTrace();
							System.out.println("Updating data of Marinduque in Central from Marinduque failed");
							NEIGHBOR_WRITE_SUCCESSFUL = false;
							WRITE_RESULT = true;
						}
					}
					else if(targetLocation.equals("Palawan")) {
						try {
							s = new Socket(central.getIpadd(), Port);
							s.setSoTimeout(2000);
							
							item.setCommand("TOCENTRALUPDATE");
							
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.writeObject(item);
							pw.flush();
							s.close();
							
						}
						catch(Exception e) {
							System.out.println("Cannot update to Palawan, Central is closed, transaction aborted");
							e.printStackTrace();

							NEIGHBOR_WRITE_SUCCESSFUL = false;
							WRITE_RESULT = true;
						}
					}
				}
				else if(originalSender.equals("Central")) {
					if(targetLocation.equals("Palawan")) {
						try {
							s = new Socket(palawan.getIpadd(), Port);
							s.setSoTimeout(2000);
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.writeObject(item);
							pw.flush();
							s.close();
							
							System.out.println("Updating data of Palawan in Palawan from Central, sent update to central");
						}
						catch(Exception e) {
							System.out.println("Updating data of Palawan in Palawan from Central failed");
							e.printStackTrace();
							NEIGHBOR_WRITE_SUCCESSFUL = false;
							WRITE_RESULT = true;
						}
					}
					else if(targetLocation.equals("Marinduque")) {
						try {
							s = new Socket(marin.getIpadd(), Port);
							s.setSoTimeout(2000);
							ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
							pw.writeObject(item);
							pw.flush();
							s.close();
							
							System.out.println("Updating data of Marinduque in Marinduque from Central, sent update to central");
						}
						catch(Exception e) {
							System.out.println("Updating data of Marinduque in Marinduque from Central failed");
							e.printStackTrace();
							NEIGHBOR_WRITE_SUCCESSFUL = false;
							WRITE_RESULT = true;
						}
					}	
				}
			}
			else if("UPDATERESPONSE".equals(command)) {
				String originalSender = item.getOriginalSender();
				
				if(originalSender.equals("Palawan")) {
					try {
						s = new Socket(palawan.getIpadd(), Port);
						s.setSoTimeout(2000);
						ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
						pw.writeObject(item);
						pw.flush();
						s.close();
						
						System.out.println("Sending UPDATERESPONSE to Palawan, message is from " + sender);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				else if(originalSender.equals("Marinduque")) {
					try {
						s = new Socket(marin.getIpadd(), Port);
						s.setSoTimeout(2000);
						ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
						pw.writeObject(item);
						pw.flush();
						s.close();
						
						System.out.println("Sending UPDATERESPONSE to Marinduque, message is from " + sender);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				else if(originalSender.equals("Central")) {
					try {
						s = new Socket(central.getIpadd(), Port);
						s.setSoTimeout(2000);
						ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
						pw.writeObject(item);
						pw.flush();
						s.close();
						
						System.out.println("Sending UPDATERESPONSE to Central, message is from " + sender);
					}
					catch(Exception e) {
						e.printStackTrace();
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
	
	public void readLocal(int isolevel) {

		if (type.equals("Palawan")) {
//			d.appendProgress("PALAWAN");
			Transaction1 transaction = new Transaction1();
			if (isolevel != 0)
				transaction.setIsolationLevel(isolevel);
			transaction.beginTransaction();
			ResultSet rs = transaction.transactionBody(type, 0, 0, 0);
			transaction.endTransaction(Transaction.COMMIT);

			printData(rs);

			// take moreData and rs
			READ_RESULT = false;

		} else if (type.equals("Marinduque")) {

			Transaction1 transaction = new Transaction1();
			transaction.beginTransaction();
			ResultSet rs = transaction.transactionBody(type, 0, 0, 0);
			transaction.endTransaction(Transaction.COMMIT);

			printData(rs);

			// take moreData and rs
			READ_RESULT = false;

		} else {

			Transaction1 transaction = new Transaction1();
			transaction.beginTransaction();
			ResultSet rs = transaction.transactionBody(type, 0, 0, 0);
			transaction.endTransaction(Transaction.COMMIT);

			printData(rs);

			// take moreData and rs
			READ_RESULT = false;
		}
	}
	
public void readDoubleLocal(int t1isoL, int t2isoL){
		
		Thread T1 = new Thread(){
			  public void run () {
				  Transaction1 transaction1 = new Transaction1();
				  transaction1.setIsolationLevel(t1isoL);
				  transaction1.beginTransaction();
				  setTRS(transaction1.transactionBody(type, 0, 0, 0), 1);
				  transaction1.endTransaction(Transaction.COMMIT);		    
			  }
			};
			
		Thread T2 = new Thread(){
			  public void run () {
				  Transaction1 transaction1 = new Transaction1();
				  transaction1.setIsolationLevel(t1isoL);
				  transaction1.beginTransaction();
				  setTRS(transaction1.transactionBody(type, 0, 0, 0), 2);
				  transaction1.endTransaction(Transaction.COMMIT);;
			  }
			};
		
		T1.start();
		try {
			T1.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		T2.start();
	}
	
	public void readwrite(){
		
		Thread T1 = new Thread(){
			  public void run () {
				  Transaction1 transactionRead = new Transaction1();
				  transactionRead.setIsolationLevel(0);
				  transactionRead.beginTransaction();
				  setTRS(transactionRead.transactionBody(type, 0, 0, 0), 1);
				  transactionRead.endTransaction(Transaction.COMMIT);
			  }
			};
			
			Thread T2 = new Thread(){
				  public void run () {
					  Transaction2 transactionWrite = new Transaction2();
					  transactionWrite.setIsolationLevel(0);
					  transactionWrite.beginTransaction();
					  setTRS(transactionWrite.transactionBody(type, 0, 0, 0), 2);
					  transactionWrite.endTransaction(Transaction.COMMIT);;
				  }
				};
				
		T1.start();
		T2.start();
	}
	
	public void setTRS(ResultSet rs, int x){
		if(x == 1){
			tempRS1 = rs;
			printData(tempRS1);
			}
		if(x == 2){
			tempRS2 = rs;
			printData(tempRS2);
		}
	}
	
	public void writeResponseAction(Message message) {
		String sender = message.getSender();
		String text = message.getText();
		System.out.println("In write response action");
		
		if(type.equals("Palawan")) {
			if(sender.equals("Central")) {
				if(text.equals("OK")) {
					NEIGHBOR_WRITE_SUCCESSFUL = true;
					WRITE_RESULT = true;
				}
			}
		}
		else if (type.equals("Marinduque")) {
			if(sender.equals("Central")) {
				if(text.equals("OK")) {
					NEIGHBOR_WRITE_SUCCESSFUL = true;
					WRITE_RESULT = true;
				}
			}
		}
		else if(type.equals("Central")) {
			if(sender.equals("Palawan") || sender.equals("Marinduque")) {
				if(text.equals("OK")) {
					NEIGHBOR_WRITE_SUCCESSFUL = true;
					WRITE_RESULT = true;
				}
			}
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
		
		public void printData(ResultSet rs){

			try {
				d.appendText("\n");

				int numColumns = rs.getMetaData().getColumnCount();

				for (int j = 1; j <= numColumns; j++) {
					String tempString = String.format("%-25s\t", rs
							.getMetaData().getColumnName(j));
					d.appendText(tempString);
				}
				d.appendText("\n");

				while (rs.next()) {

					for (int i = 1; i <= numColumns; i++)
						d.appendText(String.format("%-25s\t", rs.getObject(i)));
					d.appendText("\n");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}
