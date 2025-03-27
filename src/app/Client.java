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

		String username="";
		while(username.length()==0){
			username=JOptionPane.showInputDialog("Nombre Usuario:");
			if(username==null) System.exit(0);
		}
		frame.Ok(username);
	}

}


class ClientFrame extends JFrame{
	
	private ClientView view;

	public ClientFrame(){
		
		setBounds(600,300,280,390);
				
		view=new ClientView();
		
		
		setResizable(false);
		add(view);
		
		setVisible(true);
		}	
	
	public void Ok(String nick){
		view.Ok(nick);
		ClientView.WindowAction action=view.new WindowAction();
		addWindowListener(action);
	}
}

class ClientView extends JPanel{

	private final int port;
	JComboBox<String> ip;
	JLabel nick;
	JButton clear;
  private JTextArea chatField;	
	private JTextField send;
	private JButton startSend;

	private class newMenus implements Runnable{
		public void run() {
			try {
				ServerSocket clientListener=new ServerSocket(port);
				while(true) {
					Socket connection=clientListener.accept();
					ObjectInputStream inputStream=new ObjectInputStream(connection.getInputStream());
					Object object=inputStream.readObject();
					if(object instanceof PackData) {
						PackData source=(PackData)object;
						String FromU=source.getFromU();
						if(FromU.equals(nick.getText()))FromU+=" (Tú)";
						chatField.append("\n"+FromU+": "+source.getMessage());
					}else {
						ArrayList<String>NewUsers=(ArrayList<String>)object;
						ip.removeAllItems();
						for(String x:NewUsers) {
								ip.addItem(x);
						}
					}
					inputStream.close();
					connection.close();
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
				objOut.writeObject(new State(nick.getText(),0,false));
				objOut.close();
				sockClose.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	public void Ok(String nick){
		startSend.setEnabled(true);
		clear.setEnabled(true);
		this.nick.setText(nick);
		try {
			State setOnline=new State(this.nick.getText(),port,true); 
			Socket sockOn=new Socket("127.0.0.1",7777);
			ObjectOutputStream ObjStream=new ObjectOutputStream(sockOn.getOutputStream()); 
			ObjStream.writeObject(setOnline);
			ObjStream.close();
			sockOn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ClientView(){
		int PortRandom=0;
		boolean findPort=true;
		while(findPort) {
			try {
				PortRandom=(int)(Math.random()*65536);
				Socket temporal=new Socket("",PortRandom);
        temporal.close();
			}catch(Exception e) {
				findPort=false;
			}
		}
		port=PortRandom;
		JLabel shownick=new JLabel("NICK:");
		add(shownick);
		nick=new JLabel("");
		add(nick);
		JLabel text=new JLabel("    |  ONLINE:");
		
		add(text);
		
		ip=new JComboBox<String>(); 
		
		add(ip);
		
		Thread receiveUsers=new Thread(new newMenus());
		receiveUsers.start();
		
		chatField=new JTextArea(12,20);

		chatField.setEditable(false);
		setBackground(new Color(233,246,247));
	
		JScrollPane scroll=new JScrollPane(chatField);
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
				chatField.setText("");
			}
		});
		
		add(vertical);
		startSend.setEnabled(false);
		clear.setEnabled(false);
		startSend.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				chatField.append("\n- Tú: "+send.getText()+"   | "+ip.getSelectedItem());
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
	
}

class PackData implements Serializable{
	private String FromUser,message,ToUser;
	public PackData(String from,String message, String to){
		FromUser=from;
		this.message=message;
		ToUser=to;
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
	public State(String username, int port, boolean state){
		UserName=username;
		Port=port;
		on_off=state;
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