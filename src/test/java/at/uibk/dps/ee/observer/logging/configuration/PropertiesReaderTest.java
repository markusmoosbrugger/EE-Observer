package at.uibk.dps.ee.observer.logging.configuration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PropertiesReaderTest {

  public static String testPropertiesPathInfluxDB = "./src/test/resources/influxdb-test.properties";
  public static String testPropertiesPathDynamoDB = "./src/test/resources/dynamodb-test.properties";
  public static String testPropertiesPathJDBC = "./src/test/resources/jdbc-test.properties";

  @Test
  public void testReadPropertiesInfluxDB() {
    Properties properties = PropertiesReader.readProperties(testPropertiesPathInfluxDB);

    assertTrue(properties.containsKey("token"));
    assertTrue(properties.containsKey("bucket"));
    assertTrue(properties.containsKey("org"));
    assertTrue(properties.containsKey("url"));

    assertEquals("testtoken", properties.get("token"));
    assertEquals("testbucket", properties.get("bucket"));
    assertEquals("testorg", properties.get("org"));
    assertEquals("testurl", properties.get("url"));
  }

  @Test
  public void testReadPropertiesDynamoDB() {
    Properties properties = PropertiesReader.readProperties(testPropertiesPathDynamoDB);

    assertTrue(properties.containsKey("aws_access_key_id"));
    assertTrue(properties.containsKey("aws_secret_access_key"));
    assertTrue(properties.containsKey("region"));
    assertTrue(properties.containsKey("table"));

    assertEquals("testaccesskeyid", properties.get("aws_access_key_id"));
    assertEquals("testsecretaccesskey", properties.get("aws_secret_access_key"));
    assertEquals("testregion", properties.get("region"));
    assertEquals("testtable", properties.get("table"));
  }

  @Test
  public void testReadPropertiesJdbc() {
    Properties properties = PropertiesReader.readProperties(testPropertiesPathJDBC);

    assertTrue(properties.containsKey("user"));
    assertTrue(properties.containsKey("password"));
    assertTrue(properties.containsKey("db_instance"));
    assertTrue(properties.containsKey("db_port"));
    assertTrue(properties.containsKey("db_name"));

    assertEquals("testuser", properties.get("user"));
    assertEquals("testpassword", properties.get("password"));
    assertEquals("testinstance", properties.get("db_instance"));
    assertEquals("1234", properties.get("db_port"));
    assertEquals("testdbname", properties.get("db_name"));
  }

  @Test
  public void testReadPropertiesFileNotFound() {
    String filepath = "./filepath_does_not_exist/test.properties";
    Logger logger = (Logger) PropertiesReader.logger;

    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    listAppender.start();
    logger.addAppender(listAppender);

    String expectedLogEntry = "Properties file not found at given location " + filepath + ".";

    Properties properties = PropertiesReader.readProperties(filepath);

    List<ILoggingEvent> logList = listAppender.list;
    assertEquals(1, logList.size());
    assertEquals(Level.ERROR, logList.get(0).getLevel());
    assertEquals(expectedLogEntry, logList.get(0).getFormattedMessage());
    assertTrue(properties.isEmpty());
  }


  @Test
  public void testReadPropertiesIOException() throws IOException {
    Logger logger = (Logger) PropertiesReader.logger;

    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    listAppender.start();
    logger.addAppender(listAppender);

    FileInputStream inputStreamMock = mock(FileInputStream.class);
    when(inputStreamMock.read(any())).thenThrow(IOException.class);

    String expectedLogEntry = "IO Exception while reading properties file with input stream.";
    Properties properties = PropertiesReader.readProperties(inputStreamMock);

    List<ILoggingEvent> logList = listAppender.list;
    assertEquals(1, logList.size());
    assertEquals(Level.ERROR, logList.get(0).getLevel());
    assertEquals(expectedLogEntry, logList.get(0).getFormattedMessage());
    assertTrue(properties.isEmpty());
  }

  @Test
  public void testPrivateConstructor()
      throws NoSuchMethodException, InstantiationException, IllegalAccessException {
    Constructor<PropertiesReader> constructor = PropertiesReader.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    constructor.setAccessible(true);

    try {
      constructor.newInstance();
    } catch (InvocationTargetException e) {
      // Exception must be checked manually as the reflection layer wraps the
      // IllegalStateException in an InvocationTargetException
      assertTrue(e.getCause() instanceof IllegalStateException);
      assertEquals("Utility class which should not be instantiated.", e.getCause().getMessage());
    }
  }
}
