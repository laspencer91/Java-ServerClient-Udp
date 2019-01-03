package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.packetprocessing.PacketProcessor;
import server.packetprocessing.ServerPacketProcessor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final Logger LOGGER = LogManager.getLogger(Server.class);

    private boolean running;

    private ServerPacketProcessor packetProcessor;

    private ServerConnectionHandler connectionHandler;

    private List<PacketProcessor> packetProcessorList = new ArrayList<>();

    public final static String PACKET_PROTOCOL_KEY = "IA";

    // UDP
    private Thread udpSocketThread;
    private DatagramSocket udpSocket;
    private byte[] udpBuffer = new byte[512];
    private int udpPort;


    public Server(int udpPort) {
        this.udpPort = udpPort;
        this.packetProcessor   = new ServerPacketProcessor(this);
        this.connectionHandler = new ServerConnectionHandler(this, 10);
    }

    public boolean StartServer() {
        running = true;

        // Start all registered processors
        for (PacketProcessor processor : packetProcessorList) { processor.start(); }

        udpSocketThread = new Thread(() -> runUdpThread(), "Udp Server Thread");
        udpSocketThread.start();

        return true;
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
            packetProcessor.addWork(packet);
        }
        udpSocket.close();
    }

    public void registerPacketProcessor(PacketProcessor processor) {
        if (packetProcessorList.contains(processor)) {
            LOGGER.error("Attempting to register packetProcessor " + processor.getClass() +
                            " multiple times for this server. Note: packet processors register themselves on creation.");
            return;
        }

        packetProcessorList.add(processor);
    }

    /** Server Shutdown Related Logic **/
    public void shutdown() {
        running = false;
        // Shutdown all registered processors
        for (PacketProcessor processor : packetProcessorList) { processor.shutdown(); }
    }

    public ServerConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }
}
