package progark.gruppe13.colorgame;
import android.graphics.Color;
import android.os.AsyncTask;

import java.net.*;
import java.io.*;
import java.util.*;

/*
 * The Client that can be run both as a console or a GUI
 */
public class ServerHandler{


	// for I/O
	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket
	private Socket socket;

	// the server, the port and the username
	private String server, username;
	private int port;

	private AsyncConnect aConnection;
	private ListenFromServer aListen;

	private boolean connected;

	private GameState listener;


	ServerHandler(String server, int port, String username) {
		this.server = server;
		this.port = port;
		this.username = username;

		this.aConnection = new AsyncConnect();
		this.aListen = new ListenFromServer();
		this.aConnection.execute();
		this.aListen.execute();
		this.connected = true;
	}

	public void setListener(GameState listener){
		this.listener = listener;
	}

	/*
	 * To send a message to the console or the GUI
	 */
	private void display(String msg) {
		System.out.println(msg);
	}

	/*
	 * To send a message to the server
	 */
	private class SendMessage extends AsyncTask<ColorMessage, Void, String>{
		@Override
		protected String doInBackground(ColorMessage... msg) {
			display("Sending message to server...");
			try {
				sOutput.writeObject(msg);
			}
			catch(IOException e) {
				display("Exception writing to server: " + e);
			}
			return "Hallo";
		}

	}

	/*
	 * When something goes wrong
	 * Close the Input/Output streams and disconnect not much to do in the catch clause
	 */
	public void disconnect() {
		ColorMessage clrMsg = new ColorMessage(ColorMessage.LOGOUT);
		try {
			sOutput.writeObject(clrMsg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
		try {
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {} // not much else I can do
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {} // not much else I can do
		try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {} // not much else I can do

		this.aConnection.cancel(true);
		this.aListen.cancel(true);
		this.connected = false;
	}

	public void startGame(String username){
		ColorMessage startMsg = new ColorMessage(ColorMessage.START, username);
		try {
			sOutput.writeObject(startMsg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	public void sendUsername(String username){
		ColorMessage nameMsg = new ColorMessage(ColorMessage.USERNAME, username);
		try {
			sOutput.writeObject(nameMsg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	public void joinGame(String gameSession){
		ColorMessage joinMsg = new ColorMessage(ColorMessage.JOIN, gameSession);
		try {
			sOutput.writeObject(joinMsg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	public void beginRound(){
		System.out.println("Sending message beginRound");
		ColorMessage beginMsg = new ColorMessage(ColorMessage.BEGIN);
		try {
			sOutput.writeObject(beginMsg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	public void sendScore(int score){
		ColorMessage clrMsg = new ColorMessage(ColorMessage.COLOR, Integer.toString(score));
		try {
			sOutput.writeObject(clrMsg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}




	public void getUsernames(String gameID){
		ColorMessage userMsg = new ColorMessage(ColorMessage.GETNAMES);
		try {
			sOutput.writeObject(userMsg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	public void getColor(){
		ColorMessage msg = new ColorMessage(ColorMessage.ROUND);
		try {
			sOutput.writeObject(msg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	public void getScores(){
		ColorMessage msg = new ColorMessage(ColorMessage.AFTERMATH);
		try {
			sOutput.writeObject(msg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	public void getGameSessionId(){
		ColorMessage msg = new ColorMessage(ColorMessage.GETID);
		try {
			sOutput.writeObject(msg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	public void connect(){
		if (!this.connected){
			this.aConnection = new AsyncConnect();
			this.aListen = new ListenFromServer();
			this.aConnection.execute();
			this.aListen.execute();
			this.connected = true;
		}
		else{
			System.out.println("Connection error: Already connected");
		}
	}

	private class AsyncConnect extends AsyncTask<Void, Void, String> {

		//Connects to the server
		@Override
		protected String doInBackground(Void... args) {
			// try to connect to the server
			try {
				socket = new Socket(server, port);
			}
			// if it failed not much I can so
			catch(Exception ec) {
				display("Error connectiong to server:" + ec);
				return ec.toString();
			}

			String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
			display(msg);
		
			/* Creating both Data Stream */
			try
			{
				sInput  = new ObjectInputStream(socket.getInputStream());
				sOutput = new ObjectOutputStream(socket.getOutputStream());
			}
			catch (IOException eIO) {
				display("Exception creating new Input/output Streams: " + eIO);
				return eIO.toString();
			}

			// creates the Thread to listen from the server 
			//new ListenFromServer().execute();
			// Send our username to the server this is the only message that we
			// will send as a String. All other messages will be ChatMessage objects
			try
			{
				sOutput.writeObject(username);
			}
			catch (IOException eIO) {
				display("Exception doing login : " + eIO);
				disconnect();
				return eIO.toString();
			}
			// success we inform the caller that it worked
			return "success";
		}
	}

	/*
	 * a class that waits for the message from the server and append them to the JTextArea
	 * if we have a GUI or simply System.out.println() it in console mode
	 */
	private class ListenFromServer extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... args) {
			while(true) {
				try {
					ColorMessage cm = (ColorMessage) sInput.readObject();
					listener.serverCallback(cm);

				}
				catch(IOException e) {
					display("Server has closed the connection: " + e);
					break;
				}
				// can't happen with a String object but need the catch anyhow
				catch(ClassNotFoundException e2) {
					display("Class not found exception: " + e2);
				}
			}return "success";
		}
	}
}