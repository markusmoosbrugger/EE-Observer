package at.uibk.dps.ee.observer;

import at.uibk.dps.ee.observer.logging.dynamodb.DynamoDBEnactmentLogger;
import at.uibk.dps.ee.observer.logging.influxdb.InfluxDBEnactmentLogger;
import at.uibk.dps.ee.observer.logging.logback.LogbackEnactmentLogger;
import at.uibk.dps.ee.observer.simulator.FunctionInvocationSimulator;
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
