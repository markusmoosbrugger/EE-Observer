package at.uibk.dps.ee.observer.logging.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * The {@link PropertiesReader} reads properties file.
 *
 * @author Markus Moosbrugger
 */
public class PropertiesReader {
  protected static final Logger logger = LoggerFactory.getLogger(PropertiesReader.class);


  private PropertiesReader() {
    throw new IllegalStateException("Utility class which should not be instantiated.");
  }

  /**
   * Reads the properties from the given path to the properties file.
   *
   * @param filepath the path to the file
   * @return the properties
   */
  public static Properties readProperties(String filepath) {
    Properties properties = new Properties();
    try {
      properties = readProperties(new FileInputStream(filepath));
    } catch (FileNotFoundException e) {
      logger.error("Properties file not found at given location {}.", filepath, e);
    }

    return properties;
  }

  /**
   * Reads the properties from the given input stream.
   *
   * @param inputStream the input stream
   * @return the properties
   */
  public static Properties readProperties(FileInputStream inputStream) {
    Properties properties = new Properties();
    try (inputStream) {
      properties.load(inputStream);
    } catch (IOException e) {
      logger.error("IO Exception while reading properties file with input stream.", e);
    }

    return properties;
  }
}
