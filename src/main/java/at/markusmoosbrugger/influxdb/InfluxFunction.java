package at.markusmoosbrugger.influxdb;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "FunctionMeasurement") public class InfluxFunction {

  @Column(name = "functionId", tag = true) protected String functionId;

  @Column(name = "functionType", tag = true) protected String functionType;

  @Column(name = "executionTime") protected Double executionTime;

  @Column(name = "success") protected boolean success;

  @Column(name = "time") protected Instant time;

  public void setRandomExecutionTime(int maxExecutionTime) {
    this.executionTime = Math.random() * maxExecutionTime;
  }

  public void setRandomSuccess(double successPercentage) {
    this.success = Math.random() < successPercentage ? true : false;
  }

  @Override public String toString() {
    return "InfluxFunction{" + "functionId='" + functionId + '\'' + ", functionType='"
        + functionType + '\'' + ", executionTime=" + executionTime + ", success=" + success
        + ", time=" + time + '}';
  }
}
