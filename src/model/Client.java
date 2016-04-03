package model;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class Client {
	 private Controller c;
	 public Client(Controller c, Socket X)
	 {
		  	this.c = c;
		  	RECEIVE(X); // pinasa na yung socket :)
	 }

	 //receive
	 // process the message received
	 void RECEIVE(Socket S){
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
			    	System.out.println("Query here");
			    	c.SEND("<Palawan>(READRESPONSE)[{}]");
			    }
			    else if("READRESPONSE".equals(command)) {
			    	System.out.println("i sent the response");
			    	System.out.println("i figure out who sent me and i add/concatenate stuff here");
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
