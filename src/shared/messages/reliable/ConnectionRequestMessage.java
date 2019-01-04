package shared.messages.reliable;

import shared.messages.NetMessageId;
import shared.messages.data.connection.ConnectionRequestMessageData;

public class ConnectionRequestMessage extends ReliableMessage<ConnectionRequestMessageData> {

    public ConnectionRequestMessage(ConnectionRequestMessageData data) {
        super(data);
    }

    @Override
    protected void processReceivedMessage(ConnectionRequestMessageData message) {

    }

    @Override
    public NetMessageId getIdentifier() {
        return NetMessageId.CONNECTION_REQUEST;
    }
}
