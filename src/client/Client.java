package client;

public class Client {


	private Communication comm = new Communication();
	private LagDetector ld;
	
    public Client() {
    	ld = new LagDetector();
    }
    
    public void connectTo(String host, int port) {
    	comm.connect(host, port);
    }
    
    public void connectTo(String host, String port) {
    	connectTo(host, Integer.parseInt(port));
    }
    
	public void sentToServer(String str) {
		if (comm == null) {
			return;
		}
		
		if (comm.isConnected()) {
//			System.out.println("STS:"+str);
			comm.getChannel().writeAndFlush(str);
		}
	}

	public LagDetector getLagDetector() {
		return ld;
	}
	
	public void closeConnection() {
		comm.disconnect("Disconnect");
	}
	


}
