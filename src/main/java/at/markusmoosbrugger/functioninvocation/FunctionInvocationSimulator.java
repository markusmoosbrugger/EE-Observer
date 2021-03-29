package at.markusmoosbrugger.functioninvocation;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FunctionInvocationSimulator {

  static final double MAX_EXECUTION_TIME = 10;
  protected Logger logger = LoggerFactory.getLogger(FunctionInvocationSimulator.class);
  protected List<FunctionInvocationWriter> writers;

  public FunctionInvocationSimulator() {
    this.writers = new ArrayList<>();
  }

  public void addWriter(FunctionInvocationWriter writer) {
    this.writers.add(writer);
  }

  public void removeWriter(FunctionInvocationWriter writer) {
    this.writers.remove(writer);
  }

  public void simulateMultipleFunctions(int numberOfFunctions, int numberOfRuns) {
    for (int i = 0; i < numberOfRuns; i++) {
      System.out.println("\n--------- Simulating run " + (i+1) + " ---------");
      simulateRun(numberOfFunctions);
    }
  }

  // functions are simulated in a way that functions with a slower function number tend to have a
  // lower execution time, however, also a smaller success ratio
  public void simulateRun(int numberOfFunctions) {
    for (int i = 0; i < numberOfFunctions; i++) {
      randomWait(1000);
      FunctionInvocation invocation = new FunctionInvocation();
      invocation.functionId = "dummy_function_" + i;
      invocation.functionType = "functionType_" + i % 3;
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

      saveFunctionInvocation(invocation);
    }
  }

  private void saveFunctionInvocation(FunctionInvocation invocation) {
    writers.stream().forEach(
        functionInvocationWriter -> functionInvocationWriter.saveFunctionInvocation(invocation));
  }



  private void randomWait(int maxMillis) {
    try {
      Thread.sleep((long) (Math.random() * maxMillis));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException();
    }
  }


}
