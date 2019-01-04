package shared.messages;

import lombok.NoArgsConstructor;
import shared.messages.data.IMessageData;
import shared.serialization.NetworkReader;
import shared.serialization.NetworkWriter;

@NoArgsConstructor
public class PacketHeaderData implements IMessageData {

    public int protocolId;

    public short sequenceNumber;

    public PacketHeaderData(int protocolId) {
        this.protocolId = protocolId;
    }

    public PacketHeaderData(int protocolId, short sequenceNumber) {
        this.protocolId = protocolId;
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public void serialize(NetworkWriter writer) {
        writer.write(protocolId);
        writer.write(sequenceNumber);
    }

    @Override
    public void deserialize(NetworkReader reader) {
        protocolId = reader.readInt();
        sequenceNumber = reader.readShort();
    }
}
