package at.markusmoosbrugger.logging.dynamodb;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class DynamoDBEnactmentLoggerTest {
  public static String testPropertiesPath = "./src/test/resources/dynamodb-test.properties";


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
    DynamoDBEnactmentLogger dynamoDBLogger = new DynamoDBEnactmentLogger(dynamoDBMock, "testtable");

    when(dynamoDBMock.getTable("testtable")).thenReturn(tableMock);

    EnactmentLogEntry entry =
        new EnactmentLogEntry(timestamp, id, type, executionTime, success, inputComplexity);

    dynamoDBLogger.logEnactment(entry);

    Item item = new Item().withPrimaryKey("functionId", id, "timestamp", timestamp.toEpochMilli())
        .withBoolean("success", success).withString("functionType", "type")
        .withDouble("executionTime", executionTime).with("inputComplexity", inputComplexity);

    verify(tableMock).putItem(item);
  }

  @Test
  public void readProperties() {
    DynamoDB dynamoDBMock = mock(DynamoDB.class);
    DynamoDBEnactmentLogger dynamoDBLogger = new DynamoDBEnactmentLogger(dynamoDBMock, "tablename");

    assertNull(dynamoDBLogger.accessKeyId);
    assertNull(dynamoDBLogger.secretAccessKey);
    assertNull(dynamoDBLogger.region);
    assertEquals("tablename", dynamoDBLogger.tableName);

    dynamoDBLogger.pathToPropertiesFile = testPropertiesPath;
    dynamoDBLogger.readProperties();

    assertEquals("testaccesskeyid", dynamoDBLogger.accessKeyId);
    assertEquals("testsecretaccesskey", dynamoDBLogger.secretAccessKey);
    assertEquals("testregion", dynamoDBLogger.region);
    assertEquals("testtable", dynamoDBLogger.tableName);
  }
}
