package com.vidayko.carrentals.solution.service;

import com.vidayko.carrentals.solution.model.*;
import com.vidayko.carrentals.solution.model.entry.*;
import java.time.*;
import java.util.*;

/**
 * Discrete atomic time unit that contains and encapsulates all reservations associated with it.
 */
public interface TimesSlot extends Comparable<TimesSlot>{

  /**
   * Adds a reservation to the timeslot.
   *
   * @param type item class
   * @param reservation reservation object
   */
  void addReservation(Class<? extends Reservable> type, Reservation reservation);

  /**
   * Retrieves all present reservations from the timeslot.
   *
   * @param type item class
   * @return set of reservations
   */
  Set<Reservation> getReservations(Class<? extends Reservable> type);

  /**
   * Removes a reservation from the timeslot.
   *
   * @param reservation reservation object
   * @return boolean success flag
   */
  boolean removeReservation(Reservation reservation);

  /**
   * @Getter
   */
  ZonedDateTime getFrom();

  /**
   * @Getter
   */
  Duration getDuration();

  /**
   * Creates the next adjacent timeslot.
   *
   * @return next timeslot
   */
  TimesSlot nextTimeSlot();

}
