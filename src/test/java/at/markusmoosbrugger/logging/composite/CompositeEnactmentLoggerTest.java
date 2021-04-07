package at.markusmoosbrugger.logging.composite;

import at.markusmoosbrugger.logging.dynamodb.DynamoDBEnactmentLogger;
import at.markusmoosbrugger.logging.influxdb.InfluxDBEnactmentLogger;
import at.markusmoosbrugger.logging.logback.LogbackEnactmentLogger;
import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import org.junit.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CompositeEnactmentLoggerTest {

  @Test
  public void testLogEnactment() {
    String id = "id";
    String type = "type";
    double executionTime = 1.12;
    boolean success = true;
    double inputComplexity = 0.9;
    Instant timestamp = Instant.now();
    EnactmentLogEntry entry =
        new EnactmentLogEntry(timestamp, id, type, executionTime, success, inputComplexity);

    Set<EnactmentLogger> loggers = new HashSet<>();

    LogbackEnactmentLogger logbackMock = mock(LogbackEnactmentLogger.class);
    InfluxDBEnactmentLogger influxDBMock = mock(InfluxDBEnactmentLogger.class);
    DynamoDBEnactmentLogger dynamoDBMock = mock(DynamoDBEnactmentLogger.class);

    loggers.add(logbackMock);
    loggers.add(influxDBMock);
    loggers.add(dynamoDBMock);

    CompositeEnactmentLogger compositeLogger = new CompositeEnactmentLogger(loggers);

    compositeLogger.logEnactment(entry);

    verify(logbackMock).logEnactment(entry);
    verify(influxDBMock).logEnactment(entry);
    verify(dynamoDBMock).logEnactment(entry);
  }
}
