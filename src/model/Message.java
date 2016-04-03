package model;

import java.io.Serializable;
import java.sql.ResultSet;

public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sender;
	private String command;
	private String originalSender;
	private ResultSet data;
	
	
	
	public Message(String sender, String command) {
		super();
		this.sender = sender;
		this.command = command;
	}

	public Message(String sender, String command, String originalSender, ResultSet data) {
		super();
		this.sender = sender;
		this.command = command;
		this.originalSender = originalSender;
		this.data = data;
	}
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	
	public String getOriginalSender() {
		return originalSender;
	}

	public void setOriginalSender(String originalSender) {
		this.originalSender = originalSender;
	}

	public ResultSet getData() {
		return data;
	}
	public void setData(ResultSet data) {
		this.data = data;
	}
	
	

}
