package shared.Messages.Types;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.exceptions.NetworkWriterException;
import shared.serialization.NetworkReader;
import shared.serialization.NetworkWriter;

import java.lang.reflect.Field;

/**
 * This class can be inherited from when creating a class that holds data.
 * No-Param constructor is required.
 */
public class BaseMessageData implements IMessageData {
    private static final Logger LOGGER = LogManager.getLogger(BaseMessageData.class);

    public void serialize(NetworkWriter writer) {
        Field[] fields = this.getClass().getFields();

        for (Field field : fields) {
            Class<?> clazz = field.getType();

            try {
                if (clazz.equals(Long.TYPE)) {
                    writer.write((long) field.get(this));
                } else if (clazz.equals(Short.TYPE)) {
                    writer.write((short) field.get(this));
                } else if (clazz.equals(Integer.TYPE)) {
                    writer.write((int) field.get(this));
                } else if (clazz.equals(Double.TYPE)) {
                    writer.write((double) field.get(this));
                } else if (clazz.equals(Character.TYPE)) {
                    writer.write((char) field.get(this));
                } else if (clazz.equals(Byte.TYPE)) {
                    writer.write((byte) field.get(this));
                } else if (clazz.equals(String.class)) {
                    writer.write((String) field.get(this));
                } else if (clazz.equals(Boolean.TYPE)) {
                    writer.write((boolean) field.get(this));
                } else if (clazz.equals(Float.TYPE)) {
                    writer.write((float) field.get(this));
                } else {
                    LOGGER.debug("Could note serialize public value {} of type {} ", field.getName(), clazz.getTypeName());
                }
            } catch (IllegalAccessException | NetworkWriterException e) {
                LOGGER.error("Could not use reflection to determine field", e);
            }
        }
    }

    public void deserialize(NetworkReader reader) {
        Class myClass = getClass();

        Field[] fields = myClass.getFields();

        for (Field field : fields) {
            Class<?> clazz = field.getType();
            field.setAccessible(true);

            try {
                if (clazz.equals(Long.TYPE)) {
                    field.set(this, reader.readLong());
                } else if (clazz.equals(Short.TYPE)) {
                    field.set(this, reader.readShort());
                } else if (clazz.equals(Integer.TYPE)) {
                    field.set(this, reader.readInt());
                } else if (clazz.equals(Double.TYPE)) {
                    field.set(this, reader.readDouble());
                } else if (clazz.equals(Character.TYPE)) {
                    field.set(this, reader.readChar());
                } else if (clazz.equals(Byte.TYPE)) {
                    field.set(this, reader.readByte());
                } else if (clazz.equals(String.class)) {
                    field.set(this, reader.readString());
                } else if (clazz.equals(Boolean.TYPE)) {
                    field.set(this, reader.readBoolean());
                } else if (clazz.equals(Float.TYPE)) {
                    field.set(this, reader.readFloat());
                } else {
                    LOGGER.debug("Could note deserialize public value {} of type {} ", field.getName(), clazz.getTypeName());
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("Could not access a given field even after attempting to set it to accessable.");
            }
        }
    }
}
