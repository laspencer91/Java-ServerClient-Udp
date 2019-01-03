package server;

import server.types.enums.ConnectionState;

public class Connection {

    private ConnectionState state;

    private int connectionId;

    public Connection(int id) {
        state = ConnectionState.PENDING;
        connectionId = id;
    }

    public void SetState(ConnectionState state) {
        this.state = state;
    }
}
