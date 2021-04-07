package at.markusmoosbrugger.functioninvocation;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FunctionInvocationSimulatorTest {
  @Test public void testAddAndRemoveWriter() {
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

  @Test public void testSimulateRun() {
    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator();

    EnactmentLogger loggerMock1 = Mockito.mock(EnactmentLogger.class);
    EnactmentLogger loggerMock2 = Mockito.mock(EnactmentLogger.class);

    simulator.loggers.add(loggerMock1);
    simulator.loggers.add(loggerMock2);

    simulator.simulateRun(2);

    verify(loggerMock1, times(2)).logEnactment(any(EnactmentLogEntry.class));
    verify(loggerMock2, times(2)).logEnactment(any(EnactmentLogEntry.class));

  }

  @Test public void test() {
    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator();

    EnactmentLogger loggerMock1 = Mockito.mock(EnactmentLogger.class);
    EnactmentLogger loggerMock2 = Mockito.mock(EnactmentLogger.class);

    simulator.loggers.add(loggerMock1);
    simulator.loggers.add(loggerMock2);

    simulator.simulateMultipleFunctions(5, 2);

    verify(loggerMock1, times(10)).logEnactment(any(EnactmentLogEntry.class));
    verify(loggerMock2, times(10)).logEnactment(any(EnactmentLogEntry.class));
  }
}
