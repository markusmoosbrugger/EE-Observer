package at.markusmoosbrugger.logback;

import at.markusmoosbrugger.functioninvocation.FunctionInvocation;
import at.markusmoosbrugger.functioninvocation.FunctionInvocationWriter;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogbackMySQLWriter implements FunctionInvocationWriter {

  protected Logger logger;
  static String pathToLoggingConfiguration = "./logging/config/logback.xml";

  public LogbackMySQLWriter() {
    // configure the location of the logback config file
    System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, pathToLoggingConfiguration);

    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    // print logback's internal status
    StatusPrinter.print(lc);

    logger = LoggerFactory.getLogger(LogbackMySQLWriter.class);
  }

  @Override public void saveFunctionInvocation(FunctionInvocation invocation) {
    logger.info("ID {} TYPE {} EXEC TIME {} milliseconds SUCCESS {} INPUT {} OUTPUT {}.",
        invocation.getFunctionId(), invocation.getFunctionType(), invocation.getExecutionTime(),
        invocation.isSuccess(), invocation.getInput(), invocation.getOutput());
  }
}
