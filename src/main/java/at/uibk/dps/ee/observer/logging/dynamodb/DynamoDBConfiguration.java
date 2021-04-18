package at.uibk.dps.ee.observer.logging.dynamodb;

import at.uibk.dps.ee.observer.logging.configuration.LoggerConfiguration;

import java.security.InvalidParameterException;
import java.util.Properties;

/**
 * The {@link DynamoDBConfiguration} defines the properties required for connecting to a DynamoDB
 * database.
 *
 * @author Markus Moosbrugger
 */
public class DynamoDBConfiguration extends LoggerConfiguration {
  protected String accessKeyId;
  protected String secretAccessKey;
  protected String region;
  protected String tableName;



  /**
   * The default constructor.
   *
   * @param properties the properties for using DynamoDB
   */
  public DynamoDBConfiguration(Properties properties) {
    super(properties);

    this.accessKeyId = properties.getProperty("aws_access_key_id");
    this.secretAccessKey = properties.getProperty("aws_secret_access_key");
    this.region = properties.getProperty("region");
    this.tableName = properties.getProperty("table");
  }

  @Override
  protected void validateProperties(Properties properties) throws InvalidParameterException {
    if (!properties.containsKey("aws_access_key_id") || !properties.containsKey("aws_secret_access_key") || !properties
        .containsKey("region") || !properties.containsKey("table")) {
      throw new InvalidParameterException("Property file does not contain all required keys.");
    }
  }

  public String getAccessKeyId() {
    return accessKeyId;
  }

  public String getSecretAccessKey() {
    return secretAccessKey;
  }

  public String getRegion() {
    return region;
  }

  public String getTableName() {
    return tableName;
  }
}
