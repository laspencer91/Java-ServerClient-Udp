package shared.messages.types;

import shared.serialization.NetworkReader;
import shared.serialization.NetworkWriter;

public interface IMessageData {
    void serialize(NetworkWriter writer);
    void deserialize(NetworkReader reader);
}
