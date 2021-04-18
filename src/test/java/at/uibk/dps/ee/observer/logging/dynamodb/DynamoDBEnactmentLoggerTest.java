package at.uibk.dps.ee.observer.logging.dynamodb;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.junit.Test;

import java.time.Instant;
import java.util.HashSet;
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
    String typeId = "type";
    String enactmentMode = "mode";
    String implementationId = "id";

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
        new EnactmentLogEntry(timestamp, typeId, enactmentMode, implementationId, new HashSet<>(),
            executionTime, success, inputComplexity);

    dynamoDBLogger.logEnactment(entry);

    Item item = new Item()
        .withPrimaryKey("typeId", typeId, "timestamp", timestamp.toEpochMilli())
        .withString("enactmentMode", enactmentMode)
        .withString("implementationId", implementationId)
        .withDouble("executionTime", executionTime)
        .withBoolean("success", success)
        .withDouble("inputComplexity", inputComplexity);

    verify(tableMock).putItem(item);
  }
}
