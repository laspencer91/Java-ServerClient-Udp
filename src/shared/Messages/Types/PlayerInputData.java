package shared.Messages.Types;

import lombok.NoArgsConstructor;
import shared.serialization.NetworkWriter;

@NoArgsConstructor
public class PlayerInputData extends BaseMessageData {
        public int CS_PLAYER_INPUTS;
        public float aimAngle;

}