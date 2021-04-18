package at.uibk.dps.ee.observer.logging.configuration;

import org.junit.Test;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

  static void setFinalStatic(Field field, Object newValue)
      throws NoSuchFieldException, IllegalAccessException {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }

  @Test
  public void testReadPropertiesFileNotFound() throws NoSuchFieldException, IllegalAccessException {

    String filepath = "./filepath_does_not_exist/test.properties";
    Logger loggerMock = mock(Logger.class);

    // get rid of the final modifier from the logger field to mock it afterwards
    setFinalStatic(PropertiesReader.class.getDeclaredField("logger"), loggerMock);

    Properties properties = PropertiesReader.readProperties(filepath);

    verify(loggerMock).error(eq("Properties file not found at given location {}."), eq(filepath),
        any(FileNotFoundException.class));
    assertTrue(properties.isEmpty());
  }

  @Test
  public void testReadPropertiesIOException()
      throws IOException, NoSuchFieldException, IllegalAccessException {
    FileInputStream inputStreamMock = mock(FileInputStream.class);
    when(inputStreamMock.read(any())).thenThrow(IOException.class);

    Logger loggerMock = mock(Logger.class);

    // get rid of the final modifier from the logger field to mock it afterwards
    setFinalStatic(PropertiesReader.class.getDeclaredField("logger"), loggerMock);

    Properties properties = PropertiesReader.readProperties(inputStreamMock);

    verify(loggerMock).error(eq("IO Exception while reading properties file with input stream."),
        any(IOException.class));
    assertTrue(properties.isEmpty());
  }
}
