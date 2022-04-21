package dev.avyguzov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class ConfigsReader {
    private static final Logger logger = LogManager.getLogger(ConfigsReader.class);
    public final Properties properties = new Properties();

    public ConfigsReader(String resourceName) throws IOException {
        logger.info("Searching for config file: " + resourceName);

        InputStream si = Optional.ofNullable(ConfigsReader.class.getClassLoader().getResourceAsStream(resourceName))
                .orElseThrow(FileNotFoundException::new);
        properties.load(si);
    }

    public String getProperty(String propKey) {
        return properties.getProperty(propKey);
    }
}
