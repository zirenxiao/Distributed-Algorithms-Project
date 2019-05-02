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
import javax.swing.table.DefaultTableModel;

public class ConnectionInfo extends JFrame{
	
	private static final long serialVersionUID = 1891198530883212402L;
	private static ConnectionInfo ci = null;
	
	private JPanel mainPanel;
	private DefaultTableModel dtmServer;
	private JLabel connectStatus;
	private JButton connect;

	public ConnectionInfo() {
		mainPanel = setupMainPanel();
		this.connectionTable();
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
		mainPanel.setLayout(new GridLayout(2,2));
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
		connect = new JButton("Connect");
		JPanel connectToServer = new JPanel();
		JPanel mannualConnect = new JPanel();
//		mannualConnect.setLayout(new GridLayout(2,2));
		connectToServer.setLayout(new GridLayout(2,2));
		mannualConnect.add(new JLabel("ServerAddress"));
		mannualConnect.add(address);
		mannualConnect.add(new JLabel("ServerPort"));
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
		mainPanel.add(connectToServer);
	}
	
	
	public void setConnectStatus(String connectStatus) {
		this.connectStatus.setText(connectStatus);;
	}

	public void setConnectEnable(Boolean connect) {
		this.connect.setEnabled(connect);;
	}

	public void addClientConnection(String address) {
		dtmServer.addRow(new String[] {address});
	}
	
	public void delClientConnection(String address) {
		for (int i=0; i<dtmServer.getRowCount(); i++) {
			if (dtmServer.getValueAt(i, 0).equals(address)) {
				dtmServer.removeRow(i);
				break;
			}
		}
	}
	
	public void setConnectionStatus(String status) {
		connectStatus.setText(status);
	}
}
