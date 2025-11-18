package com.vidayko.carrentals.solution.model.time;

import java.time.*;
import java.time.format.*;
import java.util.*;
import lombok.*;

@NoArgsConstructor
public final class ReservationPeriod {

  @Getter
  private ZonedDateTime fromInclusive;

  @Getter
  private ZonedDateTime toExclusive;

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
