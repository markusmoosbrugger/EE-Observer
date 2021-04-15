package at.uibk.dps.ee.observer.logging.properties;

import at.uibk.dps.ee.observer.logging.jdbc.JdbcConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
  protected static final Logger logger = LoggerFactory.getLogger(PropertiesReader.class);


  public static Properties readProperties(String filepath) {
    Properties properties = new Properties();
    try (InputStream input = new FileInputStream(filepath)) {
      properties.load(input);
    } catch (FileNotFoundException e) {
      logger.error("Properties file not found at given location {}.", filepath, e);
    } catch (IOException e) {
      logger.error("IO Exception while reading properties file at given location {}.", filepath, e);
    }

    return properties;
  }
}
