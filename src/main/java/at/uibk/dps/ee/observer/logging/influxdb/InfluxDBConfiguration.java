package at.uibk.dps.ee.observer.logging.influxdb;

import at.uibk.dps.ee.observer.logging.configuration.LoggerConfiguration;

import java.security.InvalidParameterException;
import java.util.Properties;

/**
 * The {@link InfluxDBConfiguration} defines the properties required for connecting to a InfluxDB
 * database.
 *
 * @author Markus Moosbrugger
 */
public class InfluxDBConfiguration extends LoggerConfiguration {

  protected String token;
  protected String bucket;
  protected String organization;
  protected String url;

  /**
   * The default constructor.
   *
   * @param properties the properties for using InfluxDB
   */
  public InfluxDBConfiguration(Properties properties) {
    super(properties);

    this.token = properties.getProperty("token");
    this.bucket = properties.getProperty("bucket");
    this.organization = properties.getProperty("org");
    this.url = properties.getProperty("url");
  }

  @Override
  protected void validateProperties(Properties properties) throws InvalidParameterException {
    if (!properties.containsKey("token") || !properties.containsKey("bucket") || !properties
        .containsKey("org") || !properties.containsKey("url")) {
      throw new InvalidParameterException("Property file does not contain all required keys.");
    }
  }

  public String getToken() {
    return token;
  }

  public String getBucket() {
    return bucket;
  }

  public String getOrganization() {
    return organization;
  }

  public String getUrl() {
    return url;
  }
}
