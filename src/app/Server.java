package app;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ServerFrame view=new ServerFrame();
		
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

class ServerFrame extends JFrame{
	private class Thread_Between implements Runnable{
		public void run() {
			try {
				ServerSocket serverListener=new ServerSocket(9090);
				while(true) {
					Socket connection=serverListener.accept();
					ObjectInputStream input=new ObjectInputStream(connection.getInputStream());
					PackData data=(PackData)input.readObject();
					textField.append("\n"+"- From '"+data.getFromU()+"' to '"+data.getTo()+"'");
					Socket sendData=new Socket("",getToUser(data.getTo()));
					ObjectOutputStream outObj=new ObjectOutputStream(sendData.getOutputStream());
					outObj.writeObject(data);
					outObj.close();
					sendData.close();
					input.close();
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public int getToUser(String Key){
		for(Map.Entry<String, Integer>it:Users.entrySet()) {
			if(it.getKey().equals(Key)) return it.getValue();
		}
		return -1; 
	}
	private HashMap<String,Integer>Users; 
	public ServerFrame(){
		
		setBounds(600,300,280,350);				
			
		JPanel panel= new JPanel();
		
		panel.setLayout(new BorderLayout());
		
		textField=new JTextArea();
		textField.setBackground(Color.black);
		textField.setForeground(Color.white);
		textField.setEditable(false);
		JScrollPane scroll=new JScrollPane(textField);
		
		panel.add(scroll,BorderLayout.CENTER);
		
		add(panel);
		
		Users=new HashMap<String,Integer>();
		
		setVisible(true);
		
		Thread OnOff=new Thread(new ThreadON_OFF());
		OnOff.start();
		
		Thread communication=new Thread(new Thread_Between());
		communication.start();
		
	}
	private class ThreadON_OFF implements Runnable{ // set online / offline user
		public void run() {
			try {
				ServerSocket server=new ServerSocket(7777);
				while(true) {
					ArrayList<String>NewUsers=new ArrayList<String>();
					Socket connection=server.accept();
					ObjectInputStream in=new ObjectInputStream(connection.getInputStream());
					State ObjIn=(State)in.readObject();
					if(ObjIn.getOnOff()) {
						Users.put(ObjIn.getUsername(),ObjIn.getPort());
						textField.append("\n"+ObjIn.getUsername()+"-> ONLINE");
					} else {
						Users.remove(ObjIn.getUsername());
						textField.append("\n"+ObjIn.getUsername()+"-> OFFLINE");
					}
					
					getNewUsers(NewUsers);
					sendNewUsers(NewUsers);
					in.close();
					connection.close();
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void getNewUsers(ArrayList<String>x){
			for(String it:Users.keySet()) {
				x.add(it);	
			}
		}	
		public void sendNewUsers(ArrayList<String>data){
			for(int y:Users.values()) {
				try {
					Socket temporalSock=new Socket("127.0.0.1",y);
					ObjectOutputStream out=new ObjectOutputStream(temporalSock.getOutputStream());
					out.writeObject(data);
					out.close();
					temporalSock.close();
					//close streams
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	private	JTextArea textField;
}