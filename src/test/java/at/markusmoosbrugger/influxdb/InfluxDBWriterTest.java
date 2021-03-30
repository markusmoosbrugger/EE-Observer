package at.markusmoosbrugger.influxdb;

import at.markusmoosbrugger.functioninvocation.FunctionInvocation;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class InfluxDBWriterTest {

  @Test public void testConstructor() {
    InfluxDBWriter writer = new InfluxDBWriter();

    Properties properties = new Properties();
    try (InputStream input = new FileInputStream("./src/main/resources/influxdb.properties")) {
      properties.load(input);
    } catch (FileNotFoundException e) {
      fail();
    } catch (IOException e) {
      fail();
    }

    assertEquals(writer.bucket, properties.get("bucket"));
    assertEquals(writer.org, properties.get("org"));
    assertNotNull(writer.client);
  }

  @Test public void testSaveFunctionInvocation() {
    InfluxDBClient clientMock = mock(InfluxDBClient.class);
    WriteApi writeApiMock = mock(WriteApi.class);
    Logger loggerMock = mock(Logger.class);

    InfluxDBWriter writer = new InfluxDBWriter();
    writer.client = clientMock;
    writer.logger = loggerMock;

    FunctionInvocation invocation = new FunctionInvocation();
    Instant timestamp = Instant.now();
    invocation.setFunctionId("testID");
    invocation.setFunctionType("testType");
    invocation.setExecutionTime(110.2);
    invocation.setSuccess(true);
    invocation.setTime(timestamp);

    when(clientMock.getWriteApi()).thenReturn(writeApiMock);

    writer.saveFunctionInvocation(invocation);

    InfluxFunctionInvocation influxInvocation = new InfluxFunctionInvocation();
    influxInvocation.functionId = "testID";
    influxInvocation.functionType = "testType";
    influxInvocation.executionTime = 110.2;
    influxInvocation.success = true;
    influxInvocation.time = timestamp;

    verify(writeApiMock)
        .writeMeasurement(anyString(), anyString(), eq(WritePrecision.MS), eq(influxInvocation));
    verify(writeApiMock).flush();
    verify(loggerMock)
        .info("TYPE {} ID {} EXEC TIME {} milliseconds SUCCESS {}.", "testID", "testType", 110.2,
            true);
  }

}
