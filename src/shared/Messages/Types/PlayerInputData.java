package shared.Messages.Types;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlayerInputData extends shared.Messages.Types.MessageData {
        public int CS_PLAYER_INPUTS;
        public float aimAngle;

        public PlayerInputData(int inputs, float aimAngle) {
            this.CS_PLAYER_INPUTS = inputs;
            this.aimAngle = aimAngle;
        }
}
