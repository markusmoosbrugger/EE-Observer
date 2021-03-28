package at.markusmoosbrugger.functioninvocation;

import com.google.gson.JsonObject;

import java.time.Instant;

public class FunctionInvocation {
  protected String functionId;
  protected String functionType;

  protected Instant time;
  protected boolean success;
  protected double executionTime;

  protected JsonObject input;
  protected JsonObject output;

  public FunctionInvocation() {

  }

  public FunctionInvocation(String functionId, String functionType, Instant time, boolean success,
      double executionTime) {
    this.functionId = functionId;
    this.functionType = functionType;
    this.time = time;
    this.success = success;
    this.executionTime = executionTime;
  }

  public String getFunctionId() {
    return functionId;
  }

  public void setFunctionId(String functionId) {
    this.functionId = functionId;
  }

  public String getFunctionType() {
    return functionType;
  }

  public void setFunctionType(String functionType) {
    this.functionType = functionType;
  }

  public Instant getTime() {
    return time;
  }

  public void setTime(Instant time) {
    this.time = time;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public double getExecutionTime() {
    return executionTime;
  }

  public void setExecutionTime(double executionTime) {
    this.executionTime = executionTime;
  }

  public JsonObject getInput() {
    return input;
  }

  public void setInput(JsonObject input) {
    this.input = input;
  }

  public JsonObject getOutput() {
    return output;
  }

  public void setOutput(JsonObject output) {
    this.output = output;
  }

  public void setRandomExecutionTime(double maxExecutionTime, double minExecutionTime) {
    this.executionTime = Math.random() * (maxExecutionTime-minExecutionTime) + minExecutionTime;
  }

  public void setRandomSuccess(double successPercentage) {
    this.success = Math.random() < successPercentage;
  }
}
