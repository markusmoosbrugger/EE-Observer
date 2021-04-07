package at.markusmoosbrugger.logging.logback;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link LogbackEnactmentLogger} is used to log information about the enactment. Depending
 * on the configuration of logback this data may be logged to STDOUT, a log file or a
 * relational database.
 *
 * @author Markus Moosbrugger
 */
public class LogbackEnactmentLogger implements EnactmentLogger {
  protected final Logger logger = LoggerFactory.getLogger(LogbackEnactmentLogger.class);

  @Override
  public void logEnactment(final EnactmentLogEntry entry) {
    logger.info("TYPE {} ID {} EXEC TIME {} ms SUCCESS {} INPUT COMPLEXITY {}.", entry.getFunctionType(),
        entry.getFunctionId(), entry.getExecutionTime(), entry.isSuccess(), entry.getInputComplexity());
  }
}
