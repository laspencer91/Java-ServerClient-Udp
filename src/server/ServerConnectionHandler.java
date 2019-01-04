package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.messages.NetMessageId;
import shared.messages.PacketHeaderData;
import shared.packetprocessing.GenericProcessor;
import shared.serialization.NetworkReader;
import shared.statics.enums.ConnectionStatus;
import shared.utils.NetUtils;

import java.net.DatagramPacket;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * This is the Class that is responsible for managing connections. These responsibilities include beginning the
 * connection process, following through that process to the end, and providing an interface to gather payload about these
 * connections. It is a GenericProcessor and its operations are running on its own thread.
 */
public class ServerConnectionHandler extends GenericProcessor<DatagramPacket> {

    private static final Logger LOGGER = LogManager.getLogger(ServerConnectionHandler.class);

    private int maxConnections;

    private Map<String, Connection> connections = new HashMap<>();

    private BlockingDeque<Integer> freeIds = new LinkedBlockingDeque<>();

    public ServerConnectionHandler(Server server, int maxConnections) {
        super(server);                                                // Store our parentServer variable
        this.maxConnections = maxConnections;
        for (int i = 0; i < maxConnections; i++) { freeIds.push(i); } // Initialize Id Queue.
    }

    NetworkReader reader = new NetworkReader();
    PacketHeaderData header = new PacketHeaderData();

    @Override
    public void processItem(DatagramPacket packet) {
        byte[] data = packet.getData();

        // Set our reader to look at the recieved data and then set our cached header values to the deserialzied values
        reader.setBuffer(data);
        header.deserialize(reader);

        NetMessageId messageId = NetMessageId.fromShort(reader.readShort());

        switch (messageId) {
            case CONNECTION_REQUEST:
                LOGGER.info("User {} has sent a connection request!", reader.readString());
                break;
        }
    }

    public void beginNewConnection(String ip, int port) {
         String key = NetUtils.getConnectionKeyFromAddress(ip, port);

         if (connections.containsKey(key)) {
             LOGGER.error("Attempting to start a new connection when the ip and port are already in our connections.");
             return;
         }
         if (freeIds.isEmpty()) {
             LOGGER.warn("Attempting new connection will not work since we have no available room left.");
             return;
         }

        try {
            int connectionId = freeIds.take();
            connections.put(key, new Connection(connectionId));

            // TODO: Send client connection initialization packet
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted when trying to retrieve a new id from the freeIds queue", e);
            return;
        }
    }

    /**
     * Resize Max Connections. Note that we cannot shrink the array at runtime since that would have the potential
     * to remove connected clients.
     * @param maxConnections
     */
    public void changeMaxConnections(int maxConnections) {
        if (maxConnections < this.maxConnections) {
            LOGGER.warn("Shrinking of server maxConnections is not currently supported! Ignoring command.");
        }

        // Push more free ids to the queue!
        for (int i = this.maxConnections; i < maxConnections; i++) {
            freeIds.push(i);
        }

        this.maxConnections = maxConnections;
    }

    public boolean connectionEstablished(DatagramPacket packet) {
        String key = NetUtils.getConnectionKeyFromAddress(packet.getAddress().getHostAddress(), packet.getPort());
        Connection conn = connections.get(key);

        return (conn != null) && conn.getStatus() == ConnectionStatus.CONNECTED;
    }
}
