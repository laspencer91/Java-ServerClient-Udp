import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
import org.apache.logging.log4j.spi.LoggerContext;
import shared.Messages.Types.PlayerInputData;
import shared.exceptions.NetworkWriterException;
import shared.serialization.NetworkReader;
import shared.serialization.NetworkWriter;

import java.io.File;
import java.io.FileInputStream;

public class Main {

    public static void main(String args[]) {

        PlayerInputData inputData = new PlayerInputData();
        inputData.aimAngle = 352.232f;
        inputData.CS_PLAYER_INPUTS = 10;

        PlayerInputData inputData2 = new PlayerInputData();
        inputData2.aimAngle = 12.232f;
        inputData2.CS_PLAYER_INPUTS = 1232;

        NetworkWriter writer = new NetworkWriter();
        long startTime = System.nanoTime();
        inputData.serialize(writer);
        inputData2.serialize(writer);

        System.out.println(System.nanoTime() - startTime);

        byte[] bytes = writer.getBytesAndReset();

        NetworkReader reader = new NetworkReader(bytes);
        PlayerInputData inputDataReceived = new PlayerInputData();
        PlayerInputData inputDataReceived2 = new PlayerInputData();
        inputDataReceived.deserialize(reader);
        inputDataReceived2.deserialize(reader);

        System.out.println(inputDataReceived.aimAngle);
        System.out.println(inputDataReceived.CS_PLAYER_INPUTS);

        System.out.println(inputDataReceived2.aimAngle);
        System.out.println(inputDataReceived2.CS_PLAYER_INPUTS);
    }
}
