package at.markusmoosbrugger.logback;

import org.junit.Test;

import static org.junit.Assert.*;

public class RunnerTest {
  @Test
  public void testRandomWait() {
    Runner runner = new Runner();
    long result = 0;
    try {
      result = runner.randomWaitFunction();
    } catch (InterruptedException e) {
      fail();
    }

    assertTrue(result >= 0 && result <= 1000);
  }

}
