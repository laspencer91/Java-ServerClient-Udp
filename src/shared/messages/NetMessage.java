package shared.messages;

import com.google.common.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.messages.data.IMessageData;
import shared.serialization.NetworkReader;
import shared.serialization.NetworkWriter;
import shared.statics.NetworkProtocol;
import shared.statics.enums.MessageSendType;

/**
 * NetMessage is what all  NetworkMessages should inherit from. It handles serialization and deserialization
 * of its messages.
 * @param <T> Type of IMessageData that this message should holds
 */
public abstract class NetMessage<T extends IMessageData> {

    private static Logger LOGGER = LogManager.getLogger(NetMessage.class);

    private NetMessageId messageIdentifier;

    private Class dataClass = new TypeToken<T>(getClass()) { }.getRawType();

    public PacketHeaderData header = new PacketHeaderData(NetworkProtocol.PROTOCOL_ID);

    public final MessageSendType TYPE = getMessageSendType();

    protected T payload;

    public NetMessage() {
    }

    public NetMessage(T payload) {
        this.payload = payload;
        messageIdentifier = getIdentifier();
    }

    public void serialize(NetworkWriter writer) {
        header.serialize(writer);

        writer.write(messageIdentifier.shortValue());
        payload.serialize(writer);
    }

    public <B extends IMessageData> void deserialize(NetworkReader reader, Class<B> messageType) throws IllegalAccessException, InstantiationException {
        // READ HEADER DATA
        header.deserialize(reader);

        // READ PAYLOAD DATA INTO THIS MESSAGE
        B messageData = messageType.newInstance();
        payload = (T) messageData;
        payload.deserialize(reader);
    }

    /**
     * Get the payload that this class is housing
     * @return The payload
     */
    public T getPayload() {
        return payload;
    }

    /**
     * Get the class of the payload this is housing
     * @return The class of the payload
     */
    public Class getDataClass() {
        return dataClass;
    }

    /**
     * Using payload stored in a reader return a NetMessage that it contains.
     * @param reader The reader to read from
     * @param classType The NetMessage subtype class to generate
     * @param <A> A type that extends NetMessage
     * @return Filled out NetMessage from deserialized payload
     */
    public static <A extends NetMessage> A read(NetworkReader reader, Class<A> classType) {
        A message = null;

        try {
            message = classType.newInstance();
            message.deserialize(reader, message.getDataClass());
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Error deserializing NetMessage, probably missing a No argument constructor!!! Use @NoArgsConstructor above class", e);
        }

        return message;
    }

    public abstract NetMessageId getIdentifier();

    protected abstract MessageSendType getMessageSendType();
}
