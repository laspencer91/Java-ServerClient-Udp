package server;

import shared.statics.enums.ConnectionStatus;

public class Connection {

    private ConnectionStatus status;

    private int connectionId;

    public Connection(int id) {
        status = ConnectionStatus.PENDING;
        connectionId = id;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public ConnectionStatus getStatus() {
        return status;
    }
}
