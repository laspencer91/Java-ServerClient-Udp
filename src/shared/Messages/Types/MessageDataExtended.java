package shared.Messages.Types;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * This class can be inherited from in order to specify how the object
 * should be written to and read from. No-Param constructor is required.
 */
public abstract class MessageDataExtended extends MessageData implements KryoSerializable {

    @Override
    public void write(Kryo kryo, Output output) {
        writeMessage(output);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        readMessage(input);
    }

    protected abstract void writeMessage(Output writer);

    protected abstract void readMessage(Input reader);
}
