package at.uibk.dps.ee.observer.logging.jdbc;

import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.Properties;

import static org.junit.Assert.*;

public class JdbcConfigurationTest {
  @Test
  public void testValidConfiguration() {
    Properties properties = new Properties();
    properties.put("user", "testuser");
    properties.put("password", "testpassword");
    properties.put("db_instance", "testinstance");
    properties.put("db_port", "1234");
    properties.put("db_name", "testdbname");

    JdbcConfiguration configuration = new JdbcConfiguration(properties);

    assertNotNull(configuration);
    assertEquals("testuser", configuration.getUser());
    assertEquals("testpassword", configuration.getPassword());
    assertEquals("testinstance", configuration.getInstance());
    assertEquals(1234, configuration.getPort());
    assertEquals("testdbname", configuration.getDatabaseName());
  }

  @Test(expected = InvalidParameterException.class)
  public void testEmptyConfiguration() {
    Properties properties = new Properties();
    JdbcConfiguration configuration = new JdbcConfiguration(properties);

    fail();
  }

  @Test(expected = InvalidParameterException.class)
  public void testIncompleteConfiguration() {
    Properties properties = new Properties();
    properties.put("user", "testuser");
    properties.put("password", "testpassword");
    properties.put("db_instance", "testinstance");
    properties.put("db_port", "1234");

    JdbcConfiguration configuration = new JdbcConfiguration(properties);

    fail();
  }
}
