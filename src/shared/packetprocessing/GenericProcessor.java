package shared.packetprocessing;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is a generic processor class that requires an ProcessingHub entity.
 * @param <T>
 */
public abstract class GenericProcessor<T> {

    protected LinkedBlockingQueue <T> workItems = new LinkedBlockingQueue<>();
    protected ProcessingHub parentEntity;

    private boolean running;

    public GenericProcessor(ProcessingHub parentEntity) {
        this.parentEntity = parentEntity;
        parentEntity.registerProcessor(this);
    }

    /**
     * This must be called in order to start the processing thread
     */
    public void start() {
        running = true;

        Thread thread = new Thread(() -> run(), "Packet Processor");
        thread.start();
    }

    private void run() {
        while (running) {
            try {
                T packet = workItems.take();
                processItem(packet);
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
     * @param item Item of type T to process.
     */
    public void addWorkItem(T item) {
        workItems.add(item);
    }

    public abstract void processItem(T item);
}
