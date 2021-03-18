package at.markusmoosbrugger.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public class DBLogger {

  Logger logger = LoggerFactory.getLogger(DBLogger.class);

  public void logFunctionInvocation(String id, String type, long executionTime, boolean success) {
    logger.info("TYPE {} ID {} EXEC TIME {} milliseconds SUCCESS {}.", type,
        id, executionTime, success);
  }
}
