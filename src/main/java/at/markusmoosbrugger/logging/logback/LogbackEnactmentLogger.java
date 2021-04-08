package at.markusmoosbrugger.logging.logback;

import at.markusmoosbrugger.logging.influxdb.InfluxDBEnactmentLogger;
import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import ch.qos.logback.classic.util.ContextInitializer;
import com.google.inject.Inject;
import com.influxdb.client.InfluxDBClientFactory;
import org.opt4j.core.start.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link LogbackEnactmentLogger} is used to log information about the
 * enactment. Depending on the configuration of logback this data may be logged
 * to STDOUT, a log file or a relational database.
 *
 * @author Markus Moosbrugger
 */
public class LogbackEnactmentLogger implements EnactmentLogger {
  protected Logger logger;


  /**
   * Default constructor. Reads the database configuration properties from the
   * specified properties file and creates an InfluxDB client.
   */
  @Inject
  public LogbackEnactmentLogger(
      @Constant(value = "pathToLogbackConfiguration", namespace = LogbackEnactmentLogger.class)
      final String pathToLogbackConfiguration) {
    // configure the location of the logback config file
    System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, pathToLogbackConfiguration);
    logger = LoggerFactory.getLogger(LogbackEnactmentLogger.class);
  }


  @Override
  public void logEnactment(final EnactmentLogEntry entry) {
    logger.info("TYPE {} ID {} EXEC TIME {} ms SUCCESS {} INPUT COMPLEXITY {}.", entry.getFunctionType(),
        entry.getFunctionId(), entry.getExecutionTime(), entry.isSuccess(), entry.getInputComplexity());
  }
}
