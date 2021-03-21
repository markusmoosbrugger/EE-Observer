package at.markusmoosbrugger.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.util.StatusPrinter;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class Runner {
  static String pathToLoggingConfiguration = "./logging/config/logback.xml";

  public static void main(String[] args) {
    // configure the location of the logback config file
    System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, pathToLoggingConfiguration);

    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    // print logback's internal status
    StatusPrinter.print(lc);

    DBLogger dbLogger = new DBLogger();

    Logger logger = LoggerFactory.getLogger(Runner.class);
    JsonObject input = new JsonObject();
    input.add("inputValue", new JsonPrimitive(5));

    JsonObject output = new JsonObject();
    output.add("result", new JsonPrimitive(10));

    logger.debug("Starting function execution...");
    String type = "Test type";
    for (int i = 1; i <= 10; i++) {
      Instant start = Instant.now();
      boolean success = true;
      try {
        randomWaitFunction();
      } catch (InterruptedException e) {
        success = false;
        logger.error("Error while executing random wait function. ");
      }
      dbLogger.logFunctionInvocation(UUID.randomUUID().toString(), type, Duration.between(start,
          Instant.now()).toMillis(), success, input, output);
    }
    logger.debug("Function execution finished.");
  }

  public static long randomWaitFunction() throws InterruptedException {
    long randomValue = (long)(Math.random() * 1000);
    Thread.sleep(randomValue);

    // throw fake exception in 10% of the cases to test logging
    if (randomValue <100) {
      throw new InterruptedException();
    }

    return randomValue;
  }
}
