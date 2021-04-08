package at.markusmoosbrugger;

import at.markusmoosbrugger.logging.dynamodb.DynamoDBEnactmentLogger;
import at.markusmoosbrugger.logging.influxdb.InfluxDBEnactmentLogger;
import at.markusmoosbrugger.logging.logback.LogbackEnactmentLogger;
import at.markusmoosbrugger.simulator.FunctionInvocationSimulator;

/**
 * Runner which creates different loggers and runs the simulation.
 */
public class Runner {
  public static void main(String[] args) {
    LogbackEnactmentLogger logbackLogger =
        new LogbackEnactmentLogger("./logging/config/logback.xml");
    InfluxDBEnactmentLogger influxLogger =
        new InfluxDBEnactmentLogger("./database/influxdb/influxdb.properties");
    DynamoDBEnactmentLogger dynamoLogger =
        new DynamoDBEnactmentLogger("./database/dynamodb/dynamodb.properties");


    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator();
    simulator.addLogger(logbackLogger);
    simulator.addLogger(influxLogger);
    simulator.addLogger(dynamoLogger);

    simulator.simulateMultipleFunctions(10, 1);
  }
}
