package Client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Client {
	
	public String username;
	public DataInputStream dis;
	public DataOutputStream dos;
	public Socket s;
	public String drawing = null;
	public Frame UI;
	public int x1, x2, y1, y2;
	public boolean disconnect = false;
	public String otheruser = "no user drawing";
	public ClientWindow c;
	public JLabel l;
	public JLabel lName;
	public JLabel lStatus;
	public String color = "BLACK";
	public int stroke = 1;
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public void setStroke(int stroke) {
		this.stroke = stroke;
	}
	
	public void disconnect() {
		try {
			dos.writeUTF("Exit");
			dis.close();
			dos.close();
			s.close();
			lStatus.setText("Disconnected");
			this.disconnect = true;
			System.out.println("Disconnected");
		}catch(Exception e){
		}	
	}
	
	public void newDraw() {
		try {
			dos.writeUTF("New");
			System.out.println("New Draw");
			this.UI.repaint();
		}catch(Exception e){	
		}
	}
	
	public Client(String username, DataInputStream dis, DataOutputStream dos, Socket s, Frame frame, JLabel l, JLabel lName, JLabel lStatus) {
		this.username = username;
		this.dis = dis;
		this.dos = dos;
		this.s = s;
		this.UI = frame;
		this.l = l;
		this.lName = lName;
		this.lName.setText(this.username);
		this.lStatus = lStatus;
		this.lStatus.setText("Connected");
	}
	
	public void setDrawing(int curX, int curY, int stopX, int stopY, String method, Frame UI, String text, int diameter, int width, int height, String color, int stroke) {
		String all = Integer.toString(curX)+":"+Integer.toString(curY)+":"+Integer.toString(stopX)+":"+Integer.toString(stopY)+":"+method+":"+text+":"+Integer.toString(diameter)+":"+Integer.toString(width)+":"+Integer.toString(height)+":"+color+":"+Integer.toString(stroke);
		System.out.println(all);
		this.drawing = all;
	}
	
	public void sendData() throws IOException {
		dos.writeUTF(this.drawing);
		this.drawing = null;
		l.setText(this.username+" is drawing");
	}
	
	public void drawPrevious(String msg) {
		String[] drawInfo = msg.split(":");
		Graphics2D g = (Graphics2D) UI.getGraphics();
		
		if(drawInfo[9].contentEquals("RED")) {
			g.setColor(Color.RED);
		}else if(drawInfo[9].contentEquals("BLACK")) {
			g.setColor(Color.BLACK);
			
		}else if(drawInfo[9].contentEquals("BLUE")) {
			g.setColor(Color.BLUE);
		}else if(drawInfo[9].contentEquals("WHITE")) {
			g.setColor(Color.WHITE);
			g.setStroke(new BasicStroke(10));
		}
		
		if(drawInfo[10].contentEquals("1")) {
			g.setStroke(new BasicStroke(1));
		}else if(drawInfo[10].contentEquals("4")) {
			g.setStroke(new BasicStroke(4));
		}else if(drawInfo[10].contentEquals("7")) {
			g.setStroke(new BasicStroke(7));
		}
		
		if(drawInfo[4].contentEquals("Circle")) {
			g.drawOval(Integer.parseInt(drawInfo[0]), Integer.parseInt(drawInfo[1]), Integer.parseInt(drawInfo[6]), Integer.parseInt(drawInfo[6]));
		}else if(drawInfo[4].contentEquals("Line")) {
			g.drawLine(Integer.parseInt(drawInfo[0]), Integer.parseInt(drawInfo[1]), Integer.parseInt(drawInfo[2]), Integer.parseInt(drawInfo[3]));
			
		}else if(drawInfo[4].contentEquals("Rectangle")) {
			g.drawRect(Integer.parseInt(drawInfo[0]), Integer.parseInt(drawInfo[1]), Integer.parseInt(drawInfo[7]), Integer.parseInt(drawInfo[8]));
			
		}else if(drawInfo[4].contentEquals("Text")) {
			g.drawString(drawInfo[5],Integer.parseInt(drawInfo[0]), Integer.parseInt(drawInfo[1]));
			
		}else if(drawInfo[4].contentEquals("Eraser")) {
			g.drawLine(Integer.parseInt(drawInfo[0]), Integer.parseInt(drawInfo[1]), Integer.parseInt(drawInfo[2]), Integer.parseInt(drawInfo[3]));
		}
	}
	
	Thread readMessage = new Thread() {
		public void run() {
			while(true) {
				String msg;
				try {
					
					msg = dis.readUTF();
					System.out.println(msg);
					
					String[] all = msg.split("-");
					String[] drawInfo = all[0].split(":");
					String otheruser = all[1];
								
					Graphics2D g = (Graphics2D) UI.getGraphics();
					l.setText(otheruser+" is drawing");
					
					if(drawInfo[9].contentEquals("RED")) {
						g.setColor(Color.RED);
					}else if(drawInfo[9].contentEquals("BLACK")) {
						g.setColor(Color.BLACK);
						
					}else if(drawInfo[9].contentEquals("BLUE")) {
						g.setColor(Color.BLUE);
					}else if(drawInfo[9].contentEquals("WHITE")) {
						g.setColor(Color.WHITE);
						g.setStroke(new BasicStroke(10));
					}
					
					if(drawInfo[10].contentEquals("1")) {
						g.setStroke(new BasicStroke(1));
					}else if(drawInfo[10].contentEquals("4")) {
						g.setStroke(new BasicStroke(4));
					}else if(drawInfo[10].contentEquals("7")) {
						g.setStroke(new BasicStroke(7));
					}
					
					if(drawInfo[4].contentEquals("Circle")) {
						g.drawOval(Integer.parseInt(drawInfo[0]), Integer.parseInt(drawInfo[1]), Integer.parseInt(drawInfo[6]), Integer.parseInt(drawInfo[6]));
					}
					else if(drawInfo[4].contentEquals("EXIT")) {					
						JOptionPane.showMessageDialog(null, otheruser+" has left");
					}
					else if(drawInfo[4].contentEquals("KICK")) {
						JOptionPane.showMessageDialog(null, "You were kicked out");
						lStatus.setText("Disconnected");
						System.out.println("Disconnected");
					}
					else if(drawInfo[4].contentEquals("Line")) {
						g.drawLine(Integer.parseInt(drawInfo[0]), Integer.parseInt(drawInfo[1]), Integer.parseInt(drawInfo[2]), Integer.parseInt(drawInfo[3]));	
					}
					else if(drawInfo[4].contentEquals("ManagerEXIT")) {
						JOptionPane.showMessageDialog(null, "Manager has left");
						lStatus.setText("Disconnected");
						System.out.println("Disconnected");
					}
					else if(drawInfo[4].contentEquals("NewDraw")) {
						UI.repaint();
						JOptionPane.showMessageDialog(null, "Manager cleaned all drawings!");
					}
					else if(drawInfo[4].contentEquals("Rectangle")) {
						g.drawRect(Integer.parseInt(drawInfo[0]), Integer.parseInt(drawInfo[1]), Integer.parseInt(drawInfo[7]), Integer.parseInt(drawInfo[8]));
						
					}
					else if(drawInfo[4].contentEquals("Text")) {
						g.drawString(drawInfo[5],Integer.parseInt(drawInfo[0]), Integer.parseInt(drawInfo[1]));
						
					}
					else if(drawInfo[4].contentEquals("Eraser")) {
						g.drawLine(Integer.parseInt(drawInfo[0]), Integer.parseInt(drawInfo[1]), Integer.parseInt(drawInfo[2]), Integer.parseInt(drawInfo[3]));
					}
				} catch(SocketException e) {
					
				} catch(EOFException e) {
					
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  catch (Exception e) {
					
				}
				
			}
		}
	};
	
	{
	readMessage.start();}
}
