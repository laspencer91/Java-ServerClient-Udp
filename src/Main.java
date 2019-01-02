import shared.Messages.Types.PlayerInputData;
import shared.Messages.Types.PositionData;
import shared.Messages.Unreliable.PlayerInputMessage;
import shared.Messages.Unreliable.PositionUpdateMessage;

public class Main {

    public static void main(String args[])  {
//        Server server = new Server(new Config().setBindPort(15150));
//        Client client = new Client(new Config().setPort(15150));
//
//        server.start();
//        client.start();
//        client.blockingWaitUntilConnected();
//
//        client.send(new PositionUpdateMessage(new PositionData(10.2432f, 123.1232f)));
//        client.send(new PlayerInputMessage(new PlayerInputData(20, 250.232f)));
//        while (true) {
//            Thread.sleep(100);
//            // System.out.println(ClientTimerSyncMessage.getRoundTripTime());
//        }

        MessagePack msgpack = new MessagePack();
    }
}
