package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class ClientHandler extends Thread{
	final DataInputStream dis; 
	final DataOutputStream dos; 
	final Socket s;
	final String username;
	private DefaultListModel<String> model;


	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, String username, DefaultListModel<String> model){ 
	    this.s = s; 
	    this.dis = dis; 
	    this.dos = dos;
	    this.username = username;
	    this.model = model;
	}
	
	public void run() {
		String received;
		while (true) {
			try {
				
				received = dis.readUTF();
				
				if(received.equals("Exit") && !this.username.contentEquals(Server.managerUsername)) {  
	                 System.out.println("Client " + this.s + " sends exit..."); 
	                 System.out.println("Closing this connection."); 
	                 this.s.close();
	                 Server.ar.remove(this);
	                 System.out.println("Connection closed");
	                       
	                 model.removeElement(this.username);
	                 
	                 for (ClientHandler mc: Server.ar) {
	 					if(!(mc.username.equals(this.username))) {
	 						String exitMessage = "0:0:0:0:EXIT::0:0:0:BLACK:10"+"-"+ this.username;
	 						mc.dos.writeUTF(exitMessage);
	 					}
	 				 }
	                 break;
	                 
	            } else if (received.equals("Exit") && this.username.contentEquals(Server.managerUsername)) {
	            	 System.out.println("Manager " + this.s + " sends exit..."); 
	                 System.out.println("Closing all connections."); 
	                 Server.ar.remove(this);
	                 this.s.close();
	                 model.removeElement(this.username);
	                 
	                 
	                 for (ClientHandler mc: Server.ar) {
	 					String exitMessage = "0:0:0:0:ManagerEXIT::0:0:0:BLACK:10"+"-"+ this.username;
	 					mc.dos.writeUTF(exitMessage);
	 					model.removeElement(mc.username);
	 					mc.s.close();
	 					
	 				 }
	                 
	                 Server.ar.clear();
	                 Server.allDrawing.clear();
	                 Server.allDrawingUsername.clear();
	                 Server.manager = false;
	                 Server.managerUsername = "";
	                 Server.allUsername.clear();
	                 break;
	            	
	            } 
	            
				if (received.equals("New") && this.username.contentEquals(Server.managerUsername)) {
					 System.out.println("Manager clear all drawing");
	                 for (ClientHandler mc: Server.ar) {
	 					String newMessage = "0:0:0:0:NewDraw::0:0:0:BLACK:10"+"-"+ this.username;
	 					mc.dos.writeUTF(newMessage);
	 				 }
	                 
	                 Server.allDrawing.clear();
		             Server.allDrawingUsername.clear();
	                         	
	            }else {

					Server.allDrawing.add(received);
					Server.allDrawingUsername.add(this.username);
					System.out.println(received +"-"+ this.username);
					
					for (ClientHandler mc: Server.ar) {
						if(!(mc.username.equals(this.username))) {
							String username_received = received +"-"+ this.username;
							mc.dos.writeUTF(username_received);
						}
					}
	            	
	            }
				
			}
			catch(Exception e) {
				
			}
		}
		
		try{ 
	         // closing resources 
	         this.dis.close(); 
	         this.dos.close();
	           
	     }catch(IOException e){ 
	         e.printStackTrace(); 
	     }
	}
}
