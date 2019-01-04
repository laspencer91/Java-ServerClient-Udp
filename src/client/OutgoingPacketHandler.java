package client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.messages.NetMessage;
import shared.serialization.NetworkWriter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.*;

public class OutgoingPacketHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private int sendRate;

    private short currentSequenceNumber = 0;

    private DatagramSocket socket;

    private BlockingQueue<NetMessage> outgoingMessages = new ArrayBlockingQueue<>(200);

    public OutgoingPacketHandler(DatagramSocket socket, int sendRate) {
        this.socket = socket;
        this.sendRate = sendRate;
    }

    /** Begin the transmitting packets process*/
    public void beginTransmitting() {
        long lastRun = System.currentTimeMillis();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> run(), 0, 1000 / sendRate, TimeUnit.MILLISECONDS);
    }

    public void run() {
        NetMessage message;
        NetworkWriter writer = new NetworkWriter();
        byte[] dataBuffer;

        while (!outgoingMessages.isEmpty()) {
            try {
                message = outgoingMessages.take();
                message.header.sequenceNumber = currentSequenceNumber++;
                message.serialize(writer);

                dataBuffer = writer.getBytesAndReset();
                DatagramPacket outPacket = new DatagramPacket(dataBuffer, dataBuffer.length);

                socket.send(outPacket);
            } catch (IOException | InterruptedException e) {
                LOGGER.error("Interruption or IOError while trying to send packets via socket! ", e);
            }
        }
    }

    public void addOutgoingMessage(NetMessage message) {
        outgoingMessages.add(message);
    }

    /**
     * Sends a message without putting it into the queue. This allows you to directly send a message without it waiting
     * for the sendRate tick.
     * @param message The filled out message to send
     * @throws InterruptedException
     */
    public void sendMessageInstant(NetMessage message, String ip, int port) throws InterruptedException, IOException {
        InetAddress address = InetAddress.getByName(ip);

        NetworkWriter writer = new NetworkWriter();
        byte[] dataBuffer;

        message.header.sequenceNumber = currentSequenceNumber++;
        message.serialize(writer);

        dataBuffer = writer.getBytesAndReset();
        DatagramPacket outPacket = new DatagramPacket(dataBuffer, dataBuffer.length, address, port);

        socket.send(outPacket);
    }
}
