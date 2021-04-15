package at.uibk.dps.ee.observer.logging.dynamodb;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.inject.Inject;
import org.opt4j.core.start.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The {@link DynamoDBEnactmentLogger} is used to log information about the
 * enactment to the NoSQL database DynamoDB.
 *
 * @author Markus Moosbrugger
 */
public class DynamoDBEnactmentLogger implements EnactmentLogger {
  protected final Logger logger = LoggerFactory.getLogger(DynamoDBEnactmentLogger.class);
  protected String pathToPropertiesFile;
  protected String accessKeyId;
  protected String secretAccessKey;
  protected String tableName;
  protected String region;
  protected DynamoDB dynamoDB;

  /**
   * Default constructor. Reads the database configuration properties from the
   * specified properties file and creates a DynamoDB object used to access the
   * database.
   */
  @Inject
  public DynamoDBEnactmentLogger(
      @Constant(value = "pathToDynamoDBProperties", namespace = DynamoDBEnactmentLogger.class)
      final String pathToPropertiesFile) {
    this.pathToPropertiesFile = pathToPropertiesFile;
    readProperties();
    initDynamoDB();
  }

  /**
   * Additional constructor which can be used to provide a DynamoDB object
   * combined with a name for the table.
   *
   * @param dynamoDB  the DynamoDB object
   * @param tableName the name of the database table
   */
  public DynamoDBEnactmentLogger(final DynamoDB dynamoDB, final String tableName) {
    this.dynamoDB = dynamoDB;
    this.tableName = tableName;
  }

  @Override
  public void logEnactment(final EnactmentLogEntry entry) {
    final Table table = dynamoDB.getTable(tableName);

    table.putItem(new Item().withPrimaryKey("functionId", entry.getFunctionId(), "timestamp",
        entry.getTimestamp().toEpochMilli()).withBoolean("success", entry.isSuccess())
        .withString("functionType", entry.getFunctionType())
        .withDouble("executionTime", entry.getExecutionTime())
        .withDouble("inputComplexity", entry.getInputComplexity()));
  }

  /**
   * Initializes the connection to DynamoDB and loads the specified table.
   */
  protected void initDynamoDB() {
    final BasicAWSCredentials awsCredentials =
        new BasicAWSCredentials(accessKeyId, secretAccessKey);
    final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(region)
        .build();
    this.dynamoDB = new DynamoDB(amazonDynamoDB);
  }

  /**
   * Reads the needed properties from the specified properties file.
   */
  protected void readProperties() {
    try (InputStream input = new FileInputStream(pathToPropertiesFile)) {
      final Properties properties = new Properties();
      properties.load(input);

      this.accessKeyId = (String) properties.get("aws_access_key_id");
      this.secretAccessKey = (String) properties.get("aws_secret_access_key");
      this.tableName = (String) properties.get("table");
      this.region = (String) properties.get("region");

    } catch (FileNotFoundException e) {
      logger.error("Properties file not found at given location {}", pathToPropertiesFile, e);
    } catch (IOException e) {
      logger.error("IO Exception while reading properties file at given location {}.",
          pathToPropertiesFile, e);
    }
  }
}
