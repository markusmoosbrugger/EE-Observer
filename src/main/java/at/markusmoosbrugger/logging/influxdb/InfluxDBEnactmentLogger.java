package at.markusmoosbrugger.logging.influxdb;


import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import com.google.inject.Inject;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.opt4j.core.start.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The {@link InfluxDBEnactmentLogger} is used to log information about the enactment to the
 * time-series database InfluxDB.
 *
 * @author Markus Moosbrugger
 */
public class InfluxDBEnactmentLogger implements EnactmentLogger {
  protected final Logger logger = LoggerFactory.getLogger(InfluxDBEnactmentLogger.class);
  protected String pathToPropertiesFile;
  protected InfluxDBClient client;
  protected String bucket;
  protected String org;
  protected String url;
  protected String token;


  /**
   * Default constructor. Reads the database configuration properties from the specified
   * properties file and creates an InfluxDB client.
   */
  @Inject
  public InfluxDBEnactmentLogger(
      @Constant(value = "pathToInfluxDBProperties", namespace = InfluxDBEnactmentLogger.class)
      final String pathToPropertiesFile) {
    this.pathToPropertiesFile = pathToPropertiesFile;
    readProperties();
    this.client = InfluxDBClientFactory.create(this.url, this.token.toCharArray());
  }

  /**
   * Additional constructor which can be used to provide a client in combination with the bucket
   * name and the organization.
   *
   * @param client an InfluxDB client
   * @param bucket the bucket name
   * @param org    the organization
   */
  public InfluxDBEnactmentLogger(final InfluxDBClient client, final String bucket,
      final String org) {
    this.client = client;
    this.bucket = bucket;
    this.org = org;
  }

  @Override
  public void logEnactment(final EnactmentLogEntry entry) {
    final Point point = Point.measurement("Enactment").addTag("functionId", entry.getFunctionId())
        .addTag("functionType", entry.getFunctionType()).addField("executionTime", entry.getExecutionTime())
        .addField("success", entry.isSuccess()).time(entry.getTimestamp(), WritePrecision.NS)
        .addField("inputComplexity", entry.getInputComplexity());

    try (WriteApi writeApi = client.getWriteApi()) {
      writeApi.writePoint(bucket, org, point);
    }
  }

  /**
   * Reads the needed properties from the specified properties file.
   */
  protected void readProperties() {
    try (InputStream input = new FileInputStream(pathToPropertiesFile)) {
      final Properties properties = new Properties();
      properties.load(input);

      this.bucket = (String) properties.get("bucket");
      this.org = (String) properties.get("org");
      this.url = (String) properties.get("url");
      this.token = (String) properties.get("token");

    } catch (FileNotFoundException e) {
      logger.error("Properties file not found at given location {}.", pathToPropertiesFile, e);
    } catch (IOException e) {
      logger.error("IO Exception while reading properties file at given location {}.",
          pathToPropertiesFile, e);
    }
  }
}
