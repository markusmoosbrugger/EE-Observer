package at.uibk.dps.ee.observer.logging.jdbc;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import org.junit.Test;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JdbcEnactmentLoggerTest {

  @Test
  public void testLogEnactment() throws SQLException {
    String typeId = "type";
    String enactmentMode = "mode";
    String implementationId = "id";
    double executionTime = 1.12;
    boolean success = true;
    double inputComplexity = 0.9;
    Instant timestamp = Instant.now();

    EnactmentLogEntry entry =
        new EnactmentLogEntry(timestamp, typeId, enactmentMode, implementationId, new HashSet<>(),
            executionTime, success, inputComplexity);

    Connection connectionMock = mock(Connection.class);

    JdbcEnactmentLogger jdbcEnactmentLogger = new JdbcEnactmentLogger(connectionMock);

    PreparedStatement statementMock = mock(PreparedStatement.class);
    when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);

    jdbcEnactmentLogger.logEnactment(entry);

    verify(statementMock).setLong(1, timestamp.toEpochMilli());
    verify(statementMock).setString(2, typeId);
    verify(statementMock).setString(3, enactmentMode);
    verify(statementMock).setString(4, implementationId);
    verify(statementMock).setDouble(5, executionTime);
    verify(statementMock).setDouble(6, inputComplexity);
    verify(statementMock).setBoolean(7, success);

    verify(statementMock).executeUpdate();
  }

  @Test
  public void testLogEnactmentSQLException()
      throws SQLException, NoSuchFieldException, IllegalAccessException {
    EnactmentLogEntry entry =
        new EnactmentLogEntry(Instant.now(), "id", "mode", "implId", new HashSet<>(), 10, true, 0.8);

    Connection connectionMock = mock(Connection.class);
    Logger loggerMock = mock(Logger.class);

    JdbcEnactmentLogger jdbcEnactmentLogger = new JdbcEnactmentLogger(connectionMock);

    PreparedStatement statementMock = mock(PreparedStatement.class);
    when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
    when(statementMock.executeUpdate()).thenThrow(SQLException.class);

    // mock static logger field
    Field loggerField = JdbcEnactmentLogger.class.getDeclaredField("logger");
    loggerField.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(loggerField, loggerField.getModifiers() & ~Modifier.FINAL);
    loggerField.set(jdbcEnactmentLogger, loggerMock);

    jdbcEnactmentLogger.logEnactment(entry);

    verify(loggerMock)
        .error(eq("Logging of enactment entry {} failed."), eq(entry), any(SQLException.class));
  }
}
