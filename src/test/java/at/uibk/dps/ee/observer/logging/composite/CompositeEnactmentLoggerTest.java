package at.uibk.dps.ee.observer.logging.composite;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import at.uibk.dps.ee.observer.logging.dynamodb.DynamoDBEnactmentLogger;
import at.uibk.dps.ee.observer.logging.influxdb.InfluxDBEnactmentLogger;
import at.uibk.dps.ee.observer.logging.logback.LogbackEnactmentLogger;
import org.junit.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CompositeEnactmentLoggerTest {

  @Test
  public void testLogEnactment() {
    String typeId = "type";
    String enactmentMode = "mode";
    String implementationId = "id";

    double executionTime = 1.12;
    boolean success = true;
    double inputComplexity = 0.9;
    Instant timestamp = Instant.now();
    EnactmentLogEntry entry =
        new EnactmentLogEntry(timestamp, typeId, enactmentMode, implementationId, new HashSet<>(),
            executionTime, success, inputComplexity);

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
