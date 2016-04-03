package model;

import java.io.Serializable;
import java.sql.ResultSet;

import com.sun.rowset.CachedRowSetImpl;

public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sender;
	private String command;
	private String text;
	private String originalSender;
	private CachedRowSetImpl data;
	
	
	
	public Message(String sender, String command) {
		super();
		this.sender = sender;
		this.command = command;
	}

	public Message(String sender, String command, String originalSender, CachedRowSetImpl data) {
		super();
		this.sender = sender;
		this.command = command;
		this.originalSender = originalSender;
		this.data = data;
	}
	
	public Message(String sender, String command, String originalSender, String text) {
		super();
		this.sender = sender;
		this.command = command;
		this.originalSender = originalSender;
		this.text = text;
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
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getOriginalSender() {
		return originalSender;
	}

	public void setOriginalSender(String originalSender) {
		this.originalSender = originalSender;
	}

	public CachedRowSetImpl getData() {
		return data;
	}
	public void setData(CachedRowSetImpl data) {
		this.data = data;
	}
	
	

}
