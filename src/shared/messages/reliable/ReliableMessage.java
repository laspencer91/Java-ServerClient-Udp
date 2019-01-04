package shared.messages.unreliable;

import shared.messages.NetMessage;
import shared.messages.data.IMessageData;
import shared.statics.enums.MessageSendType;

/**
 * No-Param constructor is required.
 * @param <T> Type of
 */
public abstract class UnreliableMessage<T extends IMessageData> extends NetMessage<T> {

    public UnreliableMessage() {}

    public UnreliableMessage(T data) {
        super(data);
    }

    @Override
    public MessageSendType getMessageSendType() {
        return MessageSendType.UNRELIABLE;
    }

    protected abstract void processReceivedMessage(T message);
}