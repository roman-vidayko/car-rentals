package com.vidayko.carrentals.solution.service;

import com.vidayko.carrentals.solution.model.*;
import com.vidayko.carrentals.solution.model.entry.*;
import com.vidayko.carrentals.solution.model.time.*;
import java.util.*;

public interface Registry {

  Map<Class<? extends Reservable>, List<Reservable>> getAvailableInventory(
      ReservationPeriod period);

  Optional<Reservation> reserve(Reservable reservable, ReservationPeriod period);

  Optional<Reservation> reserve(Class<? extends Reservable> type, ReservationPeriod period);

  void cancelReservation(UUID id);
}
