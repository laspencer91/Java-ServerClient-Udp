package shared.Messages.Unreliable;

import shared.Messages.Types.MessageData;
import com.jfastnet.messages.Message;

/**
 * No-Param constructor is required.
 * @param <T> Type of
 */
public abstract class UnreliableMessage<T extends MessageData> extends Message<T> {

    private T messageData;

    public UnreliableMessage() {}

    public UnreliableMessage(T data) {
        this.messageData = data;
    }

    @Override
    public ReliableMode getReliableMode() { return ReliableMode.UNRELIABLE; }

    @Override
    public void process(MessageData context) {
        processReceivedMessage(messageData);
    }

    protected abstract void processReceivedMessage(T message);
}