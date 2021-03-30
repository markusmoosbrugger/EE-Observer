package at.markusmoosbrugger.logback;

import at.markusmoosbrugger.functioninvocation.FunctionInvocation;
import org.junit.Test;
import org.slf4j.Logger;

import java.time.Instant;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LogbackMySQLWriterTest {

  @Test public void testConstructor() {
    LogbackMySQLWriter writer = new LogbackMySQLWriter();

    assertNotNull(writer.logger);
  }

  @Test public void saveFunctionInvocationTest() {
    Logger loggerMock = mock(Logger.class);

    LogbackMySQLWriter writer = new LogbackMySQLWriter();
    writer.logger = loggerMock;

    FunctionInvocation invocation = new FunctionInvocation();
    Instant timestamp = Instant.now();
    invocation.setFunctionId("testID");
    invocation.setFunctionType("testType");
    invocation.setExecutionTime(110.2);
    invocation.setSuccess(true);
    invocation.setTime(timestamp);

    writer.saveFunctionInvocation(invocation);

    verify(loggerMock).info("ID {} TYPE {} EXEC TIME {} milliseconds SUCCESS {} INPUT {} OUTPUT "
        + "{}.", "testID", "testType", 110.2, true, null, null);
  }

}
