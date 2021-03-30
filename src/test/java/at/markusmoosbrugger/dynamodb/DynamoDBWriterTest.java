package at.markusmoosbrugger.dynamodb;

import at.markusmoosbrugger.functioninvocation.FunctionInvocation;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DynamoDBWriterTest {

  @Test public void testConstructor() {
    DynamoDBWriter writer = new DynamoDBWriter();
    Properties properties = new Properties();
    try (InputStream input = new FileInputStream("./src/main/resources/dynamodb.credentials")) {
      properties.load(input);
    } catch (FileNotFoundException e) {
      fail();
    } catch (IOException e) {
      fail();
    }

    assertEquals(writer.tableName, properties.get("table"));
    assertNotNull(writer.invocationsTable);
  }

  @Test public void testSaveFunctionInvocation() {
    Table invocationsTableMock = mock(Table.class);

    DynamoDBWriter writer = new DynamoDBWriter();
    writer.invocationsTable = invocationsTableMock;

    FunctionInvocation invocation = new FunctionInvocation();
    Instant timestamp = Instant.now();
    invocation.setFunctionId("testID");
    invocation.setFunctionType("testType");
    invocation.setExecutionTime(110.2);
    invocation.setSuccess(true);
    invocation.setTime(timestamp);

    writer.saveFunctionInvocation(invocation);

    Item item =
        new Item().withPrimaryKey("functionId", "testID", "timestamp", timestamp.toEpochMilli())
            .withBoolean("success", true).withString("functionType", "testType")
            .withDouble("executionTime", 110.2);

    verify(invocationsTableMock).putItem(item);
  }

  @Test public void testSaveFunctionInvocationThrowException() {
    Table invocationsTableMock = mock(Table.class);
    Logger loggerMock = mock(Logger.class);

    DynamoDBWriter writer = new DynamoDBWriter();
    writer.invocationsTable = invocationsTableMock;
    writer.logger = loggerMock;

    when(invocationsTableMock.putItem(any(Item.class))).thenThrow(new IllegalStateException());

    writer.saveFunctionInvocation(new FunctionInvocation());

    verify(loggerMock).error(eq("Unable to log function invocation. "), any(Exception.class));

  }
}

