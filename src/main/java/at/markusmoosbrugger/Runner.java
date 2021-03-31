package at.markusmoosbrugger;

import at.markusmoosbrugger.dynamodb.DynamoDBWriter;
import at.markusmoosbrugger.functioninvocation.FunctionInvocationSimulator;
import at.markusmoosbrugger.influxdb.InfluxDBWriter;
import at.markusmoosbrugger.logback.LogbackMySQLWriter;

public class Runner {
  public static void main(String[] args) {
    LogbackMySQLWriter logbackWriter = new LogbackMySQLWriter();
    InfluxDBWriter influxWriter = new InfluxDBWriter();
    DynamoDBWriter dynamoDBWriter = new DynamoDBWriter();

    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator();
    simulator.addWriter(logbackWriter);
    simulator.addWriter(influxWriter);
    simulator.addWriter(dynamoDBWriter);

    simulator.simulateMultipleFunctions(10, 1);
  }
}
