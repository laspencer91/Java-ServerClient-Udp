package server;

import java.util.concurrent.ConcurrentHashMap;

public class ServerConnectionHandler<T> {

    // Thread safe Hash Map Containing a mapping from a clients' connection string to a Client Object
    private ConcurrentHashMap<T, Client> connections = new ConcurrentHashMap<>();

    public Client getConnectedClient(T key) {
        return connections.get(key);
    }

    public void addNewConnection(T key, int clientId, String ip, int port) {
        connections.put(key, new Client(clientId, ip, port));
    }

    public boolean containsConnection(T key) {
        return connections.containsKey(key);
    }
}
