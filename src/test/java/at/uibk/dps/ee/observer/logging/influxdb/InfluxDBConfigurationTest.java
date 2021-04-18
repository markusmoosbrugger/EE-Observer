package at.uibk.dps.ee.observer.logging.influxdb;

import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.Properties;

import static org.junit.Assert.*;

public class InfluxDBConfigurationTest {

  @Test
  public void testValidConfiguration() {
    Properties properties = new Properties();
    properties.put("token", "testtoken");
    properties.put("bucket", "testbucket");
    properties.put("org", "testorg");
    properties.put("url", "testurl");

    InfluxDBConfiguration configuration = new InfluxDBConfiguration(properties);

    assertNotNull(configuration);
    assertEquals("testtoken", configuration.getToken());
    assertEquals("testbucket", configuration.getBucket());
    assertEquals("testorg", configuration.getOrganization());
    assertEquals("testurl", configuration.getUrl());
  }

  @Test(expected = InvalidParameterException.class)
  public void testEmptyConfiguration() {
    Properties properties = new Properties();
    InfluxDBConfiguration configuration = new InfluxDBConfiguration(properties);

    fail();
  }

  @Test(expected = InvalidParameterException.class)
  public void testIncompleteConfigurationNoToken() {
    Properties properties = new Properties();
    properties.put("bucket", "testbucket");
    properties.put("org", "testorg");
    properties.put("url", "testurl");

    InfluxDBConfiguration configuration = new InfluxDBConfiguration(properties);
    fail();
  }

  @Test(expected = InvalidParameterException.class)
  public void testIncompleteConfigurationNoBucket() {
    Properties properties = new Properties();
    properties.put("token", "testtoken");
    properties.put("org", "testorg");
    properties.put("url", "testurl");

    InfluxDBConfiguration configuration = new InfluxDBConfiguration(properties);
    fail();
  }

  @Test(expected = InvalidParameterException.class)
  public void testIncompleteConfigurationNoOrg() {
    Properties properties = new Properties();
    properties.put("token", "testtoken");
    properties.put("bucket", "testbucket");
    properties.put("url", "testurl");

    InfluxDBConfiguration configuration = new InfluxDBConfiguration(properties);
    fail();
  }

  @Test(expected = InvalidParameterException.class)
  public void testIncompleteConfigurationNoUrl() {
    Properties properties = new Properties();
    properties.put("token", "testtoken");
    properties.put("bucket", "testbucket");
    properties.put("org", "testorg");

    InfluxDBConfiguration configuration = new InfluxDBConfiguration(properties);
    fail();
  }

}
