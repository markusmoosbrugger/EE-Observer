package at.markusmoosbrugger.functioninvocation;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.time.Instant;

public class FunctionInvocationSimulator {

  static final int MAX_EXECUTION_TIME = 10;
  protected FunctionInvocationWriter writer;

  public FunctionInvocationSimulator(FunctionInvocationWriter writer) {
    this.writer = writer;
  }

  public void simulateMultipleFunctions(int numberOfFunctions, int numberOfRuns) {
    for (int i = 0; i < numberOfRuns; i++) {
      simulateFunction(numberOfFunctions);
    }
  }

  // functions are simulated in a way that functions with a slower function number tend to have a
  // lower execution time, however, also a smaller success ratio
  public void simulateFunction(int numberOfFunctions) {
    for (int i = 0; i < numberOfFunctions; i++) {
      randomWait(1000);
      FunctionInvocation invocation = new FunctionInvocation();
      invocation.functionId = "dummy_function_" + String.valueOf(i);
      invocation.functionType = "functionType_" + String.valueOf(i % 3);
      // success ratio depends on the function number
      invocation.setRandomSuccess((i + 1) * 0.1);
      // execution time depends on the function number
      invocation.setRandomExecutionTime(MAX_EXECUTION_TIME * (i + 5), MAX_EXECUTION_TIME * i);
      invocation.time = Instant.now();

      // optional: add input and output to function invocation
      JsonObject input = new JsonObject();
      input.add("inputValue", new JsonPrimitive(Math.random() * 10));
      invocation.input = input;

      JsonObject output = new JsonObject();
      output.add("result", new JsonPrimitive(Math.random() * 10 + 10));
      invocation.output = output;

      writer.saveFunctionInvocation(invocation);
    }
  }



  private void randomWait(int maxMillis) {
    try {
      Thread.sleep((long) (Math.random() * maxMillis));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


}
