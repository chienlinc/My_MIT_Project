package Client;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.lang.*;
import java.net.SocketException;

public class Draw extends MouseAdapter{
	
	private int curX;
	private int curY;
	private int stopX;
	private int stopY;
	private String method;
	public Graphics2D g;
	private String text;
	public Client client;
	public Frame UI;
	
	public Draw(String method, Frame UI, String text, Client client) {
		this.method = method;
		this.g = (Graphics2D) UI.getGraphics();
		this.text = text;
		this.client = client;
		this.UI = UI;
	}
	
	public void mousePressed(MouseEvent e) {
		this.curX = e.getX();
		this.curY = e.getY();	
	}
	
	public void mouseReleased(MouseEvent e) {
		this.stopX = e.getX();
		this.stopY = e.getY();

		String color = client.color;
		if (color.contentEquals("RED")) {
			g.setColor(Color.RED);
		}else if (color.contentEquals("BLUE")) {
			g.setColor(Color.BLUE);
		}else if (color.contentEquals("BLACK")) {
			g.setColor(Color.BLACK);
		}
		
		int stroke = client.stroke;
		if (stroke == 1) {
			g.setStroke(new BasicStroke(1));
		}else if (stroke == 4) {
			g.setStroke(new BasicStroke(4));
		}else if (stroke == 7){
			g.setStroke(new BasicStroke(7));
		}
		
		if(method.equals("Circle")) {
			
			int diameter = Math.abs(this.stopX - this.stopY);
			client.setDrawing(curX, curY, stopX, stopY, method, UI, text, diameter, 0, 0, color, stroke);
			g.drawOval(this.curX, this.curY, diameter, diameter);

			try {
				client.sendData();
			} catch(SocketException e2) {
				
			}catch(NullPointerException e3) {
				
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
		
		else if(method.contentEquals("Line")) {
			g.drawLine(this.curX, this.curY, this.stopX, this.stopY);
			client.setDrawing(curX, curY, stopX, stopY, method, UI, text, 0, 0, 0, color, stroke);
			try {
				client.sendData();
			} catch(SocketException e2) {
				
			}catch(NullPointerException e3) {
				
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(method.contentEquals("Rectangle")) {
			int width = Math.abs(this.stopX-this.curX);
			int height = Math.abs(this.stopY-this.curY);
			g.drawRect(this.curX, this.curY, width, height);
			client.setDrawing(curX, curY, stopX, stopY, method, UI, text, 0, width, height, color, stroke);
			try {
				client.sendData();
			} catch(SocketException e2) {
				
			}catch(NullPointerException e3) {
				
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(method.contentEquals("Text")) {
			g.drawString(this.text, this.curX, this.curY);
			client.setDrawing(curX, curY, stopX, stopY, method, UI, text, 0, 0, 0, color, stroke);
			try {
				client.sendData();
			} catch(SocketException e2) {
				
			}catch(NullPointerException e3) {
				
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		else if(method.contentEquals("Eraser")) {
			g.setColor(Color.WHITE);
			g.drawLine(this.curX, this.curY, this.stopX, this.stopY);
			client.setDrawing(curX, curY, stopX, stopY, method, UI, text, 0, 0, 0, "WHITE", stroke);
			try {
				client.sendData();
			} catch(SocketException e2) {
				
			}catch(NullPointerException e3) {
				
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	

}
