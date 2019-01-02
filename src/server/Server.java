package server;

import server.packetprocessing.ServerPacketProcessor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {

    public final static String PACKET_PROTOCOL_KEY = "IA";

    private boolean running;

    private ServerPacketProcessor packetProcessor;

    private ServerConnectionHandler<String> connectionHandler;

    // UDP
    private Thread udpSocketThread;
    private DatagramSocket udpSocket;
    private byte[] udpBuffer = new byte[512];
    private int udpPort;

    // TCP
    private int tcpPort;

    public Server(int udpPort, int tcpPort) {
        this.udpPort = udpPort;
        this.tcpPort = tcpPort;
        this.packetProcessor   = new ServerPacketProcessor(this);
        this.connectionHandler = new ServerConnectionHandler<>();
    }

    public boolean StartServer() {
        running = true;
        packetProcessor.start();
        udpSocketThread = new Thread(() -> runUdpThread(), "Udp Thread");
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
            packetProcessor.AddUdpPacket(packet);
        }
        udpSocket.close();
    }

    /** Server Shutdown Related Logic **/
    public void shutdown() {
        running = false;
        packetProcessor.shutdown();
    }

    public ServerConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }
}
