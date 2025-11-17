package com.vidayko.carrentals.solution.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.util.List;

public class ConsoleLogger {

  public enum Level {
    INFO, DEBUG, ERROR
  }

  private static final boolean isDebugMode;

  public static void log(Level level, Throwable throwable) {

    if (isDebugMode) {
      final StringWriter sw = new StringWriter();
      final PrintWriter pw = new PrintWriter(sw);
      throwable.printStackTrace(pw);
      pw.flush();

      System.out.println(
          String.format("\n [%s] Thread[%d] %s: %s %n",
              level,
              Thread.currentThread().threadId(),
              throwable.getClass().getName(),
              sw));
    }
  }

  static {
    final List<String> args = ManagementFactory
        .getRuntimeMXBean()
        .getInputArguments();
    isDebugMode = args.stream().anyMatch(a -> a.contains("jdwp"));
  }

}
