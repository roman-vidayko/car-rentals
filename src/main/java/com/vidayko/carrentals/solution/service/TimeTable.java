package com.vidayko.carrentals.solution.service;

import com.vidayko.carrentals.solution.model.*;
import com.vidayko.carrentals.solution.model.entry.*;
import com.vidayko.carrentals.solution.model.time.*;
import java.util.*;

/**
 * Calendar-like structure that works with TimeSlot objects and tracks all reservations.
 */
public interface TimeTable {

  /**
   * Reserves a reservable item for specified reservation period.
   *
   * @param reservable reservable item
   * @param period reservation period
   * @return Reservation (optional)
   */
  Optional<Reservation> reserve(Reservable reservable,
      ReservationPeriod period);

  /**
   * Retrieves all reservable items (by type) for specified reservation period.
   *
   * @param type reservable item class
   * @param period reservation period
   * @return collection of reservable items
   */
  Collection<Reservable> getReserved(Class<? extends Reservable> type,
      ReservationPeriod period);

  /**
   * Cancels reservation by reservation id.
   *
   * @param id reservation UUID
   */
  void cancelReservation(UUID id);
}
