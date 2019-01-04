import client.Client;
import server.Server;
import shared.messages.NetMessage;
import shared.messages.NetMessageId;
import shared.messages.unreliable.PlayerInputMessage;
import shared.serialization.NetworkReader;
import shared.serialization.NetworkWriter;
import shared.statics.NetworkProtocol;

public class Main {

    public static void main(String args[]) {

        Server server = new Server(3000, NetworkProtocol.PROTOCOL_ID);
        Client client = new Client(3001);

        server.start();
        client.connect("localhost", 3000);
    }
}