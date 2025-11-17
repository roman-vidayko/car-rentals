package com.vidayko.carrentals.solution.service;

import com.vidayko.carrentals.solution.model.*;
import com.vidayko.carrentals.solution.model.entry.*;
import java.time.*;
import java.util.*;

public interface TimesSlot extends Comparable<TimesSlot>{

  void addReservation(Class<? extends Reservable> type, Reservation reservation);

  Set<Reservation> getReservations(Class<? extends Reservable> type);

  boolean removeReservation(Reservation reservation);

  ZonedDateTime getFrom();

  Duration getDuration();

  TimesSlot nextTimeSlot();

}
