package at.markusmoosbrugger.modules;

import at.markusmoosbrugger.logging.logback.LogbackEnactmentLogger;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

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
  public void testConfigSingleLogger() {
    LoggingModule module = new LoggingModule();
    LoggingModule moduleSpy = Mockito.spy(module);

    doNothing().when(moduleSpy).bindSingleLogger();
    doNothing().when(moduleSpy).addFunctionDecoratorFactory(any());

    moduleSpy.setLogFunctionProperties(true);
    moduleSpy.setUseLogback(true);
    moduleSpy.config();

    verify(moduleSpy).bindSingleLogger();
    verify(moduleSpy, never()).bindCompositeLogger();
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
    moduleSpy.config();

    verify(moduleSpy, never()).bindSingleLogger();
    verify(moduleSpy).bindCompositeLogger();
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
