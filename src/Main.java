import shared.Messages.NetMessage;
import shared.Messages.Types.NetMessageId;
import shared.Messages.Unreliable.PlayerInputMessage;
import shared.serialization.NetworkReader;
import shared.serialization.NetworkWriter;

public class Main {

    public static void main(String args[]) {

        PlayerInputMessage message = PlayerInputMessage.createNew(102, 123.2123f);

        NetworkWriter writer = new NetworkWriter();
        message.serialize(writer);
        byte[] bytes = writer.getBytesAndReset();

        NetworkReader reader = new NetworkReader(bytes);
        NetMessageId messageId = NetMessageId.fromShort(reader.readShort());
        PlayerInputMessage receivedMessage = NetMessage.read(reader, PlayerInputMessage.class);

        System.out.println(receivedMessage.getIdentifier());
        System.out.println(receivedMessage.getData().aimAngle);
        System.out.println(receivedMessage.getData().CS_PLAYER_INPUTS);
    }
}