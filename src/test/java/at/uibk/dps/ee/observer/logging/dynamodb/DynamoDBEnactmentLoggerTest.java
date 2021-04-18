package at.uibk.dps.ee.observer.logging.dynamodb;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.junit.Test;

import java.time.Instant;
import java.util.Properties;

import static org.mockito.Mockito.*;

public class DynamoDBEnactmentLoggerTest {

  private DynamoDBConfiguration getTestConfiguration() {
    Properties properties = new Properties();
    properties.put("aws_access_key_id", "testaccesskeyid");
    properties.put("aws_secret_access_key", "testsecretaccesskey");
    properties.put("region", "testregion");
    properties.put("table", "testtable");

    DynamoDBConfiguration configuration = new DynamoDBConfiguration(properties);

    return configuration;
  }

  @Test
  public void testLogEnactment() {
    String id = "id";
    String type = "type";
    double executionTime = 1.12;
    boolean success = true;
    double inputComplexity = 0.9;
    Instant timestamp = Instant.now();

    Table tableMock = mock(Table.class);
    DynamoDB dynamoDBMock = mock(DynamoDB.class);
    DynamoDBEnactmentLogger dynamoDBLogger =
        new DynamoDBEnactmentLogger(dynamoDBMock, getTestConfiguration());

    when(dynamoDBMock.getTable("testtable")).thenReturn(tableMock);

    EnactmentLogEntry entry =
        new EnactmentLogEntry(timestamp, id, type, executionTime, success, inputComplexity);

    dynamoDBLogger.logEnactment(entry);

    Item item = new Item().withPrimaryKey("functionId", id, "timestamp", timestamp.toEpochMilli())
        .withBoolean("success", success).withString("functionType", "type")
        .withDouble("executionTime", executionTime).with("inputComplexity", inputComplexity);

    verify(tableMock).putItem(item);
  }
}
