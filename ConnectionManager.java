package localchat;

public class ConnectionManager extends MessageSubject {
    private String address = "";
    private int port = 2823;

    private Server server = null;
    private Client client = null;

    public static final String SERVER = "server";
    public static final String CLIENT = "client";
    
    public ConnectionManager(String type, String address, MessageObserver messageObserver) {
        this.address = address;
        
        this.attach(messageObserver);

        if (type.equals(this.SERVER)) {
            this.server = new Server(this.port, this);
        } else {
            this.client = new Client(this.address, this.port, this);
        } 
    }

    public ConnectionManager(String type, String address, int port, MessageObserver messageObserver) {
        this.address = address;
        this.port = port;

        this.attach(messageObserver);

        if (type.equals(this.SERVER)) {
            this.server = new Server(this.port, this);
        } else {
            this.client = new Client(this.address, this.port, this);
        }
    }
}