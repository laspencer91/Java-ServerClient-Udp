package shared.Messages.Unreliable;
import shared.Messages.Types.PlayerInputData;

public class PlayerInputMessage extends UnreliableMessage<PlayerInputData> {

    public PlayerInputMessage() {}

    public PlayerInputMessage(PlayerInputData data) {
        super(data);
    }

    @Override
    protected void processReceivedMessage(PlayerInputData message) {
        System.out.println("Server received: " + message.aimAngle + " " + message.CS_PLAYER_INPUTS);
    }
}
