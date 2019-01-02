package shared.Messages.Types;

import lombok.NoArgsConstructor;
import org.msgpack.MessagePack;
import org.msgpack.io.Input;
import org.msgpack.io.Output;

import java.io.IOException;

@NoArgsConstructor
public class PositionData extends MessageDataExtended {
    public float x;
    public float y;

    public PositionData(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected void writeMessage(MessagePack writer) throws IOException {
        writer.write(x);
        writer.write(y);
    }

    @Override
    protected void readMessage(Input reader) throws IOException {
        x = reader.getFloat();
        y = reader.getFloat();
    }
}
