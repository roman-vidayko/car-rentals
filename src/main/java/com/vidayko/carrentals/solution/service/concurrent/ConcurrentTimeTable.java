package com.vidayko.carrentals.solution.service.concurrent;

import static com.vidayko.carrentals.solution.log.ConsoleLogger.Level.*;
import static com.vidayko.carrentals.solution.log.ConsoleLogger.*;

import com.vidayko.carrentals.solution.model.*;
import com.vidayko.carrentals.solution.model.entry.*;
import com.vidayko.carrentals.solution.model.time.*;
import com.vidayko.carrentals.solution.service.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;


public final class ConcurrentTimeTable implements TimeTable {

  public NavigableMap<ZonedDateTime, TimesSlot> timeTable = new ConcurrentSkipListMap<>();
  private Map<UUID, Reservation> reservations = new ConcurrentHashMap<>();

  @Override
  public Optional<Reservation> reserve(Reservable reservable,
      ReservationPeriod period) {

    try {

      if (!reservable.lock()) {
        throw new IllegalArgumentException(
            String.format(
                "Reservable entity %s is locked by another thread. Please try again later.",
                reservable));
      }

      if (getReserved(reservable.getClass(), period).contains(reservable)) {
        throw new IllegalArgumentException(
            String.format("Can't reserve %s for %s.", reservable, period));
      }

      final Reservation reservation = new Reservation(reservable);
      TimesSlot timesSlot = new ConcurrentTimesSlot(period.getFromInclusive());

      final ZonedDateTime firstTimeslotFrom = timesSlot.getFrom();
      while (timesSlot.getFrom().isBefore(period.getToExclusive())) {
        timeTable.putIfAbsent(timesSlot.getFrom(), timesSlot);
        timesSlot = timeTable.get(timesSlot.getFrom());
        timesSlot.addReservation(reservable.getClass(), reservation);
        timesSlot = timesSlot.nextTimeSlot();
      }

      final ZonedDateTime lastTimeslotFrom = timesSlot.getFrom();
      reservation.setPeriod(new ReservationPeriod(firstTimeslotFrom, lastTimeslotFrom));
      reservations.put(reservation.getId(), reservation);

      return Optional.of(reservation);

    } catch (Exception e) {
      log(INFO, e);
      return Optional.empty();
    } finally {
      reservable.release();
    }

  }

  @Override
  public Collection<Reservable> getReserved(Class<? extends Reservable> type,
      ReservationPeriod period) {
    return timeTable.subMap(period.getFromInclusive(), true, period.getToExclusive(), false)
        .values().stream()
        .flatMap(o -> o.getReservations(type).stream())
        .map(Reservation::getReservable)
        .collect(Collectors.toSet());
  }

  @Override
  public void cancelReservation(final UUID id) {

    Reservable reservable = null;
    try {

      final Reservation reservation = reservations.get(id);
      if (null == reservation) {
        throw new IllegalArgumentException(
            String.format("Unknown reservation with id=%s.", id));
      }

      reservable = reservation.getReservable();
      if (null == reservable) {
        throw new IllegalArgumentException(
            String.format("Unknown reservable entity for %s.", reservation));
      }

      if (!reservable.lock()) {
        throw new IllegalArgumentException(
            String.format(
                "Reservable entity %s is locked by another thread. Please try again later.",
                reservable));
      }

      timeTable.subMap(reservation.getPeriod().getFromInclusive(), true,
              reservation.getPeriod().getToExclusive(), false)
          .values().forEach(o -> o.removeReservation(reservation));

      reservations.remove(id);
    } catch (Exception e) {
      log(INFO, e);
      throw e;
    } finally {
      if (null != reservable) {
        reservable.release();
      }
    }
  }
}
