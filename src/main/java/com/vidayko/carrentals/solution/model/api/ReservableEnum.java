package com.vidayko.carrentals.solution.model.api;

import com.vidayko.carrentals.solution.model.entry.Reservable;
import com.vidayko.carrentals.solution.model.entry.Sedan;
import com.vidayko.carrentals.solution.model.entry.Suv;
import com.vidayko.carrentals.solution.model.entry.Van;

public enum ReservableEnum {

  SEDAN(Sedan.class),
  SUV(Suv.class),
  VAN(Van.class);

  private final Class<? extends Reservable> type;

  ReservableEnum(Class<? extends Reservable> type) {
    this.type = type;
  }

  public Class<? extends Reservable> getType() {
    return type;
  }

}