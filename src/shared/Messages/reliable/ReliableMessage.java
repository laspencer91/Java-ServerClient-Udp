package shared.messages.reliable;

import shared.messages.NetMessage;
import shared.messages.data.IMessageData;
import shared.statics.enums.MessageSendType;

/**
 * No-Param constructor is required.
 * @param <T> Type of
 */
public abstract class ReliableMessage<T extends IMessageData> extends NetMessage<T> {

    public ReliableMessage() {}

    public ReliableMessage(T data) {
        super(data);
    }

    @Override
    protected MessageSendType getMessageSendType() {
        return MessageSendType.RELIABLE;
    }

    protected abstract void processReceivedMessage(T message);
}