package server;

public class Client {

    private int id;

    private String ip;

    private int port;

    public Client(int clientId, String ip, int port) {
        this.id = clientId;
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
