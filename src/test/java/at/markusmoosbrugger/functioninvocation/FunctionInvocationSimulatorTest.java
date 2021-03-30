package at.markusmoosbrugger.functioninvocation;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FunctionInvocationSimulatorTest {
  @Test public void testAddAndRemoveWriter() {
    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator();

    FunctionInvocationWriter writerMock1 = Mockito.mock(FunctionInvocationWriter.class);
    FunctionInvocationWriter writerMock2 = Mockito.mock(FunctionInvocationWriter.class);

    assertEquals(0, simulator.writers.size());
    simulator.addWriter(writerMock1);
    simulator.addWriter(writerMock2);
    assertEquals(2, simulator.writers.size());
    simulator.removeWriter(writerMock1);
    assertEquals(1, simulator.writers.size());
  }

  @Test public void testSimulateRun() {
    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator();

    FunctionInvocationWriter writerMock1 = Mockito.mock(FunctionInvocationWriter.class);
    FunctionInvocationWriter writerMock2 = Mockito.mock(FunctionInvocationWriter.class);

    simulator.writers.add(writerMock1);
    simulator.writers.add(writerMock2);

    simulator.simulateRun(2);

    verify(writerMock1, times(2)).saveFunctionInvocation(any(FunctionInvocation.class));
    verify(writerMock2, times(2)).saveFunctionInvocation(any(FunctionInvocation.class));

  }

  @Test public void test() {
    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator();

    FunctionInvocationWriter writerMock1 = Mockito.mock(FunctionInvocationWriter.class);
    FunctionInvocationWriter writerMock2 = Mockito.mock(FunctionInvocationWriter.class);

    simulator.writers.add(writerMock1);
    simulator.writers.add(writerMock2);

    simulator.simulateMultipleFunctions(5, 2);

    verify(writerMock1, times(10)).saveFunctionInvocation(any(FunctionInvocation.class));
    verify(writerMock2, times(10)).saveFunctionInvocation(any(FunctionInvocation.class));
  }
}
