package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class Server {
	public static ArrayList<String> allUsername = new ArrayList<String>();
	public static ArrayList<String> allDrawing = new ArrayList<String>();
	public static ArrayList<String> allDrawingUsername = new ArrayList<String>();
	public static boolean manager = false;
	public static String managerUsername;
	
	public static ArrayList<ClientHandler> ar = new ArrayList<ClientHandler>();

	public void launch(int port, DefaultListModel model) {
		try {
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Server launch");
			
			while(true) {
				
				Socket s = ss.accept();
				System.out.println(s);
				
				DataInputStream dis = new DataInputStream(s.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());           
         
                String username = dis.readUTF();
                
                if(allUsername.contains(username)) {
                	dos.writeUTF("Invalid");
                }else {
                	dos.writeUTF("Valid");
                	
                	int result = JOptionPane.showConfirmDialog(null, username + " want to join", "Access Permission", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION) {
                    	dos.writeUTF("Approved");
                    	
                    	allUsername.add(username);
        				System.out.println("username: "+ username);
        				
        				if(!manager) {
                        	this.manager = true;
                        	this.managerUsername = username;
                        	System.out.println("Manager: "+managerUsername);
                        	
                        }
        				
        				model.addElement(username);
        				
        				String allDrawingSize = Integer.toString(allDrawing.size());
        				dos.writeUTF(allDrawingSize);
        				
        				for(int i = 0; i < allDrawing.size(); i++) {
        					dos.writeUTF(allDrawing.get(i));
        				}
        				
                        ClientHandler t = new ClientHandler(s, dis, dos, username, model);
                        t.start();
                        
                        ar.add(t);
                        
                    }else {
                    	dos.writeUTF("Rejected");
                    }
                }
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
