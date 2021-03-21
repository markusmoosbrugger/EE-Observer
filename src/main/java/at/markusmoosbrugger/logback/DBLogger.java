package at.markusmoosbrugger.logback;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DBLogger {

  Logger logger = LoggerFactory.getLogger(DBLogger.class);

  public void logFunctionInvocation(String id, String type, long executionTime, boolean success,
      JsonObject input, JsonObject output) {
    logger.info("TYPE {} ID {} EXEC TIME {} milliseconds SUCCESS {} INPUT {} OUTPUT {}.", id,
        type, executionTime, success, input, output);

    // Example to log something into table logging_event_exception
    //logger.error("Exception", new RuntimeException("Testing Exception"));
  }
}
