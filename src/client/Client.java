package client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Connection;
import shared.messages.data.connection.ConnectionRequestMessageData;
import shared.messages.reliable.ConnectionRequestMessage;
import shared.statics.enums.ConnectionStatus;
import shared.packetprocessing.ProcessingHub;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client extends ProcessingHub {

    private static final Logger LOGGER = LogManager.getLogger(Client.class);

    private boolean running;

    private static Client activeInstance;

    private ClientPacketProcessor packetProcessor;

    private OutgoingPacketHandler outgoingPacketHandler;

    private ConnectionStatus connectionStatus = ConnectionStatus.DISCONNECTED;

    // UDP settings
    private Thread udpSocketThread;
    private DatagramSocket udpSocket;
    private byte[] udpBuffer = new byte[512];


    public Client(final int udpPort) {
        this.packetProcessor   = new ClientPacketProcessor(this);

        try {
            this.udpSocket = new DatagramSocket(udpPort);
        } catch (SocketException e) {
            LOGGER.error("SocketExceptions while trying to create Client.", e);
        }

        this.outgoingPacketHandler = new OutgoingPacketHandler(udpSocket, 60);
    }

    public void connect(String ip, int port) {
        if (connectionStatus != ConnectionStatus.DISCONNECTED) {
            LOGGER.warn("Attempting to connect after a connection attempt has already begun, and not finished");
            return;
        }

        // Handle everything not connection related
        ConnectionRequestMessageData data = new ConnectionRequestMessageData("Logan");
        ConnectionRequestMessage connectionRequestMessage = new ConnectionRequestMessage(data);

        try {
            outgoingPacketHandler.sendMessageInstant(connectionRequestMessage, ip, port);
        } catch (InterruptedException | IOException e) {
            LOGGER.error("Issue sending connectionRequestMessage", e);
        }
    }

    @Override
    protected void onStart() {
        if (connectionStatus != ConnectionStatus.CONNECTED) {
            LOGGER.warn("You must call connect instead of start for the Client. Start will be called upon successful connection");
            shudown();
            return;
        }

        // Check to see if a server is already running
        if (activeInstance != null) {
            LOGGER.warn("Currently only one client per program instance is supported. Shutting down other server to start");
            activeInstance.shudown();
            activeInstance = this;
        } else {
            activeInstance = this;
        }

        running = true;
        udpSocketThread = new Thread(() -> runUdpThread(), "Udp Server Thread");
        udpSocketThread.start();
    }

    @Override
    protected void onShutdown() {
        activeInstance = null;
        running = false;
    }

    public void runUdpThread() {
        while (running) {
            DatagramPacket packet = new DatagramPacket(udpBuffer, udpBuffer.length);

            // Wait and receive
            try {
                udpSocket.receive(packet);
            } catch (IOException e) {
                LOGGER.error("Error on trying to retrieve packet from udp socket", e);
            }

            InetAddress receivedAddress = packet.getAddress();
            int         receivedPort    = packet.getPort();

            packet = new DatagramPacket(udpBuffer, udpBuffer.length, receivedAddress, receivedPort);
            packetProcessor.addWorkItem(packet);
        }
        udpSocket.close();
    }

    /**
     * Get the current running server. Only one server per process is supported currently.
     * @return The active server instance
     */
    public static Client getActiveInstance() {
        return activeInstance;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }
}
