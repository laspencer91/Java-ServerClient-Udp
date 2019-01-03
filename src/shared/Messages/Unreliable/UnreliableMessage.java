package shared.Messages.Unreliable;

import shared.Messages.NetMessage;
import shared.Messages.Types.BaseMessageData;

/**
 * No-Param constructor is required.
 * @param <T> Type of
 */
public abstract class UnreliableMessage<T extends BaseMessageData> extends NetMessage<T> {

    public UnreliableMessage() {}

    public UnreliableMessage(T data) {
        super(data);
    }

    protected abstract void processReceivedMessage(T message);
}