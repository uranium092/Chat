package app;
import java.awt.Color;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;


public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientFrame frame=new ClientFrame();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		String n="";
		while(n.length()==0){
			n=JOptionPane.showInputDialog("Nombre Usuario:");
			if(n==null) System.exit(0);
		}
		frame.Ok(n);
	}

}


class ClientFrame extends JFrame{
	
	LaminaMarcoCliente milamina;
	public ClientFrame(){
		
		setBounds(600,300,280,390);
				
		milamina=new LaminaMarcoCliente();
		
		
		setResizable(false);
		add(milamina);
		
		setVisible(true);
		}	
	
	public void Ok(String x){
		milamina.Ok(x);
		LaminaMarcoCliente.WindowAction action=milamina.new WindowAction();
		addWindowListener(action);
	}
}

class LaminaMarcoCliente extends JPanel{
	private final int Port;
	private class newMenus implements Runnable{
		public void run() {
			try {
				ServerSocket serverS=new ServerSocket(Port);
				while(true) {
					Socket sock=serverS.accept();
					ObjectInputStream inp=new ObjectInputStream(sock.getInputStream());
					Object target=inp.readObject();
					if(target instanceof PackData) {
						PackData source=(PackData)target;
						String FromU=source.getFromU();
						if(FromU.equals(nick.getText()))FromU+=" (Tú)";
						campochat.append("\n"+FromU+": "+source.getMessage());
					}else {
						ArrayList<String>NewUsers=(ArrayList<String>)target;
						ip.removeAllItems();
						for(String x:NewUsers) {
								ip.addItem(x);
						}
					}
					inp.close();
					sock.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
	class WindowAction extends WindowAdapter{
		public void windowClosing(WindowEvent e) {
			try {
				Socket sockClose=new Socket("",7777);
				ObjectOutputStream objOut=new ObjectOutputStream(sockClose.getOutputStream());
				objOut.writeObject(new State(nick.getText(),0,false)); //reason
				objOut.close();
				sockClose.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	JComboBox<String> ip;
	JLabel nick;
	JButton clear;
	public void Ok(String nick){
		startSend.setEnabled(true);
		clear.setEnabled(true);
		this.nick.setText(nick);
		try {
			State setOnline=new State(this.nick.getText(),Port,true); 
			Socket sockOn=new Socket("127.0.0.1",7777);//no existe
			ObjectOutputStream ObjStream=new ObjectOutputStream(sockOn.getOutputStream()); 
			ObjStream.writeObject(setOnline);
			ObjStream.close();
			sockOn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LaminaMarcoCliente(){
		int PortRandom=0;
		boolean findPort=true;
		while(findPort) {
			try {
				PortRandom=(int)(Math.random()*65536);
				Socket temporal=new Socket("",PortRandom);
			}catch(Exception e) {
				findPort=false;
			}
		}
		Port=PortRandom;
		JLabel shownick=new JLabel("NICK:");
		add(shownick);
		nick=new JLabel("");
		add(nick);
		JLabel texto=new JLabel("    |  ONLINE:");
		
		add(texto);
		
		ip=new JComboBox<String>(); 
		
		add(ip);
		
		Thread receiveUsers=new Thread(new newMenus());
		receiveUsers.start();
		
		campochat=new JTextArea(12,20);
//		add(campochat);
		campochat.setEditable(false);
		setBackground(new Color(233,246,247));
	
		JScrollPane scroll=new JScrollPane(campochat);
		add(scroll);
		
		send=new JTextField(20);
	
		add(send);		
	
		startSend=new JButton("Enviar");
		clear=new JButton("Vaciar");
		Box vertical=Box.createVerticalBox();
		vertical.add(startSend);
		vertical.add(new JLabel(" "));
		vertical.add(clear);
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				campochat.setText("");
			}
		});
		
		add(vertical);
		startSend.setEnabled(false);
		clear.setEnabled(false);
		startSend.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				campochat.append("\n- Tú: "+send.getText()+"   | "+ip.getSelectedItem());
				if(ip.getSelectedItem()!=null){
					try {
					Socket sendMess=new Socket("127.0.0.1",9090);
					ObjectOutputStream liver=new ObjectOutputStream(sendMess.getOutputStream());
					liver.writeObject(new PackData(nick.getText(),send.getText(),(String)ip.getSelectedItem())); 
					liver.close();
					sendMess.close();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				send.setText("");
			}
			
		});
		
	}
	
		
	private JTextArea campochat;	
		
	private JTextField send;
	
	private JButton startSend;
	
	
}

class PackData implements Serializable{
	private String FromUser,message,ToUser;
	public PackData(String a,String b, String c){
		FromUser=a;
		message=b;
		ToUser=c;
	}
	public String getFromU() {
		return FromUser;
	}
	public String getMessage() {
		return message;
	}
	public String getTo() {
		return ToUser;
	}
}


class State implements Serializable{
	private String UserName;
	private final int Port;
	private boolean on_off;
	public State(String a, int b, boolean c){
		UserName=a;
		Port=b;
		on_off=c;
	}
	public String getUsername() {
		return UserName;
	}
	public int getPort() {
		return Port;
	}
	public boolean getOnOff() {
		return on_off;
	}
}