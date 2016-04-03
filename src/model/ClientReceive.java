package model;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.Arrays;

public class ClientReceive implements Runnable{
	 private Controller c;
	 private Socket S;
	 public ClientReceive(Controller c, Socket X)
	 {
		  	this.c = c;
		  	this.S = X; // pinasa na yung socket :)
	 }
	 
	 @Override
	 public void run() {
		//receive
		 // process the message received
				try{
					//System.out.println("Client receive (start)");
					
					InputStream input = S.getInputStream();
								
					//System.out.println("Client receive (read bytes)");
					
					byte [] scannedbytes = new byte [65500];
				    int bytesRead = input.read(scannedbytes,0,scannedbytes.length);
				    int current = bytesRead;
				    
				    //System.out.println("Client receive (continue to read bytes)");
				    
				    do{
				    	//System.out.println("Client receive (in do while)");
				    	bytesRead = input.read(scannedbytes,current,scannedbytes.length - current);
				    	if(bytesRead > -1)
				    		current += bytesRead;
				    	//System.out.println("Client receive (read " + bytesRead + "/" + current + " bytes)");
				    } while(bytesRead > 0);
				    
				    //System.out.println("Client receive (bytes read: " + current + ")");
				    
				    
				    //input command is the received string
				    String message = new String(scannedbytes, "UTF-8");
				    
				    System.out.println("I received: " + message);
				    
				    String sender = message.substring(message.indexOf('<') + 1, message.indexOf('>'));
					String command = message.substring(message.indexOf('(') + 1, message.indexOf(')'));
				    
				    if("READ".equals(command)) {

				    	Transaction1 transaction = new Transaction1();
	            		transaction.beginTransaction();
	            		ResultSet rs = transaction.transactionBody(0, 0, 0, false);
	            		transaction.endTransaction(Transaction.COMMIT);
	            		
	            		String toSend = "<" + c.getType() + ">" + "(READRESPONSE)\"" + sender + "\"[";
	            		
	            		while(rs.next()) {
	            			toSend += "{" + rs.getInt(1) + "," + rs.getInt(2) + "," + rs.getInt(3) + "}|";
	            		}
	            		
	            		toSend = toSend.substring(0, toSend.length()-1);
	            		toSend += "]";
	            	
	            		System.out.println(toSend);
	            		
	            		c.sendMessage(toSend);
				    	
				    	
				    	//c.SEND("<Palawan>(READRESPONSE)[{}]");
				    }
				    else if("READRESPONSE".equals(command)) {
				    	System.out.println("i sent the response");
				    	System.out.println("i figure out who sent me and i add/concatenate stuff here");
				    	
				    	c.readResponseAction(message);
				    	
				    }
				    
				    //System.out.println("Client receive (input string start: " + InputCommand.substring(0,5) + ")");
					
				   //process protocol
					/*if(InputCommand.substring(0, 7).equals("\"READ\" "))
					{
						System.out.println("Client receive (enter READ)");
						c.ReadingAction(S.getInetAddress().toString().substring(1), Arrays.copyOfRange(scannedbytes, InputCommand.substring(0,7).getBytes().length, current), "localhost");
					}*/
				}
				catch(Exception x){
					x.printStackTrace();
					
				}
		 
	 }

}