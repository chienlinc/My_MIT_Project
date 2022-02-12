package Client;

import java.awt.EventQueue;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ClientWindow {

	private static JFrame frame;
	public Graphics2D g; 
	private JTextField textField;
	
	private static JLabel lblWhoIsDrawing;
	private static JLabel lblName;
	private static JLabel lblStatus;
	
	private static String hostname = "localhost";
	private static int port = 10000;

	public static Client client;
	public static int numDrawing;
	private static String username;
	private DataInputStream dis;
	private DataOutputStream dos;
	private static ArrayList<String> before = new ArrayList<String>();

	public Draw d;
	public static boolean available = true;
	private JPanel panel;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientWindow window = new ClientWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		if (args.length != 2) {
			System.out.println("Use the default setting ...");
		} else {
			hostname = args[0];
			port = Integer.parseInt(args[1]);
		}

        try{ 
              
            username = JOptionPane.showInputDialog("Please enter your username");
            
            Socket s = new Socket("localhost", port); 
            
            if (username == null) {
            	username = Integer.toString(s.getLocalPort());
            	JOptionPane.showMessageDialog(null, "Port number is used as username");
            }
            
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
            
            dos.writeUTF(username);
            System.out.println("Username: "+username);
            
            String validUsername = dis.readUTF();
            if(validUsername.contentEquals("Invalid")) {
            	System.out.println("Invaild username");
            	JOptionPane.showMessageDialog(null, "Username has been taken");
            	available = false;
            	client = new Client(username, dis, dos, s, frame, lblWhoIsDrawing, lblName, lblStatus);
            	lblStatus.setText("Disconnected");
            	System.out.println("Disconnected");
            } else {
	        	String result = dis.readUTF();
	        	System.out.println("Access Permission: "+result);
	            
	            if(result.contentEquals("Approved")) {
	            	System.out.println("Before: ");
	            	numDrawing = Integer.parseInt(dis.readUTF());
	                System.out.println(numDrawing);
	                for(int i = 0; i < numDrawing; i++) {
	                	String d = dis.readUTF();
	                	System.out.println(d);
	                	before.add(d);
	                }

	                client = new Client(username, dis, dos, s, frame, lblWhoIsDrawing, lblName, lblStatus);
	                
	                for (String i: before) {
	                	client.drawPrevious(i);
	                }
	            	
	            }else {
	            	JOptionPane.showMessageDialog(null, "Sorry, you were rejected");
	            	client = new Client(username, dis, dos, s, frame, lblWhoIsDrawing, lblName, lblStatus);
	            	System.out.println("Disconnected");
	            	lblStatus.setText("Disconnected");
	            }
            	
            }
        }catch(ConnectException e) {
        	JOptionPane.showMessageDialog(null, "NOT AVAILABLE, please check hostname and port or the server has not started yet");
        	available = false;
        	System.out.println("Disconnected");
        	lblStatus.setText("Disconnected");

        }catch(Exception e){ 
            e.printStackTrace(); 
        }
	}

	/**
	 * Create the application.
	 */
	public ClientWindow() {
		frame = new JFrame();
		frame.setTitle("Client");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 2;
		gbc_lblName.gridy = 1;
		frame.getContentPane().add(lblName, gbc_lblName);
		
		lblWhoIsDrawing = new JLabel("Who is drawing");
		GridBagConstraints gbc_lblHi = new GridBagConstraints();
		gbc_lblHi.insets = new Insets(0, 0, 5, 0);
		gbc_lblHi.gridx = 9;
		gbc_lblHi.gridy = 1;
		frame.getContentPane().add(lblWhoIsDrawing, gbc_lblHi);
		
		lblStatus = new JLabel("Status");
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.insets = new Insets(0, 0, 5, 5);
		gbc_lblStatus.gridx = 2;
		gbc_lblStatus.gridy = 2;
		frame.getContentPane().add(lblStatus, gbc_lblStatus);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.gridx = 9;
		gbc_textField.gridy = 2;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(available) {
					client.disconnect();
				}else {
					JOptionPane.showMessageDialog(null, "NOT AVAILABLE, please check hostname and port or the server has not started yet");
				}
			}
		});
		
		JButton btnRed = new JButton("Red");
		btnRed.setIcon(new ImageIcon(getClass().getResource("/images/Red.png")));
		btnRed.setPreferredSize(new Dimension(20, 20));
		btnRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(available) {
					client.setColor("RED");
				}else {
					JOptionPane.showMessageDialog(null, "NOT AVAILABLE, please check hostname and port or the server has not started yet");
				}
			}
		});
		GridBagConstraints gbc_btnRed = new GridBagConstraints();
		gbc_btnRed.insets = new Insets(0, 0, 5, 5);
		gbc_btnRed.gridx = 2;
		gbc_btnRed.gridy = 4;
		frame.getContentPane().add(btnRed, gbc_btnRed);
		
		JButton btnBlue = new JButton("Blue");
		btnBlue.setIcon(new ImageIcon(getClass().getResource("/images/Blue.png")));
		btnBlue.setPreferredSize(new Dimension(20, 20));
		btnBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(available) {
					client.setColor("BLUE");
				}else {
					JOptionPane.showMessageDialog(null, "NOT AVAILABLE, please check hostname and port or the server has not started yet");
				}
			}
		});
		
		JButton btnThin = new JButton();
		btnThin.setIcon(new ImageIcon(getClass().getResource("/images/Thin.png")));
		btnThin.setPreferredSize(new Dimension(30, 30));
		btnThin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(available) {
					client.setStroke(1);
				}else {
					JOptionPane.showMessageDialog(null, "NOT AVAILABLE, please check hostname and port or the server has not started yet");
				}
			}
		});
		
		GridBagConstraints gbc_btnThin = new GridBagConstraints();
		gbc_btnThin.insets = new Insets(0, 0, 5, 5);
		gbc_btnThin.gridx = 3;
		gbc_btnThin.gridy = 4;
		frame.getContentPane().add(btnThin, gbc_btnThin);

		GridBagConstraints gbc_btnBlue = new GridBagConstraints();
		gbc_btnBlue.insets = new Insets(0, 0, 5, 5);
		gbc_btnBlue.gridx = 2;
		gbc_btnBlue.gridy = 5;
		frame.getContentPane().add(btnBlue, gbc_btnBlue);
		
		JButton btnBlack = new JButton("Black");
		btnBlack.setIcon(new ImageIcon(getClass().getResource("/images/Black.png")));
		btnBlack.setPreferredSize(new Dimension(20, 20));
		btnBlack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(available) {
					client.setColor("BLACK");
				}else {
					JOptionPane.showMessageDialog(null, "NOT AVAILABLE, please check hostname and port or the server has not started yet");
				}
			}
		});
		
		JButton btnMid = new JButton();
		btnMid.setIcon(new ImageIcon(getClass().getResource("/images/Mid.png")));
		btnMid.setPreferredSize(new Dimension(30, 30));
		btnMid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(available) {
					client.setStroke(4);
				}else {
					JOptionPane.showMessageDialog(null, "NOT AVAILABLE, please check hostname and port or the server has not started yet");
				}
			}
		});
		GridBagConstraints gbc_btnMid = new GridBagConstraints();
		gbc_btnMid.insets = new Insets(0, 0, 5, 5);
		gbc_btnMid.gridx = 3;
		gbc_btnMid.gridy = 5;
		frame.getContentPane().add(btnMid, gbc_btnMid);
		
		GridBagConstraints gbc_btnBlack = new GridBagConstraints();
		gbc_btnBlack.insets = new Insets(0, 0, 5, 5);
		gbc_btnBlack.gridx = 2;
		gbc_btnBlack.gridy = 6;
		frame.getContentPane().add(btnBlack, gbc_btnBlack);
		
		JButton btnThick = new JButton();
		btnThick.setIcon(new ImageIcon(getClass().getResource("/images/Thick.png")));
		btnThick.setPreferredSize(new Dimension(30, 30));
		btnThick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(available) {
					client.setStroke(7);
				}else {
					JOptionPane.showMessageDialog(null, "NOT AVAILABLE, please check hostname and port or the server has not started yet");
				}
			}
		});
		GridBagConstraints gbc_btnThick = new GridBagConstraints();
		gbc_btnThick.insets = new Insets(0, 0, 5, 5);
		gbc_btnThick.gridx = 3;
		gbc_btnThick.gridy = 6;
		frame.getContentPane().add(btnThick, gbc_btnThick);
		
		GridBagConstraints gbc_btnDisconnect = new GridBagConstraints();
		gbc_btnDisconnect.insets = new Insets(0, 0, 5, 5);
		gbc_btnDisconnect.gridx = 2;
		gbc_btnDisconnect.gridy = 7;
		frame.getContentPane().add(btnDisconnect, gbc_btnDisconnect);

		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 9;
		gbc_panel.gridy = 8;
		frame.getContentPane().add(panel, gbc_panel);
		panel.setBackground(Color.white);
		
		JButton btnCircle = new JButton("Circle");		
		btnCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(available) {
					d = new Draw("Circle", frame, "", client);
					for(int i = 0; i < frame.getMouseListeners().length; i++ ) {
						frame.removeMouseListener(frame.getMouseListeners()[i]);
					}
					frame.addMouseListener(d);
					frame.addMouseMotionListener(d);
				}else {
					JOptionPane.showMessageDialog(null, "NOT AVAILABLE, please check hostname and port or the server has not started yet");
				}
			}
		});
		GridBagConstraints gbc_btnCircle = new GridBagConstraints();
		gbc_btnCircle.insets = new Insets(0, 0, 5, 0);
		gbc_btnCircle.gridx = 9;
		gbc_btnCircle.gridy = 6;
		frame.getContentPane().add(btnCircle, gbc_btnCircle);

		JButton btnLine = new JButton("Line");
		btnLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(available) {
					d = new Draw("Line", frame, "", client);
					for(int i = 0; i < frame.getMouseListeners().length; i++ ) {
						frame.removeMouseListener(frame.getMouseListeners()[i]);
					}
					frame.addMouseListener(d);
					frame.addMouseMotionListener(d);
				}else {
					JOptionPane.showMessageDialog(null, "NOT AVAILABLE, please check hostname and port or the server has not started yet");
				}
			}
		});
		GridBagConstraints gbc_btnLine = new GridBagConstraints();
		gbc_btnLine.insets = new Insets(0, 0, 5, 0);
		gbc_btnLine.gridx = 9;
		gbc_btnLine.gridy = 4;
		frame.getContentPane().add(btnLine, gbc_btnLine);
		
		JButton btnRectangle = new JButton("Rectangle");
		btnRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(available) {
					d = new Draw("Rectangle", frame, "", client);
					for(int i = 0; i < frame.getMouseListeners().length; i++ ) {
						frame.removeMouseListener(frame.getMouseListeners()[i]);
					}
					frame.addMouseListener(d);
					frame.addMouseMotionListener(d);
				}else {
					JOptionPane.showMessageDialog(null, "NOT AVAILABLE, please check hostname and port or the server has not started yet");
				}
			}
		});
		GridBagConstraints gbc_btnRectangle = new GridBagConstraints();
		gbc_btnRectangle.insets = new Insets(0, 0, 5, 0);
		gbc_btnRectangle.gridx = 9;
		gbc_btnRectangle.gridy = 5;
		frame.getContentPane().add(btnRectangle, gbc_btnRectangle);

		JButton btnText = new JButton("Text");
		btnText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(available) {
					String text = textField.getText();
					Draw d = new Draw("Text", frame, text, client);
					for(int i = 0; i < frame.getMouseListeners().length; i++ ) {
						frame.removeMouseListener(frame.getMouseListeners()[i]);
					}
					frame.addMouseListener(d);
					frame.addMouseMotionListener(d);
				}else {
					JOptionPane.showMessageDialog(null, "NOT AVAILABLE, please check hostname and port or the server has not started yet");
				}
			}
		});
		GridBagConstraints gbc_btnText = new GridBagConstraints();
		gbc_btnText.insets = new Insets(0, 0, 5, 0);
		gbc_btnText.gridx = 9;
		gbc_btnText.gridy = 3;
		frame.getContentPane().add(btnText, gbc_btnText);
		
		JButton btnEraser = new JButton("Eraser");
		btnEraser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(available) {
					d = new Draw("Eraser", frame, "", client);
					for(int i = 0; i < frame.getMouseListeners().length; i++ ) {
						frame.removeMouseListener(frame.getMouseListeners()[i]);
					}
					frame.addMouseListener(d);
					frame.addMouseMotionListener(d);
				}else {
					JOptionPane.showMessageDialog(null, "NOT AVAILABLE, please check hostname and port or the server has not started yet");
				}
			}
		});
		GridBagConstraints gbc_btnEraser = new GridBagConstraints();
		gbc_btnEraser.insets = new Insets(0, 0, 5, 0);
		gbc_btnEraser.gridx = 9;
		gbc_btnEraser.gridy = 7;
		frame.getContentPane().add(btnEraser, gbc_btnEraser);
	}
}
