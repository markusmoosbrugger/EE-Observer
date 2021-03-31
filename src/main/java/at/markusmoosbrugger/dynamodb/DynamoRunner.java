package at.markusmoosbrugger.dynamodb;

import at.markusmoosbrugger.functioninvocation.FunctionInvocationSimulator;

public class DynamoRunner {
  protected static String pathToPropertiesFile = "./database/dynamodb.credentials";

  public static void main(String[] args) {
    DynamoDBWriter writer = new DynamoDBWriter();

    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator();
    simulator.addWriter(writer);

    simulator.simulateMultipleFunctions(5, 5);
  }

}
