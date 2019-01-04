package shared.packetprocessing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface defines what is needed for a processing hub. A processing hub may house one or many GenericProcessors
 */
public abstract class ProcessingHub {

    private static final Logger LOGGER = LogManager.getLogger();

    private List<GenericProcessor> registeredProcessors = new ArrayList<>();

    public void start() {
        for (GenericProcessor proc : registeredProcessors) {
            proc.start();
        }
        onStart();
    }

    public void shudown() {
        for (GenericProcessor processor : registeredProcessors) { processor.shutdown(); }
        onShutdown();
    }

    public void registerProcessor(GenericProcessor processor) {
        if (registeredProcessors.contains(processor)) {
            LOGGER.error("Attempting to register packetProcessor " + processor.getClass() +
                    " multiple times for this server. Note: packet processors register themselves on creation.");
            return;
        }

        registeredProcessors.add(processor);
    }

    protected abstract void onStart();

    protected abstract void onShutdown();
}
