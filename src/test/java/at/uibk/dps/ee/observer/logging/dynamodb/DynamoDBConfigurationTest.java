package at.uibk.dps.ee.observer.logging.dynamodb;

import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.Properties;

import static org.junit.Assert.*;

public class DynamoDBConfigurationTest {

  @Test
  public void testValidConfiguration() {
    Properties properties = new Properties();
    properties.put("aws_access_key_id", "testaccesskeyid");
    properties.put("aws_secret_access_key", "testsecretaccesskey");
    properties.put("region", "testregion");
    properties.put("table", "testtable");

    DynamoDBConfiguration configuration = new DynamoDBConfiguration(properties);

    assertNotNull(configuration);
    assertEquals("testaccesskeyid", configuration.getAccessKeyId());
    assertEquals("testsecretaccesskey", configuration.getSecretAccessKey());
    assertEquals("testregion", configuration.getRegion());
    assertEquals("testtable", configuration.getTableName());
  }

  @Test(expected = InvalidParameterException.class)
  public void testEmptyConfiguration() {
    Properties properties = new Properties();
    DynamoDBConfiguration configuration = new DynamoDBConfiguration(properties);
    fail();
  }

  @Test(expected = InvalidParameterException.class)
  public void testIncompleteConfigurationNoAccessId(){
    Properties properties = new Properties();

    properties.put("aws_secret_access_key", "testsecretaccesskey");
    properties.put("region", "testregion");
    properties.put("table", "testtable");

    DynamoDBConfiguration configuration = new DynamoDBConfiguration(properties);
    fail();
  }

  @Test(expected = InvalidParameterException.class)
  public void testIncompleteConfigurationNoSecretKey() {
    Properties properties = new Properties();

    properties.put("aws_access_key_id", "testaccesskeyid");
    properties.put("region", "testregion");
    properties.put("table", "testtable");

    DynamoDBConfiguration configuration = new DynamoDBConfiguration(properties);
    fail();
  }
  @Test(expected = InvalidParameterException.class)
  public void testIncompleteConfigurationNoRegion() {
    Properties properties = new Properties();
    properties.put("aws_access_key_id", "testaccesskeyid");
    properties.put("aws_secret_access_key", "testsecretaccesskey");
    properties.put("table", "testtable");

    DynamoDBConfiguration configuration = new DynamoDBConfiguration(properties);
    fail();
  }

  @Test(expected = InvalidParameterException.class)
  public void testIncompleteConfigurationNoTable() {
    Properties properties = new Properties();
    properties.put("aws_access_key_id", "testaccesskeyid");
    properties.put("aws_secret_access_key", "testsecretaccesskey");
    properties.put("region", "testregion");

    DynamoDBConfiguration configuration = new DynamoDBConfiguration(properties);
    fail();
  }

}
