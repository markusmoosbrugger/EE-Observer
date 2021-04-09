package at.markusmoosbrugger;

import at.markusmoosbrugger.logging.dynamodb.DynamoDBEnactmentLogger;
import at.markusmoosbrugger.logging.influxdb.InfluxDBEnactmentLogger;
import at.markusmoosbrugger.logging.logback.LogbackEnactmentLogger;
import at.markusmoosbrugger.simulator.FunctionInvocationSimulator;
import ch.qos.logback.classic.util.ContextInitializer;

/**
 * Runner which creates different loggers and runs the simulation.
 */
public class Runner {
  public static void main(String[] args) {
    // configure the location of the logback config file
    System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "./logging/config/logback.xml");

    LogbackEnactmentLogger logbackLogger = new LogbackEnactmentLogger();
    InfluxDBEnactmentLogger influxLogger =
        new InfluxDBEnactmentLogger("./config/database/influxdb/influxdb.properties");
    DynamoDBEnactmentLogger dynamoLogger =
        new DynamoDBEnactmentLogger("./config/database/dynamodb/dynamodb.properties");


    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator();
    simulator.addLogger(logbackLogger);
    simulator.addLogger(influxLogger);
    simulator.addLogger(dynamoLogger);

    simulator.simulateMultipleFunctions(10, 1);
  }
}
