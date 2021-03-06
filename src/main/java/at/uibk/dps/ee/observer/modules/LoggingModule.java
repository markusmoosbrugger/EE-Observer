package at.uibk.dps.ee.observer.modules;

import at.uibk.dps.ee.observer.logging.composite.CompositeEnactmentLogger;
import at.uibk.dps.ee.observer.logging.dynamodb.DynamoDBEnactmentLogger;
import at.uibk.dps.ee.observer.logging.influxdb.InfluxDBEnactmentLogger;
import at.uibk.dps.ee.observer.logging.jdbc.JdbcEnactmentLogger;
import at.uibk.dps.ee.observer.logging.logback.LogbackEnactmentLogger;
import at.uibk.dps.ee.enactables.decorators.DecoratorEnactmentLogFactory;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import at.uibk.dps.ee.guice.modules.FunctionModule;
import ch.qos.logback.classic.util.ContextInitializer;
import com.google.inject.multibindings.Multibinder;
import org.opt4j.core.config.annotations.File;
import org.opt4j.core.config.annotations.Info;
import org.opt4j.core.config.annotations.Order;
import org.opt4j.core.config.annotations.Required;
import org.opt4j.core.start.Constant;

import java.util.Arrays;
import java.util.List;

/**
 * The {@link LoggingModule} defines the dynamic aspects of the configuration
 * of loggers.
 *
 * @author Markus Moosbrugger
 */
public class LoggingModule extends FunctionModule {

  @Order(1)
  @Info("If checked, the execution properties of functions will be logged.")
  public boolean logFunctionProperties;

  @Order(2)
  @Constant(value = "prio", namespace = DecoratorEnactmentLogFactory.class)
  @Required(property = "logFunctionProperties")
  @Info("Decorators with lower prio are applied later.")
  public int enactmentLoggingDecoratorPriority = 10;

  @Order(3)
  @Info("If checked, the execution properties are logged with Logback.")
  @Required(property = "logFunctionProperties")
  public boolean useLogback;

  @Order(4)
  @Info("The path to the logback configuration file.")
  @File
  @Required(property = "useLogback")
  @Constant(value = "pathToLogbackConfiguration", namespace = LogbackEnactmentLogger.class)
  public String pathToLogbackConfiguration = "./logging/config/logback.xml";

  @Order(5)
  @Info("If checked, the execution properties are logged to InfluxDB.")
  @Required(property = "logFunctionProperties")
  public boolean useInfluxDB;

  @Order(6)
  @Info("The path to the InfluxDB properties file.")
  @File
  @Required(property = "useInfluxDB")
  @Constant(value = "pathToInfluxDBProperties", namespace = InfluxDBEnactmentLogger.class)
  public String pathToInfluxDBProperties = "./logging/config/database/influxdb/influxdb.properties";

  @Order(7)
  @Info("If checked, the execution properties are logged to DynamoDB.")
  @Required(property = "logFunctionProperties")
  public boolean useDynamoDB;

  @Order(8)
  @Info("The path to the DynamoDB properties file.")
  @File
  @Required(property = "useDynamoDB")
  @Constant(value = "pathToDynamoDBProperties", namespace = DynamoDBEnactmentLogger.class)
  public String pathToDynamoDBProperties = "./logging/config/database/dynamodb/dynamodb.properties";

  @Order(9)
  @Info("If checked, the execution properties are logged to a JDBC compatible database.")
  @Required(property = "logFunctionProperties")
  public boolean useJdbc;

  @Order(10)
  @Info("The path to the JDBC properties file.")
  @File
  @Required(property = "useJdbc")
  @Constant(value = "pathToJdbcProperties", namespace = JdbcEnactmentLogger.class)
  public String pathToJdbcProperties = "./logging/config/database/jdbc/jdbc.properties";


  @Override
  protected void config() {
    if (logFunctionProperties) {
      final long loggerCount =
          getLoggerCount(Arrays.asList(new Boolean[] {useLogback, useInfluxDB, useDynamoDB, useJdbc}));

      if (loggerCount == 0) {
        return;
      } else if (loggerCount == 1) {
        bindSingleLogger();
      } else if (loggerCount > 1) {
        bindCompositeLogger();
      }
      // add the function wrapper
      addFunctionDecoratorFactory(DecoratorEnactmentLogFactory.class);
    }
  }

  /**
   * Binds a composite enactment logger to the {@link EnactmentLogger} interface
   * and adds the other selected loggers to the multibinder.
   */
  protected void bindCompositeLogger() {
    bind(EnactmentLogger.class).to(CompositeEnactmentLogger.class);
    final Multibinder<EnactmentLogger> multibinder =
        Multibinder.newSetBinder(binder(), EnactmentLogger.class);

    if (useLogback) {
      // configure the location of the logback config file
      // must be set before the first call to LoggerFactory.getLogger();
      System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, pathToLogbackConfiguration);
      multibinder.addBinding().to(LogbackEnactmentLogger.class);
    }
    if (useInfluxDB) {
      multibinder.addBinding().to(InfluxDBEnactmentLogger.class);
    }
    if (useDynamoDB) {
      multibinder.addBinding().to(DynamoDBEnactmentLogger.class);
    }
    if (useJdbc) {
      multibinder.addBinding().to(JdbcEnactmentLogger.class);
    }
  }

  /**
   * Binds a single enactment logger to the {@link EnactmentLogger} interface.
   */
  protected void bindSingleLogger() {
    if (useLogback) {
      // configure the location of the logback config file
      System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, pathToLogbackConfiguration);
      bind(EnactmentLogger.class).to(LogbackEnactmentLogger.class);
    } else if (useInfluxDB) {
      bind(EnactmentLogger.class).to(InfluxDBEnactmentLogger.class);
    } else if (useDynamoDB) {
      bind(EnactmentLogger.class).to(DynamoDBEnactmentLogger.class);
    } else if (useJdbc) {
      bind(EnactmentLogger.class).to(JdbcEnactmentLogger.class);
    }
  }

  /**
   * Returns the number of true values in the given list of booleans.
   *
   * @param useLoggerFlagsList the list of boolean values where a true value
   *                           specifies that the respective logger is used
   * @return the total number of loggers that are to be used
   */
  protected long getLoggerCount(final List<Boolean> useLoggerFlagsList) {
    return useLoggerFlagsList.stream().filter(Boolean::booleanValue).count();
  }

  public String getPathToLogbackConfiguration() {
    return pathToLogbackConfiguration;
  }

  public void setPathToLogbackConfiguration(final String pathToLogbackConfiguration) {
    this.pathToLogbackConfiguration = pathToLogbackConfiguration;
  }

  public String getPathToInfluxDBProperties() {
    return pathToInfluxDBProperties;
  }

  public void setPathToInfluxDBProperties(final String pathToInfluxDBProperties) {
    this.pathToInfluxDBProperties = pathToInfluxDBProperties;
  }

  public String getPathToDynamoDBProperties() {
    return pathToDynamoDBProperties;
  }

  public void setPathToDynamoDBProperties(final String pathToDynamoDBProperties) {
    this.pathToDynamoDBProperties = pathToDynamoDBProperties;
  }

  public String getPathToJdbcProperties() {
    return pathToJdbcProperties;
  }

  public void setPathToJdbcProperties(String pathToJdbcProperties) {
    this.pathToJdbcProperties = pathToJdbcProperties;
  }

  public boolean isLogFunctionProperties() {
    return logFunctionProperties;
  }

  public void setLogFunctionProperties(final boolean logFunctionProperties) {
    this.logFunctionProperties = logFunctionProperties;
  }

  public int getEnactmentLoggingDecoratorPriority() {
    return enactmentLoggingDecoratorPriority;
  }

  public void setEnactmentLoggingDecoratorPriority(final int enactmentLoggingDecoratorPriority) {
    this.enactmentLoggingDecoratorPriority = enactmentLoggingDecoratorPriority;
  }

  public boolean isUseLogback() {
    return useLogback;
  }

  public void setUseLogback(final boolean useLogback) {
    this.useLogback = useLogback;
  }

  public boolean isUseInfluxDB() {
    return useInfluxDB;
  }

  public void setUseInfluxDB(final boolean useInfluxDB) {
    this.useInfluxDB = useInfluxDB;
  }

  public boolean isUseDynamoDB() {
    return useDynamoDB;
  }

  public void setUseDynamoDB(final boolean useDynamoDB) {
    this.useDynamoDB = useDynamoDB;
  }

  public boolean isUseJdbc() {
    return useJdbc;
  }

  public void setUseJdbc(boolean useJdbc) {
    this.useJdbc = useJdbc;
  }

}
