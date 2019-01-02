package server.packetprocessing;

import server.Server;

import java.net.DatagramPacket;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class PacketProcessor {

    protected LinkedBlockingQueue <DatagramPacket> datagramPackets = new LinkedBlockingQueue<>();
    protected Server parentServer;

    private boolean running;

    public PacketProcessor(Server parentServer) {
        this.parentServer = parentServer;
    }

    /**
     * This must be called in order to start the processing thread
     */
    public void start() {
        running = true;

        Thread thread = new Thread(() -> Run(), "Packet Processor");
        thread.start();
    }

    private void Run() {
        while (running) {
            try {
                DatagramPacket packet = datagramPackets.take();
                ProcessPacket(packet);
            } catch (InterruptedException e) {
                System.out.println("Error processing datagram packets: " + e);
            }
        }
    }

    public void shutdown() {
        running = false;
    }

    /**
     * Adds a packet to the datagram queue. This queue is consistently emptied and process by a running task
     * @param packet The filled Datagram packet to process.
     */
    public void AddUdpPacket(DatagramPacket packet) {
        datagramPackets.add(packet);
    }

    public abstract void ProcessPacket(DatagramPacket packet);
}
