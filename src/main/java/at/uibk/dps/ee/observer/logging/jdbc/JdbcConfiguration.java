package at.uibk.dps.ee.observer.logging.jdbc;

import java.security.InvalidParameterException;
import java.util.Properties;

/**
 *
 * @author Markus Moosbrugger
 */
public class JdbcConfiguration {
  protected String user;
  protected String password;
  protected String instance;
  protected int port;
  protected String databaseName;

  public JdbcConfiguration(Properties properties) {
    validateProperties(properties);
    this.user = properties.getProperty("user");
    this.password = properties.getProperty("password");
    this.instance = properties.getProperty("db_instance");
    this.port = Integer.valueOf(properties.getProperty("db_port"));
    this.databaseName = properties.getProperty("db_name");
  }

  protected void validateProperties(Properties properties) {
    if (!properties.containsKey("user") || !properties.containsKey("password") || !properties
        .containsKey("db_instance") || !properties.containsKey("db_name") || !properties
        .containsKey("db_port")) {
      throw new InvalidParameterException("Property file does not contain all required keys.");
    }
  }

  public String getUser() {
    return user;
  }

  public String getPassword() {
    return password;
  }

  public String getInstance() {
    return instance;
  }

  public int getPort() {
    return port;
  }

  public String getDatabaseName() {
    return databaseName;
  }
}
