package at.markusmoosbrugger.functioninvocation;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;

public class FunctionInvocationTest {

  @Test
  public void testDefaultConstructor(){
    FunctionInvocation invocation = new FunctionInvocation();
    assertNull(invocation.getFunctionId());
    invocation.setFunctionId("testID");
    assertEquals("testID", invocation.getFunctionId());

    JsonObject input = new JsonObject();
    input.add("test", new JsonPrimitive("inputString"));

    JsonObject output = new JsonObject();
    output.add("out", new JsonPrimitive("outputString"));

    invocation.setInput(input);
    invocation.setOutput(output);

    assertEquals(input, invocation.getInput());
    assertEquals(output, invocation.getOutput());

  }

  @Test
  public void testConstructorWithArguments() {
    Instant timestamp = Instant.now();
    FunctionInvocation invocation = new FunctionInvocation("testID", "testType", timestamp, false
        , 10.9);

    assertEquals("testID", invocation.getFunctionId());
    assertEquals("testType", invocation.getFunctionType());
    assertEquals(timestamp, invocation.getTime());
    assertEquals(false, invocation.isSuccess());
    assertEquals(10.9, invocation.getExecutionTime(), 0.0001);
  }

  @Test
  public void testRandomExecutionTime() {
    FunctionInvocation invocation = new FunctionInvocation();
    assertEquals(0.0, invocation.getExecutionTime(), 0.0001);
    invocation.setRandomExecutionTime(100, 80);
    assertTrue(invocation.getExecutionTime() >= 80);
    assertTrue(invocation.getExecutionTime()<= 100);
  }

  @Test
  public void testRandomSuccess() {
    FunctionInvocation invocation = new FunctionInvocation();
    assertFalse(invocation.isSuccess());
    invocation.setRandomSuccess(1.0);
    assertTrue(invocation.isSuccess());
    invocation.setRandomSuccess(0.0);
    assertFalse(invocation.isSuccess());
  }



}
