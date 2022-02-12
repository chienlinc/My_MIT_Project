package Server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ServerWindow {

	private static String hostname = "localhost";
	private static int port = 9005;
	public static Server server = new Server();;
	
	private JFrame frame;
	public static DefaultListModel<String> model = new DefaultListModel<String>();
	private JList<String> list = new JList<String>(model);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.out.println("Use the default setting ...");
		} else {
			hostname = args[0];
			port = Integer.parseInt(args[1]);
		}
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerWindow window = new ServerWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		server.launch(port, model);
	}

	/**
	 * Create the application.
	 */
	public ServerWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		
		frame.setTitle("Server");
		frame.setSize(600, 600);
		
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		
		JLabel lblNewLabel = new JLabel("Username");
		panel.add(lblNewLabel);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				model.remove(index);
				try {
					server.ar.get(index).dos.writeUTF("0:0:0:0:KICK::0:0:0:BLACK:10"+"-"+"name");
					
					server.ar.get(index).s.close();
					String name = server.ar.get(index).username;
					server.ar.remove(index);
					
					for (ClientHandler mc: Server.ar) {
	 					if(!(mc.username.equals(name))) {
	 						String exitMessage = "0:0:0:0:EXIT::0:0:0:BLACK:10"+"-"+ name;
	 						mc.dos.writeUTF(exitMessage);
	 					}
	 				}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel.add(btnDelete);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		
		panel_1.add(list);
		
		JScrollPane jsp =new JScrollPane(list);
		frame.getContentPane().add(jsp);
		
	}

}
