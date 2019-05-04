package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;

public class ConnectionInfo extends JFrame{
	
	private static final long serialVersionUID = 1891198530883212402L;
	private static ConnectionInfo ci = null;
	
	private JPanel mainPanel;
	private DefaultTableModel dtmServer;
	private DefaultTableModel availableConnection;
	private JLabel connectStatus;
	private JButton connect;

	public ConnectionInfo() {
		mainPanel = setupMainPanel();
		this.connectionTable();
		this.availableConnectionTable();
		this.connectToServer();
		add(mainPanel);
		this.createMainWindow();
	}
	
	public static ConnectionInfo getInstance() {
		if (ci==null) {
			ci = new ConnectionInfo();
		}
		return ci;
	}
	
	private void createMainWindow() {
		setTitle("Connection Infomation");
        setVisible(true);
        setSize(500, 350);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
		JTextField address = new JTextField(10);
		JTextField port = new JTextField(5);
		address.setText("127.0.0.1");
		port.setText("888");
		connect = new JButton("Connect");
		JPanel connectToServer = new JPanel();
		JPanel mannualConnect = new JPanel();
		mannualConnect.setLayout(new GridLayout(4,1));
		connectToServer.setLayout(new GridLayout(1,2));
		mannualConnect.add(new JLabel("Address"));
		mannualConnect.add(address);
		mannualConnect.add(new JLabel("Port"));
		mannualConnect.add(port);
		mannualConnect.add(connect);
		connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.getClient().connectTo(address.getText(), port.getText());
			}
        });
		connectStatus = new JLabel("Not Connected");
		mannualConnect.add(connectStatus);
		connectToServer.add(mannualConnect);
		
		JPanel serverStatus = new JPanel();
		JButton resetServerTable = new JButton("Reset Server Table");
		resetServerTable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetAvailabelServer();
			}
        });
		serverStatus.add(new JLabel("Server Port: "+System.getProperty("port")));
		serverStatus.add(resetServerTable);
		connectToServer.add(serverStatus);
		mainPanel.add(connectToServer);
	}
	
	private void availableConnectionTable() {
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(1,1));
		availableConnection = new DefaultTableModel(new String[] {"Available Server", "Port"}, 0);
		JTable serverOutput = new JTable(availableConnection);
		
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
	
	public void setConnectionStatus(String status) {
		connectStatus.setText(status);
	}
}
