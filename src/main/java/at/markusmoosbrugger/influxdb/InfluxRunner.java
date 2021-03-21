package at.markusmoosbrugger.influxdb;

import ch.qos.logback.classic.util.ContextInitializer;

public class InfluxRunner {

  static String pathToInfluxDBConfiguration = "./logging/config/logback.xml";

  public static void main(final String[] args) {

    System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, pathToInfluxDBConfiguration);

    FunctionSimulator simulator = new FunctionSimulator();
    simulator.simulateMultipleFunctions(10, 10);

  }
}
