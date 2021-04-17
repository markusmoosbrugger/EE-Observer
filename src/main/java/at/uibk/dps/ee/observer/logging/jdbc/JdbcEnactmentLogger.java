package at.uibk.dps.ee.observer.logging.jdbc;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import at.uibk.dps.ee.observer.logging.properties.PropertiesReader;
import com.google.inject.Inject;
import org.opt4j.core.start.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The {@link JdbcEnactmentLogger} is used to log information about the enactment to various
 * databases via the Java Database Connectivity (JDBC) interface. JDBC supports different databases
 * such as MySQL, PostgreSQL or Microsoft SQL Server. The current implementation of this class,
 * however, supports only MySQL databases and the method {@link #initConnection()} must be adapted
 * in order to support other databases.
 *
 * @author Markus Moosbrugger
 */
public class JdbcEnactmentLogger implements EnactmentLogger {

  protected JdbcConfiguration configuration;
  protected Connection connection;
  protected final Logger logger = LoggerFactory.getLogger(JdbcEnactmentLogger.class);

  /**
   * Default constructor. Reads the database configuration properties from the specified properties
   * file and calls the method to initialize the connection.
   */
  @Inject
  public JdbcEnactmentLogger(
      @Constant(value = "pathToJdbcProperties", namespace = JdbcEnactmentLogger.class)
      final String pathToPropertiesFile) {
    Properties properties = PropertiesReader.readProperties(pathToPropertiesFile);
    this.configuration = new JdbcConfiguration(properties);
    this.connection = initConnection();
  }

  /**
   * Additional constructor which can be used to provide a SQL connection.
   *
   * @param connection the SQL connection
   */
  public JdbcEnactmentLogger(Connection connection) {
    this.connection = connection;
  }

  /**
   * Initializes the connection to the MySQL database.
   *
   * @return the connection
   */
  private Connection initConnection() {
    String url = "jdbc:mysql://" + configuration.getInstance() + ":" + configuration.getPort() + "/"
        + configuration.getDatabaseName();
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(url, configuration.getUser(), configuration.getPassword());
    } catch (SQLException e) {
      logger.error("SQL exception while initializing the database connection.", e);
    }
    return conn;
  }

  @Override
  public void logEnactment(EnactmentLogEntry entry) {
    String statementString = "INSERT INTO enactment "
        + "(timestamp, functionType, functionId, executionTime, inputComplexity, success) "
        + "values (?,?,?,?,?,?)";

    try (PreparedStatement preparedStatement = connection.prepareStatement(statementString)) {
      preparedStatement.setLong(1, entry.getTimestamp().toEpochMilli());
      preparedStatement.setString(2, entry.getFunctionType());
      preparedStatement.setString(3, entry.getFunctionId());
      preparedStatement.setDouble(4, entry.getExecutionTime());
      preparedStatement.setDouble(5, entry.getInputComplexity());
      preparedStatement.setBoolean(6, entry.isSuccess());

      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      logger.error("Logging of enactment entry {} failed.", entry, e);
    }
  }
}
