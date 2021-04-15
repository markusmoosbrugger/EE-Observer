package at.uibk.dps.ee.observer.logging.logback;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LogbackEnactmentLoggerTest {

  @Test public void testLogEnactment() {
    String id = "id";
    String type = "type";
    double executionTime = 1.12;
    boolean success = true;
    double inputComplexity = 0.8;

    LogbackEnactmentLogger logbackEnactmentLogger = new LogbackEnactmentLogger();
    Logger logger = (Logger) logbackEnactmentLogger.logger;

    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    listAppender.start();
    logger.addAppender(listAppender);

    String expectedLogEntry = "TYPE " + type + " ID " + id + " EXEC TIME " + executionTime + " ms"
        + " SUCCESS " + success + " INPUT COMPLEXITY "+ inputComplexity + ".";

    EnactmentLogEntry entry = new EnactmentLogEntry(Instant.now(), id, type, executionTime, success,
        inputComplexity);
    logbackEnactmentLogger.logEnactment(entry);


    List<ILoggingEvent> logList = listAppender.list;
    assertEquals(1, logList.size());
    assertEquals(Level.INFO, logList.get(0).getLevel());
    assertEquals(expectedLogEntry,
        logList.get(0).getFormattedMessage());
  }
}
