package at.markusmoosbrugger.influxdb;

import java.time.Instant;

public class FunctionSimulator {

  static final int MAX_EXECUTION_TIME = 10;
  protected InfluxDBWriter writer;

  public FunctionSimulator() {
    writer = new InfluxDBWriter();
  }

  public void simulateMultipleFunctions(int numberOfFunctions, int numberOfRuns) {
    for (int i = 0; i<numberOfRuns; i++) {
      simulateFunction(numberOfFunctions);
    }
  }
  // functions with a slower function number tend to have a lower execution time, however, also a
  // smaller success ratio
  public void simulateFunction(int numberOfFunctions) {
    for (int i = 0; i<numberOfFunctions; i++) {
      randomWait(1000);
      InfluxFunction function = new InfluxFunction();
      function.functionId = "function_" + String.valueOf(i);
      function.functionType = "functionType_" + String.valueOf(i);
      // success ratio depends on the function number
      function.setRandomSuccess((i+1) * 0.1);
      // execution time depends on the function number
      function.setRandomExecutionTime(MAX_EXECUTION_TIME * (i+1));
      function.time = Instant.now();

      writer.saveFunctionInvocation(function);
    }
  }

  private void randomWait(int maxMillis) {
    try {
      Thread.sleep((long) (Math.random()* maxMillis));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


}
