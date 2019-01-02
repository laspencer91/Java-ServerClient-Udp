package shared.Messages.Unreliable;

import shared.Messages.Types.PositionData;

public class PositionUpdateMessage extends UnreliableMessage<PositionData> {

    public PositionUpdateMessage() {}

    public PositionUpdateMessage(PositionData data) {
        super(data);
    }

    @Override
    protected void processReceivedMessage(PositionData message) {
        System.out.println(message.x + ", " + message.y);
    }
}
