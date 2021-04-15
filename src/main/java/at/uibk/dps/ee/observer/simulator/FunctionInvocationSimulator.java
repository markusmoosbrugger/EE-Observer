package at.uibk.dps.ee.observer.simulator;

import at.uibk.dps.ee.enactables.logging.EnactmentLogEntry;
import at.uibk.dps.ee.enactables.logging.EnactmentLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link FunctionInvocationSimulator} is used to simulate multiple runs of function
 * invocations.
 *
 * @author Markus Moosbrugger
 */
public class FunctionInvocationSimulator {

  static final double MAX_EXECUTION_TIME = 10;
  protected Logger logger = LoggerFactory.getLogger(FunctionInvocationSimulator.class);
  protected List<EnactmentLogger> loggers;

  /**
   * Default constructor.
   */
  public FunctionInvocationSimulator() {
    this.loggers = new ArrayList<>();
  }

  /**
   * Adds a logger which to the list of enactment loggers.
   *
   * @param logger the enactment logger
   */
  public void addLogger(EnactmentLogger logger) {
    this.loggers.add(logger);
  }

  /**
   * Removes a logger from the list of enactment loggers.
   *
   * @param logger the enactment logger
   */
  public void removeLogger(EnactmentLogger logger) {
    this.loggers.remove(logger);
  }

  /**
   * Simulates a given number of runs with a given number of functions.
   *
   * @param numberOfFunctions the amount of functions which should be simulated
   * @param numberOfRuns      the amount of runs which should be simulated
   */
  public void simulateMultipleFunctions(int numberOfFunctions, int numberOfRuns) {
    for (int i = 0; i < numberOfRuns; i++) {
      logger.info("\n--------- Simulating run {} ---------", i + 1);
      simulateRun(numberOfFunctions);
    }
  }

  /**
   * Simulates a single run for a given number of functions. Functions are simulated in a way that
   * functions with lower functionId tend to have a lower execution time, however, also a smaller
   * success ratio.
   *
   * @param numberOfFunctions the amount of functions which should be simulated
   */
  public void simulateRun(int numberOfFunctions) {
    for (int i = 0; i < numberOfFunctions; i++) {
      randomWait(1000);
      String functionId = "dummy_function_" + i;
      String functionType = "functionType_" + i % 3;
      // success ratio depends on the function number
      boolean success = getRandomSuccess((i + 1) * 0.1);
      // execution time depends on the function number
      double executionTime = getRandomDouble(MAX_EXECUTION_TIME * i, MAX_EXECUTION_TIME * (i + 5));
      double inputComplexity = getRandomDouble(0, 1);
      Instant timestamp = Instant.now();

      EnactmentLogEntry entry =
          new EnactmentLogEntry(timestamp, functionId, functionType, executionTime, success,
              inputComplexity);
      saveFunctionInvocation(entry);
    }
  }

  private void saveFunctionInvocation(EnactmentLogEntry entry) {
    loggers.stream().forEach(l -> l.logEnactment(entry));
  }


  /**
   * Generates a random double between the given minimum and maximum.
   *
   * @param min the minimum value
   * @param max the maximum value
   * @return value between min and max
   */
  protected static double getRandomDouble(double min, double max) {
    return Math.random() * (max - min) + min;
  }

  /**
   * Generates a random success value depending on the given success percentage.
   *
   * @param successPercentage the success percentage
   * @return the success value
   */
  protected static boolean getRandomSuccess(double successPercentage) {
    return Math.random() < successPercentage;
  }

  /**
   * Waits a random amount of milliseconds between 0 and the given value.
   *
   * @param maxMillis the maximum value in milliseconds.
   */
  protected static void randomWait(int maxMillis) {
    try {
      Thread.sleep((long) (Math.random() * maxMillis));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Unexpected interrupt", e);
    }
  }

}
