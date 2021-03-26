package at.markusmoosbrugger.influxdb;

import at.markusmoosbrugger.functioninvocation.FunctionInvocationSimulator;
import ch.qos.logback.classic.util.ContextInitializer;

public class InfluxRunner {

  static String pathToInfluxDBConfiguration = "./logging/config/logback.xml";

  public static void main(final String[] args) {

    System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, pathToInfluxDBConfiguration);

    InfluxDBWriter writer = new InfluxDBWriter();
    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator(writer);

    simulator.simulateMultipleFunctions(10, 10);

  }
}
