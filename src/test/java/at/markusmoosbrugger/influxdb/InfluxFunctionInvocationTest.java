package at.markusmoosbrugger.influxdb;

import at.markusmoosbrugger.functioninvocation.FunctionInvocation;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;

public class InfluxFunctionInvocationTest {

  @Test public void testDefaultConstructor() {
    InfluxFunctionInvocation invocation = new InfluxFunctionInvocation();
    assertNull(invocation.functionId);
    invocation.functionId = "testID";
    assertEquals("testID", invocation.functionId);
  }

  @Test public void testConstructorWithArguments() {
    Instant timestamp = Instant.now();
    FunctionInvocation invocation =
        new FunctionInvocation("testID", "testType", timestamp, false, 10.9);

    InfluxFunctionInvocation influxFunctionInvocation = new InfluxFunctionInvocation(invocation);

    assertEquals("testID", influxFunctionInvocation.functionId);
    assertEquals("testType", influxFunctionInvocation.functionType);
    assertEquals(timestamp, influxFunctionInvocation.time);
    assertEquals(false, influxFunctionInvocation.success);
    assertEquals(10.9, influxFunctionInvocation.executionTime, 0.0001);
  }

  @Test public void testEquals() {
    Instant timestamp = Instant.now();
    InfluxFunctionInvocation invocation1 = new InfluxFunctionInvocation();
    assertEquals(invocation1, invocation1);
    assertNotEquals(invocation1, null);

    invocation1.functionId = "function_1";
    invocation1.time = timestamp;

    InfluxFunctionInvocation invocation2 = new InfluxFunctionInvocation();
    invocation2.functionId = "function_2";
    invocation2.time = timestamp;

    assertNotEquals(invocation1, invocation2);

    invocation2.functionId = "function_1";
    assertEquals(invocation1, invocation2);

    invocation2.time = Instant.now();
    assertNotEquals(invocation1, invocation2);
  }

}
