package com.vidayko.carrentals.solution.model.time;

import java.time.ZonedDateTime;
import java.time.format.*;

public class ZonedDateTimeFormatter {

  /**
   * "2025-11-16T12:30:00[America/Toronto]"
   */
  public static DateTimeFormatter formatter() {
    return DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss'['VV']'");
  }


  public static ZonedDateTime parse(String literal) {
    return ZonedDateTime.parse(literal, formatter());
  }

}
