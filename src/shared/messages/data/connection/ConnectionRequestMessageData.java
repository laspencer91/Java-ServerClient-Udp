package shared.messages.types.connection;

import shared.exceptions.NetworkWriterException;
import shared.messages.types.IMessageData;
import shared.serialization.NetworkReader;
import shared.serialization.NetworkWriter;

// FROM CLIENT
public class ConnectionRequestMessageData implements IMessageData {

    // This padding of 500 bytes is used to thwart DDOS attacks by exploiting the connection process. Other than that
    // they are completely meaningless
    public static final byte[] connectionMessagePadding = new byte[500];

    public String userName;

    public ConnectionRequestMessageData(String userName) {
        userName = userName;
    }

    @Override
    public void serialize(NetworkWriter writer) {
        try {
            writer.write(userName);
            writer.write(connectionMessagePadding);
        } catch (NetworkWriterException e) {
            System.out.println("[ERROR][IMessageData Serialize]: " + e);
        }
    }

    @Override
    public void deserialize(NetworkReader reader) {
        userName = reader.readString();
    }
}
