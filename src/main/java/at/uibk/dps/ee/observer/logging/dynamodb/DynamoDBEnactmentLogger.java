package at.uibk.dps.ee.observer.logging.dynamodb;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import at.uibk.dps.ee.observer.logging.configuration.PropertiesReader;
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

import java.util.Properties;

/**
 * The {@link DynamoDBEnactmentLogger} is used to log information about the enactment to the NoSQL
 * database DynamoDB.
 *
 * @author Markus Moosbrugger
 */
public class DynamoDBEnactmentLogger implements EnactmentLogger {
  protected final Logger logger = LoggerFactory.getLogger(DynamoDBEnactmentLogger.class);

  protected DynamoDBConfiguration configuration;
  protected DynamoDB dynamoDB;

  /**
   * Default constructor. Reads the database configuration properties from the specified properties
   * file and creates a DynamoDB object used to access the database.
   */
  @Inject
  public DynamoDBEnactmentLogger(
      @Constant(value = "pathToDynamoDBProperties", namespace = DynamoDBEnactmentLogger.class)
      final String pathToPropertiesFile) {
    Properties properties = PropertiesReader.readProperties(pathToPropertiesFile);
    this.configuration = new DynamoDBConfiguration(properties);
    initDynamoDB();
  }

  /**
   * Additional constructor which can be used to provide a DynamoDB object combined with a name for
   * the table.
   *
   * @param dynamoDB      the DynamoDB object
   * @param configuration the DynamoDB configuration
   */
  public DynamoDBEnactmentLogger(final DynamoDB dynamoDB,
      final DynamoDBConfiguration configuration) {
    this.dynamoDB = dynamoDB;
    this.configuration = configuration;
  }

  @Override
  public void logEnactment(final EnactmentLogEntry entry) {
    final Table table = dynamoDB.getTable(configuration.getTableName());

    table.putItem(new Item().withPrimaryKey("functionId", entry.getFunctionId(), "timestamp",
        entry.getTimestamp().toEpochMilli()).withBoolean("success", entry.isSuccess())
        .withString("functionType", entry.getFunctionType())
        .withDouble("executionTime", entry.getExecutionTime())
        .withDouble("inputComplexity", entry.getInputComplexity()));
  }

  /**
   * Initializes the connection to DynamoDB and loads the specified table.
   */
  private void initDynamoDB() {
    final BasicAWSCredentials awsCredentials =
        new BasicAWSCredentials(configuration.getAccessKeyId(), configuration.getSecretAccessKey());
    final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withRegion(configuration.getRegion()).build();
    this.dynamoDB = new DynamoDB(amazonDynamoDB);
  }
}
