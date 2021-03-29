package at.markusmoosbrugger.logback;

import at.markusmoosbrugger.functioninvocation.FunctionInvocationSimulator;

public class LogbackRunner {
  public static void main(String[] args) {

    LogbackMySQLWriter writer = new LogbackMySQLWriter();
    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator();
    simulator.addWriter(writer);

    simulator.simulateMultipleFunctions(10, 5);
  }
}
