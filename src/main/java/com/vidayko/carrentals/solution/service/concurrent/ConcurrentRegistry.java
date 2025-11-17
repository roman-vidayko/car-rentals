package com.vidayko.carrentals.solution.service.concurrent;

import com.vidayko.carrentals.solution.model.*;
import com.vidayko.carrentals.solution.model.api.ReservableEnum;
import com.vidayko.carrentals.solution.model.entry.*;
import com.vidayko.carrentals.solution.model.time.*;
import com.vidayko.carrentals.solution.service.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

public final class ConcurrentRegistry implements Registry {

  private Map<Class<? extends Reservable>, Set<Reservable>> inventory = new ConcurrentHashMap<>();

  private TimeTable timeTable = new ConcurrentTimeTable();

  public ConcurrentRegistry(Map<ReservableEnum, Integer> inventory)
      throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {

    for (Map.Entry<ReservableEnum, Integer> type : inventory.entrySet()) {
      for (int i = 0; i < type.getValue(); i++) {
        addInventory(type.getKey().getType());
      }
    }

  }

  private <T extends Reservable> void addInventory(Class<T> type)
      throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    inventory.computeIfAbsent(type, v -> ConcurrentHashMap.newKeySet());
    inventory.get(type).add(type.getConstructor().newInstance());
  }

  @Override
  public Map<Class<? extends Reservable>, List<Reservable>> getAvailableInventory(
      ReservationPeriod period) {

    final Map<Class<? extends Reservable>, List<Reservable>> available = new HashMap<>();

    for (Map.Entry<Class<? extends Reservable>, Set<Reservable>> type : inventory.entrySet()) {
      final Collection<Reservable> reserved = timeTable.getReserved(type.getKey(), period);
      available.put(type.getKey(),
          type.getValue().stream().filter(e -> !reserved.contains(e)).toList());
    }

    return available;
  }

  @Override
  public Optional<Reservation> reserve(Reservable reservable, ReservationPeriod period) {
    return timeTable.reserve(reservable.getClass(), reservable, period);
  }

  @Override
  public Optional<Reservation> reserve(Class<? extends Reservable> type, ReservationPeriod period) {

    final List<Reservable> reservables = new ArrayList<>(inventory.get(type));
    Collections.shuffle(reservables);
    for (Reservable reservable : reservables) {
      final Optional<Reservation> reservation = reserve(reservable, period);
      if (!reservation.isEmpty()) {
        return reservation;
      }
    }

    return Optional.empty();
  }

  @Override
  public void cancelReservation(UUID id) {
    timeTable.cancelReservation(id);
  }

}