package shared.Messages.Unreliable;
import lombok.NoArgsConstructor;
import shared.Messages.Types.NetMessageId;
import shared.Messages.Types.PlayerInputData;

/** NOTE: NoArgsConstructor is required **/
@NoArgsConstructor
public class PlayerInputMessage extends UnreliableMessage<PlayerInputData> {

    public PlayerInputMessage(PlayerInputData data) {
        super(data);
    }

    @Override
    public NetMessageId getIdentifier() {
        return NetMessageId.PLAYER_INPUT;
    }

    @Override
    protected void processReceivedMessage(PlayerInputData message) {
        System.out.println("Server received: " + message.aimAngle + " " + message.CS_PLAYER_INPUTS);
    }

    /**
     * Convienence method for creating a new message without having to create a PlayerInputData object first
     * @param playerInputs The Player inputs to use
     * @param aimAngle  The aim angle to use for the data
     * @return PlayerInputMessage with valid filled data
     */
    public static PlayerInputMessage createNew(int playerInputs, float aimAngle ) {

        PlayerInputData data = new PlayerInputData();
        data.aimAngle = aimAngle;
        data.CS_PLAYER_INPUTS = playerInputs;

        return new PlayerInputMessage(data);
    }
}
