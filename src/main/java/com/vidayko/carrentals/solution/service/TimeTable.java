package com.vidayko.carrentals.solution.service;

import com.vidayko.carrentals.solution.model.*;
import com.vidayko.carrentals.solution.model.entry.*;
import com.vidayko.carrentals.solution.model.time.*;
import java.util.*;

public interface TimeTable {

  Optional<Reservation> reserve(Class<? extends Reservable> type, Reservable reservable,
      ReservationPeriod period);

  Collection<Reservable> getReserved(Class<? extends Reservable> type,
      ReservationPeriod period);

  void cancelReservation(UUID id);
}
