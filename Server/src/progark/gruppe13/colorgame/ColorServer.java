package progark.gruppe13.colorgame;


import java.awt.Color;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * The server that can be run both as a console application or a GUI
 */
public class ColorServer {
	private ArrayList<Color> colors;
	
	// a unique ID for each connection
	private static int uniqueId;
	
	// an ArrayList to keep the list of the Client
	private ArrayList<ClientThread> clients;
	private ArrayList<String> gameSessions;
	
	private Map<String, Color> colorDict;
	
	private SimpleDateFormat sdf;
	// the port number to listen for connection
	private int port;
	// the boolean that will be turned of to stop the server
	private boolean running;
	
	/*
	 *  server constructor that receive the port to listen to for connection as parameter
	 *  in console
	 */
	
	public ColorServer(int port) {
		// the port
		this.port = port;
		// to display hh:mm:ss
		sdf = new SimpleDateFormat("HH:mm:ss");
		// ArrayList for the Client list
		clients = new ArrayList<ClientThread>();
		
		gameSessions = new ArrayList<String>();
		
		this.colorDict = new HashMap<String, Color>();
		
		this.colors = new ArrayList<Color>();
		this.colors.add(new Color(0, 45, 255));
		this.colors.add(new Color(0, 235, 115));
		this.colors.add(new Color(255, 248, 0));
		this.colors.add(new Color(232, 106, 12));
		this.colors.add(new Color(255, 13, 252));
		this.colors.add(new Color(255, 30, 0));
	}
	
	public void start() {
		running = true;
		/* create socket server and wait for connection requests */
		try 
		{
			// the socket used by the server
			ServerSocket serverSocket = new ServerSocket(port);

			// infinite loop to wait for connections
			while(running) 
			{
				// format message saying we are waiting
				display("Server waiting for Clients on port " + port + ".");
				
				Socket socket = serverSocket.accept();  	// accept connection
				// if I was asked to stop
				if(!running)
					break;
				ClientThread clientThread = new ClientThread(socket);  // make a thread of it
				clients.add(clientThread);								// save it in the ArrayList
				clientThread.start();
			}
			// I was asked to stop
			try {
				serverSocket.close();
				for(int i = 0; i < clients.size(); ++i) {
					ClientThread tc = clients.get(i);
					try {
					tc.sInput.close();
					tc.sOutput.close();
					tc.socket.close();
					}
					catch(IOException ioE) {
						// not much I can do
					}
				}
			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		// something went bad
		catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}		
    /*
     * For the GUI to stop the server
     */
	protected void stop() {
		running = false;
		// connect to myself as Client to exit statement 
		// Socket socket = serverSocket.accept();
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
			// nothing I can really do
		}
	}
	/*
	 * Display an event (not a message) to the console or the GUI
	 */
	private void display(String msg) {
		String time = sdf.format(new Date()) + " " + msg;
		System.out.println(time);
	}
	
	/*
	 *  to broadcast a message to all clients in a game session
	 */	
	private synchronized void broadcast(ColorMessage message, String gameSession) {
		// we loop in reverse order in case we would have to remove a Client
		// because it has disconnected
		for(int i = clients.size(); --i >= 0;) {
			ClientThread ct = clients.get(i);
			// try to write to the Client if it fails remove it from the list
			if(ct.gameSession.equals(gameSession) && !ct.writeMsg(message)) {
				clients.remove(i);
				display("Disconnected Client " + ct.username + " removed from list.");
			}
		}
	}

	// for a client who logoff using the LOGOUT message
	synchronized void remove(int id) {
		// scan the array list until we found the Id
		for(int i = 0; i < clients.size(); ++i) {
			ClientThread ct = clients.get(i);
			// found it
			if(ct.id == id) {
				clients.remove(i);
				return;
			}
		}
	}
	
	/*
	 *  To run as a console application just open a console window and: 
	 * > java Server
	 * > java Server portNumber
	 * If the port number is not specified 1500 is used
	 */ 
	public static void main(String[] args) {
		// start server on port 1500 unless a PortNumber is specified 
		int portNumber = 1502;
		switch(args.length) {
			case 1:
				try {
					portNumber = Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					System.out.println("Invalid port number.");
					System.out.println("Usage is: > java Server [portNumber]");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Usage is: > java Server [portNumber]");
				return;
				
		}
		// create a server object and start it
		ColorServer server = new ColorServer(portNumber);
		server.start();
	}

	/** One instance of this thread will run for each client */
	class ClientThread extends Thread {
		// the socket where to listen/talk
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		// my unique id (easier for deconnection)
		int id;
		// the Username of the Client
		String username;
		String gameSession = null;
		boolean host = false;
		boolean roundFinished = false;
		ColorMessage cm;
		
		int score = 0;
		
		String date;

		ClientThread(Socket socket) {
			// a unique id
			id = ++uniqueId;
			this.socket = socket;
			System.out.println("Thread trying to create Object Input/Output Streams");
			try
			{
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
				username = (String) sInput.readObject();
				display(username + " just connected.");
			}
			catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			catch (ClassNotFoundException e) {
			}
            date = new Date().toString() + "\n";
		}

		public void run() {

			boolean running = true;
			while(running) {

				try {
					cm = (ColorMessage) sInput.readObject();
				}
				catch (IOException e) {
					display(username + " Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					display("class not found exception: " + e2);
					break;
				}

				// Switch on the type of message receive
				switch(cm.getType()) {
				
				case ColorMessage.COLOR:
					display("Color message received");
					this.score += Integer.parseInt(cm.getMessage().get(0));
					this.roundFinished = true;
					boolean allFinished = true;
					
					//Check if everyone is finished
					for (ClientThread ct : clients){
						if (ct.gameSession.equals(this.gameSession) && !ct.roundFinished){
							allFinished = false;
						}
					}
					//Broadcast scores
					if (allFinished){
						broadcast(new ColorMessage(ColorMessage.COLOR, "Success"), this.gameSession);	
					}
					
					break;
				
				case ColorMessage.AFTERMATH:
					display("Aftermath message received");
					ColorMessage colorMsg = new ColorMessage(ColorMessage.AFTERMATH);						
					for (ClientThread ct : clients){
						if (ct.gameSession.equals(this.gameSession)){
							colorMsg.addMessage(ct.username);
							colorMsg.addMessage(Integer.toString(ct.score));
						}
					}
					writeMsg(colorMsg);
				
				case ColorMessage.BEGIN:
					display("Begin message received");
					if (this.host){
						Random rand = new Random();
						Color randColor = colors.get(rand.nextInt(colors.size()));
						colorDict.put(this.gameSession, randColor);
						broadcast(new ColorMessage(ColorMessage.BEGIN, "success"), this.gameSession);
					}
					else{
						writeMsg(new ColorMessage(ColorMessage.BEGIN, "ERROR: Only the host can begin"));
					}
					break;
					
				case ColorMessage.ROUND:
					display("Round message received");
					this.roundFinished = false;
					Color c = colorDict.get(this.gameSession);
					ColorMessage cMsg = new ColorMessage(ColorMessage.ROUND, Integer.toString(c.getRed()));
					cMsg.addMessage(Integer.toString(c.getGreen()));
					cMsg.addMessage(Integer.toString(c.getBlue()));
					writeMsg(cMsg);
					
				
				//Starting a new game
				case ColorMessage.START:
					display("Start message received: " +  cm.getMessage().toString());
					if (cm.getMessage().size() == 1){
						this.username = cm.getMessage().get(0);
						this.gameSession = Integer.toString(this.id);
						gameSessions.add(Integer.toString(this.id));
						this.host = true;
						writeMsg(new ColorMessage(ColorMessage.START, this.gameSession));
					}
					else{
						display("Stopped shit from fucking itself");
					}
					break;
				
				//Joining an existing game
				case ColorMessage.JOIN:
					display("Join message received");
					if (this.gameSession == null){
						String joinSession = cm.getMessage().get(0);
						display("Join ID input: " + joinSession);
						for (String session : gameSessions){
							display("Search session: " + session);
							if (session.equals(joinSession)){
								this.gameSession = joinSession;
								writeMsg(new ColorMessage(ColorMessage.JOIN, "Joined the game session"));
							}
							else{
								writeMsg(new ColorMessage(ColorMessage.JOIN, "ERROR: No such game session"));
							}
						}
					}
					else{
						display("Already joined a game");
						writeMsg(new ColorMessage(ColorMessage.JOIN,"ERROR: Already joined a game"));
					}
					break;
				
				case ColorMessage.USERNAME:
					if (this.gameSession != null && !this.host){
						String potentialName= cm.getMessage().get(0);
						boolean taken = false;
						for (ClientThread ct : clients){
							if (ct.gameSession != null && ct.gameSession == this.gameSession && ct.username.equals(potentialName)){
								writeMsg(new ColorMessage(ColorMessage.USERNAME, "ERROR: Username taken"));
								taken = true;
							}
						}
						if (!taken){
							this.username = potentialName;
							writeMsg(new ColorMessage(ColorMessage.USERNAME, "Username created"));
							ColorMessage updateMsg = new ColorMessage(ColorMessage.USERNAME);
							for (ClientThread ct : clients){
								if (ct.gameSession.equals(this.gameSession)){
									updateMsg.addMessage(ct.username);
								}
							}
							broadcast(updateMsg, this.gameSession);
						}
						
					}
					break;
				
				case ColorMessage.LOGOUT:
					display(username + " disconnected with a LOGOUT message.");
					running = false;
					break;

				case ColorMessage.GAMESESSION:
					if (this.gameSession == null){
						this.gameSession = cm.getMessage().get(0);
						System.out.println("The game session ID was set to: " + this.gameSession);
					}
					else {
						writeMsg(new ColorMessage(ColorMessage.GAMESESSION, "Error: You are already connected to a game session"));
					}
					break;
					
				case ColorMessage.GETNAMES:
					ColorMessage namesMsg = new ColorMessage(ColorMessage.GETNAMES);
					for (ClientThread ct : clients){
						if (ct.gameSession != null && ct.gameSession.equals(this.gameSession)){
							namesMsg.addMessage(ct.username);
						}
					}
					writeMsg(namesMsg);
					break;
				default:
					display("Default switch");
					break;
				}
					
			}

			display(this.username + " disconnected");
			remove(id);
			close();
		}
		
		// try to close everything
		private void close() {
			// try to close the connection
			try {
				if(sOutput != null) sOutput.close();
			}
			catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}

		//Write a String to the Client output stream
		private boolean writeMsg(ColorMessage msg) {
			display("Writing message to " + this.username + ": " + msg.getMessage());
			// if Client is still connected send the message to it
			if(!socket.isConnected()) {
				close();
				return false;
			}
			// write the message to the stream
			try {
				sOutput.writeObject(msg);
			}
			// if an error occurs, do not abort just inform the user
			catch(IOException e) {
				display("Error sending message to " + username);
				display(e.toString());
			}
			return true;
		}
	}
}

