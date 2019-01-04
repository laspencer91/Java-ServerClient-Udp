package client;

import shared.messages.data.connection.ConnectionRequestMessageData;
import shared.messages.reliable.ConnectionRequestMessage;
import shared.statics.enums.ConnectionStatus;
import shared.packetprocessing.GenericProcessor;

import java.net.DatagramPacket;

public class ClientPacketProcessor extends GenericProcessor<DatagramPacket> {

    Client parentClient;

    public ClientPacketProcessor(Client parent) {
        super(parent);
        parentClient = parent;
    }

    @Override
    public void processItem(DatagramPacket packet) {
        if (parentClient.getConnectionStatus() != ConnectionStatus.CONNECTED) {
            handleConnectionPacket(packet);
            return;
        }
    }

    private void handleConnectionPacket(DatagramPacket packet) {
        
    }
}
