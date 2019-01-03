package shared.Messages;

import shared.Messages.Types.IMessageData;
import shared.serialization.NetworkReader;
import shared.serialization.NetworkWriter;

public abstract class NetMessage<T extends IMessageData> {
    T data;

    public NetMessage(T data) {
        this.data = data;
    }

    public void serialize(NetworkWriter writer) {
        // TODO: Write Header
        data.serialize(writer);
    }

    public void deserialize(NetworkReader reader) {
        // TODO: Read Header
        data.deserialize(reader);
    }
}
