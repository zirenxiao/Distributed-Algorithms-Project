package GUI;

import main.Main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

public class ConnectionInfo{
	
	private static final long serialVersionUID = 1891198530883212402L;
	private static ConnectionInfo ci = null;
	
	private JPanel mainPanel;
	private DefaultTableModel dtmServer;
	private DefaultTableModel availableConnection;
	private JLabel connectStatus;
	private JButton connect;
	private JTextField serverAddress;
	private JTextField serverPort;
	private JFrame main;

	public ConnectionInfo() {
		main = new JFrame();
		mainPanel = setupMainPanel();
		this.connectionTable();
		this.availableConnectionTable();
		this.connectToServer();
		main.add(mainPanel);
		this.createMainWindow();
//		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		main.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}
	
	public static ConnectionInfo getInstance() {
		if (ci==null) {
			ci = new ConnectionInfo();
		}
		return ci;
	}
	
	private void createMainWindow() {
		main.setTitle("Connection Infomation");
//		main.setVisible(true);
		main.setSize(500, 350);
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
	
	private JPanel setupMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3,1));
		return mainPanel;
	}
	
	private void connectionTable() {
		JPanel infoPanel = new JPanel();
		dtmServer = new DefaultTableModel(new String[] {"Client Connections"}, 0);
		infoPanel.setLayout(new GridLayout(1,1));
		JTable serverOutput = new JTable(dtmServer);
		JScrollPane scrollPane = new JScrollPane(serverOutput);
		infoPanel.add(scrollPane,BorderLayout.CENTER);
		mainPanel.add(infoPanel);
	}
	
	private void connectToServer() {
		serverAddress = new JTextField(10);
		serverPort = new JTextField(5);
		serverAddress.setText("127.0.0.1");
		serverPort.setText("800");
		connect = new JButton("Connect");
		JPanel connectToServer = new JPanel();
		JPanel mannualConnect = new JPanel();
		mannualConnect.setLayout(new GridLayout(4,1));
		connectToServer.setLayout(new GridLayout(1,2));
		mannualConnect.add(new JLabel("Address"));
		mannualConnect.add(serverAddress);
		mannualConnect.add(new JLabel("Port"));
		mannualConnect.add(serverPort);
		mannualConnect.add(connect);
		connect.addActionListener(e -> connectTo(serverAddress.getText(), serverPort.getText()));
		
		connectToServer.add(mannualConnect);
		
		JPanel serverStatus = new JPanel();
		JButton resetServerTable = new JButton("Reset Server Table");
		resetServerTable.addActionListener(e -> resetAvailabelServer());
		serverStatus.add(new JLabel("Server Port: "+System.getProperty("port")));
		serverStatus.add(resetServerTable);
		connectStatus = new JLabel("Not Connected");
		serverStatus.add(connectStatus);
		connectToServer.add(serverStatus);
		mainPanel.add(connectToServer);
	}
	
	private void connectTo(String address, String port) {
		NotePadGUI.getInstance().setVisible(true);
		// avoid connect to self
		if (getSelfAddress().contains(address)) {
			if (port.equals(System.getProperty("port"))) {
				setConnectStatus("Cannot connect to self");
				return;
			}
//			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
		Main.getClient().connectTo(address, port);
	}
	
	private ArrayList<String> getSelfAddress(){
		ArrayList<String> list = new ArrayList<String>();
		list.add("127.0.0.1");
		list.add("localhost");
		try {
			list.add(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	private void availableConnectionTable() {
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(1,1));
		availableConnection = new DefaultTableModel(new String[] {"Available Server", "Port"}, 0);
		JTable serverOutput = new JTable(availableConnection);
		serverOutput.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int row=serverOutput.rowAtPoint(e.getPoint());
//				int col= serverOutput.columnAtPoint(e.getPoint());
				serverAddress.setText(serverOutput.getValueAt(row, 0).toString());
				serverPort.setText(serverOutput.getValueAt(row, 1).toString());
				
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(serverOutput);
		infoPanel.add(scrollPane,BorderLayout.CENTER);
		mainPanel.add(infoPanel);
	}
	
	public void setConnectStatus(String connectStatus) {
		this.connectStatus.setText(connectStatus);;
	}

	public void setConnectEnable(Boolean connect) {
		this.connect.setEnabled(connect);;
	}

	public void addClientConnection(String address) {
		addToDTM(dtmServer, new String[] {address});
	}
	
	public void delClientConnection(String address) {
		for (int i=0; i<dtmServer.getRowCount(); i++) {
			if (dtmServer.getValueAt(i, 0).equals(address)) {
				dtmServer.removeRow(i);
				break;
			}
		}
	}
	
	public void resetAvailabelServer() {
		for (int i=0; i<availableConnection.getRowCount(); i++) {
			availableConnection.removeRow(i);
		}
	}
	
	public void addAvailableServer(String address, String port) {
		if (!isAvailableServerExist(address, port)) {
			addToDTM(availableConnection, new String[] {address, port});
		}
		
	}
	
	private boolean isAvailableServerExist(String address, String port) {
		for (int i=0; i<availableConnection.getRowCount(); i++) {
			if (availableConnection.getValueAt(i,0).equals(address) && availableConnection.getValueAt(i,1).equals(port)) {
				return true;
			}
		}
		return false;
	}
	
	private void addToDTM(DefaultTableModel dtm, String[] str) {
		dtm.addRow(str);
	}

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		main.setVisible(b);
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return main.isVisible();
	}
}
