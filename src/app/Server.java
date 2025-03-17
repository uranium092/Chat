package app;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

class MarcoServidor extends JFrame{
	private class Thread_Between implements Runnable{ // Recibir y enviar mensaje a destinatario
		public void run() {
			try {
				ServerSocket serverBtt=new ServerSocket(9090);
				while(true) {
					Socket conn=serverBtt.accept();
					ObjectInputStream input=new ObjectInputStream(conn.getInputStream());
					PackData data=(PackData)input.readObject();
					areatexto.append("\n"+"- Conexión: De '"+data.getFromU()+"' para '"+data.getTo()+"'");
					Socket sendData=new Socket("",getToUser(data.getTo()));
					ObjectOutputStream outObj=new ObjectOutputStream(sendData.getOutputStream());
					outObj.writeObject(data);
					outObj.close();
					sendData.close();
					input.close();
					conn.close();
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
	private String nick, ip, mensaje;
	private HashMap<String,Integer>Users; 
	public MarcoServidor(){
		
		setBounds(600,300,280,350);				
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		areatexto.setBackground(Color.black);
		areatexto.setForeground(Color.white);
		areatexto.setEditable(false);
		JScrollPane scroll=new JScrollPane(areatexto);
		
		milamina.add(scroll,BorderLayout.CENTER);
		
		add(milamina);
		
		Users=new HashMap<String,Integer>();
		
		setVisible(true);
		
		Thread OnOff=new Thread(new ThreadON_OFF());
		OnOff.start();
		
		Thread communication=new Thread(new Thread_Between());
		communication.start();
		
		}
	private class ThreadON_OFF implements Runnable{ // registrar usuario online / offline
		public void run() {
			try {
				ServerSocket server=new ServerSocket(7777);
				while(true) {
					ArrayList<String>NewUsers=new ArrayList<String>();
					Socket sock=server.accept();
					ObjectInputStream in=new ObjectInputStream(sock.getInputStream());
					State ObjIn=(State)in.readObject();
					if(ObjIn.getOnOff()) {
						Users.put(ObjIn.getUsername(),ObjIn.getPort());
						areatexto.append("\n"+ObjIn.getUsername()+"-> ONLINE");
					} else {
						Users.remove(ObjIn.getUsername());
						areatexto.append("\n"+ObjIn.getUsername()+"-> OFFLINE");
					}
					
					getNewUsers(NewUsers); //llenar NewUsers
					sendNewUsers(NewUsers);
					in.close();
					sock.close();
					
					
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
		public void sendNewUsers(ArrayList<String>x){
			for(int y:Users.values()) {
				try {
					Socket temporalSock=new Socket("127.0.0.1",y);
					ObjectOutputStream out=new ObjectOutputStream(temporalSock.getOutputStream());
					out.writeObject(x);
					out.close();
					temporalSock.close();
					
					
					//close streams
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	private	JTextArea areatexto;
}