package client;

public class Client {


	private Communication comm;
	private LagDetector ld;
    
    public void connectTo(String host, int port) {
    	ld = new LagDetector();
    	comm = new Communication();
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
	
	public String getHost() {
		return comm.getHost();
	}
	
	public int getPort() {
		return comm.getPort();
	}


}
