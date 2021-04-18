package at.uibk.dps.ee.observer.logging.configuration;

import java.security.InvalidParameterException;
import java.util.Properties;

/**
 * The {@link LoggerConfiguration} is the parent class for the different database configurations.
 *
 * @author Markus Moosbrugger
 */
public abstract class LoggerConfiguration {
  /**
   * The default constructor.
   *
   * @param properties the database properties
   */
  protected LoggerConfiguration(Properties properties) {
    validateProperties(properties);
  }

  /**
   * Validates the given properties. Checks if all required properties exist and throws an {@link
   * InvalidParameterException} otherwise.
   *
   * @param properties the database properties
   * @throws InvalidParameterException if a required property is missing
   */
  protected abstract void validateProperties(Properties properties)
      throws InvalidParameterException;
}
