package at.markusmoosbrugger.dynamodb;

import at.markusmoosbrugger.functioninvocation.FunctionInvocation;
import at.markusmoosbrugger.functioninvocation.FunctionInvocationWriter;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DynamoDBWriter implements FunctionInvocationWriter {

  protected static String pathToPropertiesFile = "./database/dynamodb.properties";
  protected Logger logger;
  protected Table invocationsTable;
  protected String tableName;


  public DynamoDBWriter() {
    logger = LoggerFactory.getLogger(DynamoDBWriter.class);
    BasicAWSCredentials awsCreds = null;
    try (InputStream input = new FileInputStream(pathToPropertiesFile)) {
      Properties properties = new Properties();
      properties.load(input);

      String accessKeyId = (String) properties.get("aws_access_key_id");
      String secretAccessKey = (String) properties.get("aws_secret_access_key");
      awsCreds = new BasicAWSCredentials(accessKeyId, secretAccessKey);

      this.tableName = (String) properties.get("table");

    } catch (FileNotFoundException e) {
      logger.error("Credentials file not found. ", e);
    } catch (IOException e) {
      logger.error("IO Exception while reading credentials file. ", e);
    }
    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
        .withRegion(Regions.EU_CENTRAL_1).build();


    DynamoDB dynamoDB = new DynamoDB(client);
    this.invocationsTable = dynamoDB.getTable(this.tableName);
  }



  @Override public void saveFunctionInvocation(FunctionInvocation invocation) {
    try {
      invocationsTable.putItem(new Item()
          .withPrimaryKey("functionId", invocation.getFunctionId(), "timestamp",
              invocation.getTime().toEpochMilli()).withBoolean("success", invocation.isSuccess())
          .withString("functionType", invocation.getFunctionType())
          .withDouble("executionTime", invocation.getExecutionTime()));

      logger.info("TYPE {} ID {} EXEC TIME {} milliseconds SUCCESS {}.", invocation.getFunctionId(),
          invocation.getFunctionType(), invocation.getExecutionTime(), invocation.isSuccess());
    } catch (Exception e) {
      logger.error("Unable to log function invocation. ", e);
    }
  }
}
