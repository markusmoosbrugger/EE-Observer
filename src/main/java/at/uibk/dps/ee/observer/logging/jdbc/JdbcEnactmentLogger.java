package at.uibk.dps.ee.observer.logging.jdbc;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import at.uibk.dps.ee.observer.logging.configuration.PropertiesReader;
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
  }

  /**
   * Additional zero-argument constructor used for unit tests.
   */
  public JdbcEnactmentLogger() {
  }

  protected Connection getConnection() throws SQLException {
    String url = "jdbc:mysql://" + configuration.getInstance() + ":" + configuration.getPort() + "/"
        + configuration.getDatabaseName();
    return DriverManager.getConnection(url, configuration.getUser(), configuration.getPassword());
  }

  @Override
  public void logEnactment(EnactmentLogEntry entry) {
    String statementString = "INSERT INTO enactment "
        + "(timestamp, typeId, enactmentMode, implementationId, executionTime, inputComplexity, "
        + "success) values (?,?,?,?,?,?,?)";

    try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(statementString)) {
      preparedStatement.setLong(1, entry.getTimestamp().toEpochMilli());
      preparedStatement.setString(2, entry.getTypeId());
      preparedStatement.setString(3, entry.getEnactmentMode());
      preparedStatement.setString(4, entry.getImplementationId());
      preparedStatement.setDouble(5, entry.getExecutionTime());
      preparedStatement.setDouble(6, entry.getInputComplexity());
      preparedStatement.setBoolean(7, entry.isSuccess());

      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      logger.error("Logging of enactment entry {} failed.", entry, e);
    }
  }
}
