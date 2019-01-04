package shared.messages.types;

import lombok.NoArgsConstructor;
import shared.serialization.NetworkReader;
import shared.serialization.NetworkWriter;

@NoArgsConstructor
public class PlayerInputData implements IMessageData {
        public int CS_PLAYER_INPUTS;
        public float aimAngle;

        @Override
        public void serialize(NetworkWriter writer) {
                writer.write(CS_PLAYER_INPUTS);
                writer.write(aimAngle);
        }

        @Override
        public void deserialize(NetworkReader reader) {
                CS_PLAYER_INPUTS = reader.readInt();
                aimAngle = reader.readFloat();
        }
}