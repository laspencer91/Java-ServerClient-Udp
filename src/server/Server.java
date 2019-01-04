package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.packetprocessing.ProcessingHub;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server extends ProcessingHub {

    private static final Logger LOGGER = LogManager.getLogger(Server.class);

    private static int protocolId;

    private boolean running;

    private static Server activeInstance;

    private ServerPacketProcessor packetProcessor;

    private ServerConnectionHandler connectionHandler;

    // UDP settings
    private Thread udpSocketThread;
    private DatagramSocket udpSocket;
    private byte[] udpBuffer = new byte[512];
    private int udpPort;


    public Server(final int udpPort, final int protocolId) {
        this.udpPort = udpPort;
        this.protocolId = protocolId;
        this.packetProcessor   = new ServerPacketProcessor(this);
        this.connectionHandler = new ServerConnectionHandler(this, 10);

        try {
            this.udpSocket = new DatagramSocket(udpPort);
        } catch (SocketException e) {
            LOGGER.error("SocketExceptions while trying to create Server.", e);
        }
    }

    @Override
    protected void onStart() {
        // Check to see if a server is already running
        if (activeInstance != null) {
            LOGGER.warn("Currently only one server per program instance is supported. Shutting down other server to start");
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
                e.printStackTrace(); // TODO: Server Log. "Error on packet reception or attempt to receive"
            }

            InetAddress receivedAddress = packet.getAddress();
            int         receivedPort    = packet.getPort();

            packet = new DatagramPacket(udpBuffer, udpBuffer.length, receivedAddress, receivedPort);
            packetProcessor.addWorkItem(packet);
        }
        udpSocket.close();
    }

    public ServerConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public int getProtocolId() {
        return protocolId;
    }

    /**
     * Get the current running server. Only one server per process is supported currently.
     * @return The active server instance
     */
    public static Server getActiveInstance() {
        return activeInstance;
    }
}
