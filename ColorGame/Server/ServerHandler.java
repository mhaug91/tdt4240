import java.net.*;
import java.io.*;
import java.util.*;

/*
 * The Client that can be run both as a console or a GUI
 */
public class ServerHandler  {

	// for I/O
	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket
	private Socket socket;

	// the server, the port and the username
	private String server, username;
	private int port;


	ServerHandler(String server, int port, String username) {
		this.server = server;
		this.port = port;
		this.username = username;
	}
	
	/*
	 * To start the dialog
	 */
	public boolean start() {
		// try to connect to the server
		try {
			socket = new Socket(server, port);
		} 
		// if it failed not much I can so
		catch(Exception ec) {
			display("Error connectiong to server:" + ec);
			return false;
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
			return false;
		}

		// creates the Thread to listen from the server 
		new ListenFromServer().start();
		// Send our username to the server this is the only message that we
		// will send as a String. All other messages will be ChatMessage objects
		try
		{
			sOutput.writeObject(username);
		}
		catch (IOException eIO) {
			display("Exception doing login : " + eIO);
			disconnect();
			return false;
		}
		// success we inform the caller that it worked
		return true;
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
	void sendMessage(ColorMessage msg) {
		try {
			sOutput.writeObject(msg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	/*
	 * When something goes wrong
	 * Close the Input/Output streams and disconnect not much to do in the catch clause
	 */
	private void disconnect() {
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
			
	}
	
	void startGame(String username){
		ColorMessage startMsg = new ColorMessage(ColorMessage.START, username);
		sendMessage(startMsg);
	}
	
	void sendUsername(String username){
		ColorMessage nameMsg = new ColorMessage(ColorMessage.USERNAME, username);
		sendMessage(nameMsg);
	}
	
	void joinGame(String gameSession){
		ColorMessage joinMsg = new ColorMessage(ColorMessage.JOIN, gameSession);
		sendMessage(joinMsg);
	}
	
	void beginRound(){
		ColorMessage beginMsg = new ColorMessage(ColorMessage.BEGIN);
		sendMessage(beginMsg);
	}
	
	void sendScore(int score){
		ColorMessage clrMsg = new ColorMessage(ColorMessage.COLOR, Integer.toString(score));
		sendMessage(clrMsg);
	}


	public static void main(String[] args) {
		// default values
		int portNumber = 1501;
		String serverAddress = "localhost";
		String userName = "Anonymous";

		// depending of the number of arguments provided we fall through
		switch(args.length) {
			// > javac Client username portNumber serverAddr
			case 3:
				serverAddress = args[2];
			// > javac Client username portNumber
			case 2:
				try {
					portNumber = Integer.parseInt(args[1]);
				}
				catch(Exception e) {
					System.out.println("Invalid port number.");
					System.out.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
					return;
				}
			// > javac Client username
			case 1: 
				userName = args[0];
			// > java Client
			case 0:
				break;
			// invalid number of arguments
			default:
				System.out.println("Usage is: > java Client [username] [portNumber] {serverAddress]");
			return;
		}
		// create the Client object
		ServerHandler serverHandler = new ServerHandler(serverAddress, portNumber, userName);
		// test if we can start the connection to the Server
		// if it failed nothing we can do
		if(!serverHandler.start())
			return;
		
		serverHandler.startGame("LeifOve");
		serverHandler.beginRound();
		serverHandler.sendScore(8008);
		
	}

	/*
	 * a class that waits for the message from the server and append them to the JTextArea
	 * if we have a GUI or simply System.out.println() it in console mode
	 */
	class ListenFromServer extends Thread {

		public void run() {
			while(true) {
				try {
					ColorMessage cm = (ColorMessage) sInput.readObject();
					
					switch (cm.getType()) {
					case ColorMessage.BEGIN:
						System.out.println("Round began with color: " + cm.getColor().toString());
						break;
					case ColorMessage.START:
						System.out.println("Successfully created a new game with game session ID: " + cm.getMessage().get(0));
						break;
					case ColorMessage.COLOR:
						System.out.println("Round ended and scored received");
						System.out.println(cm.getMessage().toString());
						break;
						
					}
					
				}
				catch(IOException e) {
					display("Server has close the connection: " + e);
					break;
				}
				// can't happen with a String object but need the catch anyhow
				catch(ClassNotFoundException e2) {
				}
			}
		}
	}
}
