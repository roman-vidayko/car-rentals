package com.vidayko.carrentals.solution.service;

import com.vidayko.carrentals.solution.model.*;
import com.vidayko.carrentals.solution.model.entry.*;
import com.vidayko.carrentals.solution.model.time.*;
import java.util.*;

/**
 * Aggregates and exposes reservation management API and maintains the inventory.
 */
public interface Registry {

  /**
   * Retrieves entries available for reservation.
   *
   * @param period time period
   * @return Map<Type, List<Reservable>>
   */
  Map<Class<? extends Reservable>, List<Reservable>> getAvailableInventory(
      ReservationPeriod period);

  /**
   * Reserves unit for a reservation period.
   *
   * @param reservable  reservable unit
   * @param period reservation period
   * @return Reservation (Optional)
   */
  Optional<Reservation> reserve(Reservable reservable, ReservationPeriod period);

  /**
   * Reserves unit by its type and reservation period.
   *
   * @param type  reservable unit
   * @param period reservation period
   * @return Reservation (Optional)
   */
  Optional<Reservation> reserve(Class<? extends Reservable> type, ReservationPeriod period);

  /**
   * Cancels reservation by UUID.
   *
   * @param id reservation UUID
   */
  void cancelReservation(UUID id);
}
