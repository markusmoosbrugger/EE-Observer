package at.markusmoosbrugger.dynamodb;

import at.markusmoosbrugger.functioninvocation.FunctionInvocationSimulator;

public class DynamoRunner {
  protected static String pathToPropertiesFile = "./src/main/resources/dynamodb.credentials";

  public static void main(String[] args) {
    DynamoDBWriter writer = new DynamoDBWriter();

    FunctionInvocationSimulator simulator = new FunctionInvocationSimulator(writer);

    simulator.simulateMultipleFunctions(5, 5);
  }

}
