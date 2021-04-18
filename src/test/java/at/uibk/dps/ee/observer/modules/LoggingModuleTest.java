package at.uibk.dps.ee.observer.modules;

import at.uibk.dps.ee.observer.logging.jdbc.JdbcEnactmentLogger;
import at.uibk.dps.ee.observer.logging.logback.LogbackEnactmentLogger;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class LoggingModuleTest {


  @Test
  public void testConfigZeroLoggers() {
    LoggingModule module = new LoggingModule();
    LoggingModule moduleSpy = Mockito.spy(module);

    moduleSpy.setLogFunctionProperties(true);
    moduleSpy.config();

    verify(moduleSpy, never()).bindSingleLogger();
    verify(moduleSpy, never()).bindCompositeLogger();
    verify(moduleSpy, never()).addFunctionDecoratorFactory(any());

    moduleSpy.setLogFunctionProperties(false);
    moduleSpy.setUseDynamoDB(true);

    verify(moduleSpy, never()).bindSingleLogger();
    verify(moduleSpy, never()).bindCompositeLogger();
    verify(moduleSpy, never()).addFunctionDecoratorFactory(any());
  }

  @Test
  public void testConfigLogbackLogger() {
    LoggingModule module = new LoggingModule();
    LoggingModule moduleSpy = Mockito.spy(module);
    String logbackConfigPath = "./testpath/logback";

    doNothing().when(moduleSpy).bindSingleLogger();
    doNothing().when(moduleSpy).addFunctionDecoratorFactory(any());

    moduleSpy.setLogFunctionProperties(true);
    moduleSpy.setUseLogback(true);
    moduleSpy.setPathToLogbackConfiguration(logbackConfigPath);
    moduleSpy.config();

    verify(moduleSpy).bindSingleLogger();
    verify(moduleSpy, never()).bindCompositeLogger();

    assertTrue(moduleSpy.isLogFunctionProperties());
    assertTrue(moduleSpy.isUseLogback());
    assertEquals(logbackConfigPath, moduleSpy.getPathToLogbackConfiguration());
  }

  @Test
  public void testConfigInfluxDBLogger() {
    LoggingModule module = new LoggingModule();
    LoggingModule moduleSpy = Mockito.spy(module);
    String influxDBConfigPath = "./testpath/influxdb";

    doNothing().when(moduleSpy).bindSingleLogger();
    doNothing().when(moduleSpy).addFunctionDecoratorFactory(any());

    moduleSpy.setLogFunctionProperties(true);
    moduleSpy.setUseInfluxDB(true);
    moduleSpy.setPathToInfluxDBProperties(influxDBConfigPath);
    moduleSpy.config();

    verify(moduleSpy).bindSingleLogger();
    verify(moduleSpy, never()).bindCompositeLogger();

    assertTrue(moduleSpy.isUseInfluxDB());
    assertEquals(influxDBConfigPath, moduleSpy.getPathToInfluxDBProperties());
  }

  @Test
  public void testConfigDynamoDBLogger() {
    LoggingModule module = new LoggingModule();
    LoggingModule moduleSpy = Mockito.spy(module);
    String dynamoDBConfigPath = "./testpath/dynamodb";

    doNothing().when(moduleSpy).bindSingleLogger();
    doNothing().when(moduleSpy).addFunctionDecoratorFactory(any());

    moduleSpy.setLogFunctionProperties(true);
    moduleSpy.setUseDynamoDB(true);
    moduleSpy.setPathToDynamoDBProperties(dynamoDBConfigPath);
    moduleSpy.config();

    verify(moduleSpy).bindSingleLogger();
    verify(moduleSpy, never()).bindCompositeLogger();

    assertTrue(moduleSpy.isUseDynamoDB());
    assertEquals(dynamoDBConfigPath, moduleSpy.getPathToDynamoDBProperties());
  }

  @Test
  public void testConfigJdbcLogger() {
    LoggingModule module = new LoggingModule();
    LoggingModule moduleSpy = Mockito.spy(module);
    String jdbcConfigPath = "./testpath/jdbc";

    doNothing().when(moduleSpy).bindSingleLogger();
    doNothing().when(moduleSpy).addFunctionDecoratorFactory(any());

    moduleSpy.setLogFunctionProperties(true);
    moduleSpy.setUseJdbc(true);
    moduleSpy.setPathToJdbcProperties(jdbcConfigPath);
    moduleSpy.config();

    verify(moduleSpy).bindSingleLogger();
    verify(moduleSpy, never()).bindCompositeLogger();

    assertTrue(moduleSpy.isUseJdbc());
    assertEquals(jdbcConfigPath, moduleSpy.getPathToJdbcProperties());
  }

  @Test
  public void testConfigCompositeLogger() {
    LoggingModule module = new LoggingModule();
    LoggingModule moduleSpy = Mockito.spy(module);

    doNothing().when(moduleSpy).bindCompositeLogger();
    doNothing().when(moduleSpy).addFunctionDecoratorFactory(any());

    moduleSpy.setLogFunctionProperties(true);
    moduleSpy.setUseLogback(true);
    moduleSpy.setUseInfluxDB(true);
    moduleSpy.setUseDynamoDB(true);
    moduleSpy.setUseJdbc(true);
    moduleSpy.config();

    verify(moduleSpy, never()).bindSingleLogger();
    verify(moduleSpy).bindCompositeLogger();

    assertTrue(moduleSpy.isUseLogback());
    assertTrue(moduleSpy.isUseInfluxDB());
    assertTrue(moduleSpy.isUseDynamoDB());
    assertTrue(moduleSpy.isUseJdbc());
    assertTrue(moduleSpy.isLogFunctionProperties());
  }

  @Test
  public void testBindSingleLogger() {
    LoggingModule module = new LoggingModule();
    LoggingModule moduleSpy = Mockito.spy(module);
    doNothing().when(moduleSpy).addFunctionDecoratorFactory(any());

    moduleSpy.setLogFunctionProperties(true);
    moduleSpy.setUseLogback(true);

    Injector injector = Guice.createInjector(moduleSpy);
    EnactmentLogger logger = injector.getInstance(EnactmentLogger.class);
    assertTrue(logger instanceof LogbackEnactmentLogger);
  }

  @Test(expected = ConfigurationException.class)
  public void testBindSingleLoggerNoLogger() {
    LoggingModule module = new LoggingModule();
    LoggingModule moduleSpy = Mockito.spy(module);
    doNothing().when(moduleSpy).addFunctionDecoratorFactory(any());

    moduleSpy.setLogFunctionProperties(true);

    Injector injector = Guice.createInjector(moduleSpy);
    injector.getInstance(EnactmentLogger.class);
  }
}
