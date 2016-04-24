package progark.gruppe13.colorgame;


import java.awt.Color;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class ColorServer {
	private ArrayList<Color> colors;
	
	private static int uniqueId;
	
	private ArrayList<ClientThread> clients;
	private ArrayList<String> gameSessions;
	
	private Map<String, Color> colorDict;
	
	private SimpleDateFormat sdf;
	private int port;
	private boolean running;
	
	
	public ColorServer(int port) {
		this.port = port;
		sdf = new SimpleDateFormat("HH:mm:ss");
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
		try 
		{
			ServerSocket serverSocket = new ServerSocket(port);

			while(running) 
			{
				display("Server waiting for Clients on port " + port + ".");
				
				Socket socket = serverSocket.accept(); 
				if(!running)
					break;
				ClientThread clientThread = new ClientThread(socket);  
				clients.add(clientThread);
				clientThread.start();
			}
			try {
				serverSocket.close();
				for(int i = 0; i < clients.size(); ++i) {
					ClientThread tc = clients.get(i);
					try {
					tc.sInput.close();
					tc.sOutput.close();
					tc.socket.close();
					}
					catch(IOException ioE) {}
				}
			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}		

	protected void stop() {
		running = false;
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {}
	}

	private void display(String msg) {
		String time = sdf.format(new Date()) + " " + msg;
		System.out.println(time);
	}
	
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

	synchronized void remove(int id) {
		// scan the array list until we found the Id
		for(int i = 0; i < clients.size(); ++i) {
			ClientThread ct = clients.get(i);
			if(ct.id == id) {
				clients.remove(i);
				return;
			}
		}
	}
	
	public static void main(String[] args) {
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
		ColorServer server = new ColorServer(portNumber);
		server.start();
	}

	class ClientThread extends Thread {
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		int id;
		String username;
		String gameSession = null;
		boolean host = false;
		boolean roundFinished = false;
		ColorMessage cm;
		
		int score = 0;
		
		String date;

		ClientThread(Socket socket) {
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
					
				case ColorMessage.GETID:
					ColorMessage idMsg = new ColorMessage(ColorMessage.GETID, this.gameSession);
					writeMsg(idMsg);
					
				default:
					display("Default switch");
					break;
				}
					
			}

			display(this.username + " disconnected");
			remove(id);
			close();
		}
		
		private void close() {
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

		private boolean writeMsg(ColorMessage msg) {
			display("Writing message to " + this.username + ": " + msg.getMessage());
			if(!socket.isConnected()) {
				close();
				return false;
			}
			try {
				sOutput.writeObject(msg);
			}
			catch(IOException e) {
				display("Error sending message to " + username);
				display(e.toString());
			}
			return true;
		}
	}
}

