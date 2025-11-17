package com.vidayko.carrentals.solution.service.concurrent;

import com.vidayko.carrentals.solution.model.*;
import com.vidayko.carrentals.solution.model.entry.*;
import com.vidayko.carrentals.solution.service.*;
import java.time.*;
import java.time.temporal.*;
import java.util.*;
import java.util.concurrent.*;
import lombok.*;

public final class ConcurrentTimesSlot implements TimesSlot, Hashable  {

  private final ConcurrentMap<Class<? extends Reservable>, Set<Reservation>> reserved =
      new ConcurrentHashMap<>();

  @Getter
  private final ZonedDateTime from;

  private static final Duration duration = Duration.ofMinutes(30);

  @Override
  public Duration getDuration(){
    return duration;
  }

  public ConcurrentTimesSlot(ZonedDateTime from) {
    final Duration day = Duration.ofHours(24);
    if (day.toMillis() % duration.toMillis() != 0) {
      throw new IllegalArgumentException("period must divide 24 hours exactly");
    }
    this.from = Objects.requireNonNull(toPreviousTimeSlot(from));
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ConcurrentTimesSlot that)) {
      return false;
    }
    return Objects.equals(from, that.from);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(from);
  }

  @Override
  public int compareTo(TimesSlot o) {
    return Comparator
        .nullsFirst(Comparator.<ZonedDateTime>naturalOrder())
        .compare(getFrom(), o.getFrom());
  }

  private static ZonedDateTime toPreviousTimeSlot(ZonedDateTime from) {

    ZonedDateTime dayStart = from.truncatedTo(ChronoUnit.DAYS);
    long sinceMillis = Duration.between(dayStart, from).toMillis();

    if (sinceMillis < 0) {
      dayStart = dayStart.minusDays(1);
      sinceMillis = Duration.between(dayStart, from).toMillis();
    }

    long periodMillis = duration.toMillis();
    long flooredMillis = sinceMillis - (sinceMillis % periodMillis);

    return dayStart.plus(Duration.ofMillis(flooredMillis));
  }

  public TimesSlot nextTimeSlot() {
    return new ConcurrentTimesSlot(this.getFrom().plus(duration));
  }

  public void addReservation(Class<? extends Reservable> type, Reservation reservation) {
    reserved.computeIfAbsent(type, t -> ConcurrentHashMap.newKeySet())
        .add(reservation);
  }

  @Override
  public Set<Reservation> getReservations(Class<? extends Reservable> type) {
    return reserved.getOrDefault(type, Collections.emptySet());
  }

  @Override
  public boolean removeReservation(Reservation reservation) {
    return reserved.get(reservation.getReservable().getClass()).remove(reservation);
  }
}
