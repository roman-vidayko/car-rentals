package com.vidayko.carrentals.solution.model.time;

import java.time.*;
import java.time.format.*;
import java.util.*;
import lombok.*;

public final class ReservationPeriod {

  private static final int MAX_HORIZON_SLOTS = (365 * 24 * 2); // 1 year in advance

  @Getter
  private final ZonedDateTime fromInclusive;

  @Getter
  private final ZonedDateTime toExclusive;

  public ReservationPeriod(ZonedDateTime fromInclusive, ZonedDateTime toExclusive) {

    Objects.requireNonNull(fromInclusive, "fromInclusive must not be null");
    Objects.requireNonNull(toExclusive, "toExclusive must not be null");

    if (!fromInclusive.getZone().equals(toExclusive.getZone())) {
      throw new IllegalArgumentException("from and to must be in the same time zone");
    }

    if (!fromInclusive.isBefore(toExclusive)) {
      throw new IllegalArgumentException("from must be before to");
    }

    this.fromInclusive = fromInclusive;
    this.toExclusive = toExclusive;
  }

  @Override
  public String toString() {

    return String.format("%s{fromInclusive=[%s], toExclusive=[%s]}",
        getClass().getSimpleName(),
        getFromInclusive().format(ZonedDateTimeFormatter.formatter()),
        getToExclusive().format(ZonedDateTimeFormatter.formatter()));
  }
}
