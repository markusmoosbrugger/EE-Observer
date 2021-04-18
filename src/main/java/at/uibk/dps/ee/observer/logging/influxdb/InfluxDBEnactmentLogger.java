package at.uibk.dps.ee.observer.logging.influxdb;


import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import at.uibk.dps.ee.observer.logging.configuration.PropertiesReader;
import com.google.inject.Inject;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.opt4j.core.start.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * The {@link InfluxDBEnactmentLogger} is used to log information about the enactment to the
 * time-series database InfluxDB.
 *
 * @author Markus Moosbrugger
 */
public class InfluxDBEnactmentLogger implements EnactmentLogger {
  protected final Logger logger = LoggerFactory.getLogger(InfluxDBEnactmentLogger.class);
  protected InfluxDBClient client;
  protected InfluxDBConfiguration configuration;


  /**
   * Default constructor. Reads the database configuration properties from the specified properties
   * file and creates an InfluxDB client.
   */
  @Inject
  public InfluxDBEnactmentLogger(
      @Constant(value = "pathToInfluxDBProperties", namespace = InfluxDBEnactmentLogger.class)
      final String pathToPropertiesFile) {
    Properties properties = PropertiesReader.readProperties(pathToPropertiesFile);
    this.configuration = new InfluxDBConfiguration(properties);
    this.client = InfluxDBClientFactory
        .create(configuration.getUrl(), configuration.getToken().toCharArray());
  }

  /**
   * Additional constructor which can be used to provide a client in combination with the bucket
   * name and the organization.
   *
   * @param client        the InfluxDB client
   * @param configuration the InfluxDB configuration
   */
  public InfluxDBEnactmentLogger(final InfluxDBClient client, InfluxDBConfiguration configuration) {
    this.client = client;
    this.configuration = configuration;
  }

  @Override
  public void logEnactment(final EnactmentLogEntry entry) {
    final Point point = Point.measurement("Enactment")
        .addTag("typeId", entry.getTypeId())
        .addTag("enactmentMode", entry.getEnactmentMode())
        .addTag("implementationId", entry.getImplementationId())
        .addField("executionTime", entry.getExecutionTime())
        .addField("success", entry.isSuccess())
        .addField("inputComplexity", entry.getInputComplexity())
        .time(entry.getTimestamp(), WritePrecision.NS);

    try (WriteApi writeApi = client.getWriteApi()) {
      writeApi.writePoint(configuration.getBucket(), configuration.getOrganization(), point);
    }
  }

}
