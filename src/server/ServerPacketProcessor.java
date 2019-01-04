package server;

import server.Server;
import shared.packetprocessing.GenericProcessor;

import java.net.DatagramPacket;

public class ServerPacketProcessor extends GenericProcessor<DatagramPacket> {

    Server parentServer;

    public ServerPacketProcessor(Server parent) {
        super(parent);
        parentServer = parent;
    }

    @Override
    public void processItem(DatagramPacket packet) {
        System.out.println("Packet Received On Server");

        if (!parentServer.getConnectionHandler().connectionEstablished(packet)) {
            System.out.println("Packet Placed In Connection Handler Work");
            parentServer.getConnectionHandler().addWorkItem(packet);
            return;
        }
    }
}
