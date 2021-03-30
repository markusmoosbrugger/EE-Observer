package at.markusmoosbrugger.influxdb;

import at.markusmoosbrugger.functioninvocation.FunctionInvocation;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;
import java.util.Objects;

@Measurement(name = "FunctionMeasurement") public class InfluxFunctionInvocation {

  @Column(name = "functionId", tag = true) protected String functionId;

  @Column(name = "functionType", tag = true) protected String functionType;

  @Column(name = "executionTime") protected Double executionTime;

  @Column(name = "success") protected boolean success;

  @Column(name = "time") protected Instant time;

  public InfluxFunctionInvocation() {

  }

  public InfluxFunctionInvocation(FunctionInvocation functionInvocation) {
    this.functionId = functionInvocation.getFunctionId();
    this.functionType = functionInvocation.getFunctionType();
    this.success = functionInvocation.isSuccess();
    this.executionTime = functionInvocation.getExecutionTime();
    this.time = functionInvocation.getTime();
  }

  @Override public String toString() {
    return "InfluxFunction{" + "functionId='" + functionId + '\'' + ", functionType='"
        + functionType + '\'' + ", executionTime=" + executionTime + ", success=" + success
        + ", time=" + time + '}';
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    InfluxFunctionInvocation that = (InfluxFunctionInvocation) o;
    return functionId.equals(that.functionId) && Objects.equals(time, that.time);
  }

  @Override public int hashCode() {
    return Objects.hash(functionId, time);
  }
}
