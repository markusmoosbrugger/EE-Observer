package at.uibk.dps.ee.observer.logging.influxdb;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.write.Point;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class InfluxDBEnactmentLoggerTest {

  //public static String testPropertiesPath = "./src/test/resources/influxdb-test.properties";

  private InfluxDBConfiguration getTestConfiguration() {
    Properties properties = new Properties();
    properties.put("token", "testtoken");
    properties.put("url", "testurl");
    properties.put("bucket", "testbucket");
    properties.put("org", "testorg");
    InfluxDBConfiguration configuration = new InfluxDBConfiguration(properties);

    return configuration;
  }

  @Test
  public void testLogEnactment() {
    String id = "id";
    String type = "type";
    double executionTime = 1.12;
    boolean success = true;
    double inputComplexity = 0.9;
    Instant timestamp = Instant.ofEpochMilli(1618674847123l);

    InfluxDBClient clientMock = mock(InfluxDBClient.class);
    WriteApi writeApiMock = mock(WriteApi.class);


    InfluxDBEnactmentLogger influxDBLogger =
        new InfluxDBEnactmentLogger(clientMock, getTestConfiguration());

    EnactmentLogEntry entry =
        new EnactmentLogEntry(timestamp, id, type, executionTime, success, inputComplexity);

    when(clientMock.getWriteApi()).thenReturn(writeApiMock);
    influxDBLogger.logEnactment(entry);

    ArgumentCaptor acPoint = ArgumentCaptor.forClass(Point.class);
    verify(writeApiMock).writePoint(eq("testbucket"), eq("testorg"), (Point) acPoint.capture());

    Point capturedPoint = (Point) acPoint.getValue();
    String expectedLineProtocol = "Enactment,functionId=id,functionType=type executionTime=1.12,"
        + "inputComplexity=0.9,success=true 1618674847123000000";

    assertEquals(expectedLineProtocol, capturedPoint.toLineProtocol());
  }
}
