package at.markusmoosbrugger.influxdb;

import at.markusmoosbrugger.functioninvocation.FunctionInvocation;
import at.markusmoosbrugger.functioninvocation.FunctionInvocationWriter;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InfluxDBWriter implements FunctionInvocationWriter {

  protected Logger logger;
  protected static String pathToPropertiesFile = "./database/influxdb.properties";
  protected InfluxDBClient client;
  protected String bucket;
  protected String org;
  protected String url;
  private String token;

  public InfluxDBWriter() {
    logger = LoggerFactory.getLogger(InfluxDBWriter.class);
    readProperties();
    this.client = InfluxDBClientFactory.create(this.url, this.token.toCharArray());
  }

  // currently only used for unit test
  public InfluxDBWriter(InfluxDBClient client) {
    logger = LoggerFactory.getLogger(InfluxDBWriter.class);
    readProperties();
    this.client = client;
  }

  private void readProperties() {
    try (InputStream input = new FileInputStream(pathToPropertiesFile)) {
      Properties properties = new Properties();
      properties.load(input);
      this.bucket = (String) properties.get("bucket");
      this.org = (String) properties.get("org");
      this.url = (String) properties.get("url");
      this.token = (String) properties.get("token");

    } catch (FileNotFoundException e) {
      logger.error("Properties file not found. ", e);
    } catch (IOException e) {
      logger.error("IO Exception while reading properties file. ", e);
    }
  }


  public void logFunctionInvocation(InfluxFunctionInvocation function) {

    // this logger does only output to the console at the moment
    logger.info("TYPE {} ID {} EXEC TIME {} milliseconds SUCCESS {}.", function.functionId,
        function.functionType, function.executionTime, function.success);
  }

  public void saveFunctionInvocation(FunctionInvocation invocation) {
    InfluxFunctionInvocation function = new InfluxFunctionInvocation(invocation);

    WriteApi writeApi = client.getWriteApi();
    writeApi.writeMeasurement(bucket, org, WritePrecision.MS, function);
    writeApi.flush();
    logFunctionInvocation(function);
  }
}
