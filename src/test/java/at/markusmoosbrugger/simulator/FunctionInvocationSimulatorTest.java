package at.markusmoosbrugger.simulator;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.Instant;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FunctionInvocationSimulatorTest {
  @Test
  public void testAddAndRemoveWriter() {
    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator();

    EnactmentLogger loggerMock1 = Mockito.mock(EnactmentLogger.class);
    EnactmentLogger loggerMock2 = Mockito.mock(EnactmentLogger.class);

    assertEquals(0, simulator.loggers.size());
    simulator.addLogger(loggerMock1);
    simulator.addLogger(loggerMock2);
    assertEquals(2, simulator.loggers.size());
    simulator.removeLogger(loggerMock1);
    assertEquals(1, simulator.loggers.size());
  }

  @Test
  public void testSimulateRun() {
    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator();

    EnactmentLogger loggerMock1 = Mockito.mock(EnactmentLogger.class);
    EnactmentLogger loggerMock2 = Mockito.mock(EnactmentLogger.class);

    simulator.loggers.add(loggerMock1);
    simulator.loggers.add(loggerMock2);

    simulator.simulateRun(2);

    verify(loggerMock1, times(2)).logEnactment(any(EnactmentLogEntry.class));
    verify(loggerMock2, times(2)).logEnactment(any(EnactmentLogEntry.class));

  }

  @Test
  public void test() {
    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator();

    EnactmentLogger loggerMock1 = Mockito.mock(EnactmentLogger.class);
    EnactmentLogger loggerMock2 = Mockito.mock(EnactmentLogger.class);

    simulator.loggers.add(loggerMock1);
    simulator.loggers.add(loggerMock2);

    simulator.simulateMultipleFunctions(5, 2);

    verify(loggerMock1, times(10)).logEnactment(any(EnactmentLogEntry.class));
    verify(loggerMock2, times(10)).logEnactment(any(EnactmentLogEntry.class));
  }

  @Test
  public void testGetRandomSuccess() {
    boolean success = FunctionInvocationSimulator.getRandomSuccess(0.0);
    assertFalse(success);

    success = FunctionInvocationSimulator.getRandomSuccess(1.0);
    assertTrue(success);
  }

  @Test
  public void testGetRandomDouble() {
    double executionTime = FunctionInvocationSimulator.getRandomDouble(0, 0);
    assertEquals(0.0, executionTime, 0.00001);

    executionTime = FunctionInvocationSimulator.getRandomDouble(10.5, 10.5);
    assertEquals(10.5, executionTime, 0.00001);

    executionTime = FunctionInvocationSimulator.getRandomDouble(80, 100);
    assertTrue(executionTime >= 80);
    assertTrue(executionTime <= 100);
  }

  @Test
  public void testGetRandomWait() {
    Instant start = Instant.now();
    FunctionInvocationSimulator.randomWait(50);
    long millis = Duration.between(start, Instant.now()).toMillis();
    assertTrue(millis >= 0);
    assertTrue(millis <= 50);
  }

}
