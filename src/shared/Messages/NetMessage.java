package shared.Messages;

import com.google.common.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.Messages.Types.IMessageData;
import shared.Messages.Types.NetMessageId;
import shared.serialization.NetworkReader;
import shared.serialization.NetworkWriter;

/**
 * NetMessage is what all  NetworkMessages should inherit from. It handles serialization and deserialization
 * of its messages.
 * @param <T> Type of IMessageData that this message should holds
 */
public abstract class NetMessage<T extends IMessageData> {

    private static Logger LOGGER = LogManager.getLogger(NetMessage.class);

    private NetMessageId messageIdentifier;

    private Class dataClass = new TypeToken<T>(getClass()) { }.getRawType();

    protected T data;


    public NetMessage() {
    }

    public NetMessage(T data) {
        this.data = data;
        messageIdentifier = getIdentifier();
    }

    public void serialize(NetworkWriter writer) {
        // TODO: Write Header

        writer.write(messageIdentifier.shortValue());
        data.serialize(writer);
    }

    public <B extends IMessageData> void deserialize(NetworkReader reader, Class<B> messageType) throws IllegalAccessException, InstantiationException {
        // TODO: Read Header
        B messageData = messageType.newInstance();
        data = (T) messageData;
        data.deserialize(reader);
    }

    /**
     * Get the data that this class is housing
     * @return The data
     */
    public T getData() {
        return data;
    }

    /**
     * Get the class of the data this is housing
     * @return The class of the data
     */
    public Class getDataClass() {
        return dataClass;
    }

    /**
     * Using data stored in a reader return a NetMessage that it contains.
     * @param reader The reader to read from
     * @param classType The NetMessage subtype class to generate
     * @param <A> A type that extends NetMessage
     * @return Filled out NetMessage from deserialized data
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
}
