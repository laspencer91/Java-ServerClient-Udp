package server.packetprocessing;

import server.Server;
import java.net.DatagramPacket;

public class ServerPacketProcessor extends PacketProcessor {

    public ServerPacketProcessor(Server parentServer) {
        super(parentServer);
    }

    @Override
    public void ProcessPacket(DatagramPacket packet) {

    }
}
