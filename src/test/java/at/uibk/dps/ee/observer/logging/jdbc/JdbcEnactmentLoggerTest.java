package at.uibk.dps.ee.observer.logging.jdbc;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
  public void testLogEnactmentSQLException() throws SQLException {
    EnactmentLogEntry entry =
        new EnactmentLogEntry(Instant.now(), "id", "mode", "implId", new HashSet<>(), 10, true,
            0.8);

    Connection connectionMock = mock(Connection.class);

    JdbcEnactmentLogger jdbcEnactmentLogger = new JdbcEnactmentLogger(connectionMock);
    Logger logger = (Logger) jdbcEnactmentLogger.logger;

    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    listAppender.start();
    logger.addAppender(listAppender);

    PreparedStatement statementMock = mock(PreparedStatement.class);
    when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
    when(statementMock.executeUpdate()).thenThrow(SQLException.class);

    jdbcEnactmentLogger.logEnactment(entry);

    String expectedLogEntry = "Logging of enactment entry " + entry.toString() + " failed.";
    List<ILoggingEvent> logList = listAppender.list;
    assertEquals(1, logList.size());
    assertEquals(Level.ERROR, logList.get(0).getLevel());
    assertEquals(expectedLogEntry, logList.get(0).getFormattedMessage());
  }
}
