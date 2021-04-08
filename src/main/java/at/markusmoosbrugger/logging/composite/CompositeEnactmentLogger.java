package at.markusmoosbrugger.logging.composite;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;

import javax.inject.Inject;
import java.util.Set;

/**
 * The {@link CompositeEnactmentLogger} is used to log information about the
 * enactment with multiple loggers.
 *
 * @author Markus Moosbrugger
 */
public class CompositeEnactmentLogger implements EnactmentLogger {

  protected Set<EnactmentLogger> enactmentLoggers;

  /**
   * The default constructor.
   *
   * @param enactmentLoggers the set of enactment loggers
   */
  @Inject
  public CompositeEnactmentLogger(final Set<EnactmentLogger> enactmentLoggers) {
    this.enactmentLoggers = enactmentLoggers;
  }

  @Override
  public void logEnactment(final EnactmentLogEntry entry) {
    enactmentLoggers.stream().forEach(logger -> logger.logEnactment(entry));
  }
}
