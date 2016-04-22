package progark.gruppe13.colorgame;


import java.awt.Color;
import java.io.*;
import java.util.ArrayList;

public class ColorMessage implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	// The different types of message sent by the Client
	static final int START = 0, JOIN = 1, COLOR = 2, GAMESESSION = 3, LOGOUT = 4, USERNAME = 5, BEGIN = 6;
	private int type;
	private ArrayList<String> message;
	private Color color;
	
	ColorMessage(int type) {
		this.type = type;
		this.message = new ArrayList<String>();
		this.color = null;
	}
	
	ColorMessage(int type, Color c) {
		this.type = type;
		this.message = new ArrayList<String>();
		this.color = c;
	}
	
	ColorMessage(int type, String m) {
		this.type = type;
		this.message = new ArrayList<String>();
		this.message.add(m);
		this.color = null;
	}
	
	int getType() {
		return type;
	}
	ArrayList<String> getMessage() {
		return message;
	}
	
	void addMessage(String m){
		this.message.add(m);
	}
	
	void addColor(Color c){
		this.color = c;
	}
	
	Color getColor(){
		return this.color;
	}
}
