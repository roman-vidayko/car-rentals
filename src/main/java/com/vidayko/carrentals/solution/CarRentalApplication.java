package com.vidayko.carrentals.solution;

import static com.vidayko.carrentals.solution.log.ConsoleLogger.Level.*;
import static com.vidayko.carrentals.solution.log.ConsoleLogger.log;

import com.vidayko.carrentals.solution.model.*;
import com.vidayko.carrentals.solution.model.api.*;
import com.vidayko.carrentals.solution.model.entry.*;
import com.vidayko.carrentals.solution.model.time.*;
import com.vidayko.carrentals.solution.service.*;
import com.vidayko.carrentals.solution.service.concurrent.*;
import java.lang.reflect.*;
import java.time.*;
import java.util.*;

/**
 * <br> Car Rental System
 * <br> Provides initialization API for limited set of cars.
 * <br> Exposes API for car reservation and cancellation.
 */
public class CarRentalApplication {

  private final Registry registry;

  /**
   * Constructor
   *
   * @param inventory map of car types with quantities
   */
  public CarRentalApplication(Map<ReservableEnum, Integer> inventory) {
    try {
      registry = new ConcurrentRegistry(inventory);
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
             NoSuchMethodException e) {
      log(INFO, e);
      throw new RuntimeException("The registry can't be initiated with the provided inventory", e);
    }
  }

  /**
   * Exposes reservation API for a car of a given type starting from desired date and time for a
   * given number of days.
   *
   * @param typeString   car type
   * @param startingFrom desired date and time
   * @param numberOfDays desired date and time
   * @return response with status (success/failure) and reservation entity when success
   */
  public Response<Reservation> reserve(String typeString, ZonedDateTime startingFrom,
      int numberOfDays) {
    final ReservationPeriod period = new ReservationPeriod(startingFrom,
        startingFrom.plusDays(numberOfDays));
    final Class<? extends Reservable> type = ReservableEnum.valueOf(typeString).getType();

    return registry.reserve(type, period)
        .map(value -> new Response<>(Status.SUCCESS, value))
        .orElseGet(() -> new Response<>(Status.FAILURE));
  }

  /**
   * Exposes reservation API by reservation id.
   *
   * @param reservationId reservation id
   * @return response with status (success/failure) and failure cause if applicable
   */
  public Response<String> cancelReservation(String reservationId) {

    try {
      final UUID id = UUID.fromString(reservationId);
      registry.cancelReservation(id);
      return new Response<>(Status.SUCCESS);
    } catch (Exception e) {
      log(INFO, e);
      return new Response<>(Status.FAILURE, e.getMessage());
    }
  }

}