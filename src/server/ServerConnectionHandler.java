package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.packetprocessing.PacketProcessor;

import java.net.DatagramPacket;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * This is the Class that is responsible for managing connections. These responsibilities include beginning the
 * connection process, following through that process to the end, and providing an interface to gather data about these
 * connections. It is a PacketProcessor and its operations are running on its own thread.
 */
public class ServerConnectionHandler extends PacketProcessor {

    private static final Logger LOGGER = LogManager.getLogger(ServerConnectionHandler.class);

    private int maxConnections;

    private Map<String, Connection> connections = new HashMap<>();

    private BlockingDeque<Integer> freeIds = new LinkedBlockingDeque<>();

    public ServerConnectionHandler(Server server, int maxConnections) {
        super(server);                                                // Store our parentServer variable
        this.maxConnections = maxConnections;
        for (int i = 0; i < maxConnections; i++) { freeIds.push(i); } // Initialize Id Queue.
    }

    @Override
    public void ProcessPacket(DatagramPacket packet) {

    }

    public void beginNewConnection(String ip, int port) {
         String key = getKeyFromAddress(ip, port);

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

    public static String getKeyFromAddress(String ip, int port) {
        return ip + ":" + port;
    }
}
