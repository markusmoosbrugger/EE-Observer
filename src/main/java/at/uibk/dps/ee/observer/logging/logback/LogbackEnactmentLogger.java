package at.uibk.dps.ee.observer.logging.logback;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
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
  protected final Logger logger = LoggerFactory.getLogger(LogbackEnactmentLogger.class);

  @Override
  public void logEnactment(final EnactmentLogEntry entry) {
    StringBuffer attrBuffer = new StringBuffer();
    entry.getAdditionalAttributes().forEach(
        attr -> attrBuffer.append(attr.getKey()).append(':').append(attr.getValue()).append('\n'));

    logger.info("TYPE ID {} EnactmentMode {} Implementation ID {} Additional Attributes {} EXEC "
            + "TIME {} milliseconds SUCCESS {} INPUT COMPLEXITY {}.", entry.getTypeId(),
        entry.getEnactmentMode(), entry.getImplementationId(), attrBuffer,
        entry.getExecutionTime(),
        entry.isSuccess(),
        entry.getInputComplexity());
  }
}
