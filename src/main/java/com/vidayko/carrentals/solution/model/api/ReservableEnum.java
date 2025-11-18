package com.vidayko.carrentals.solution.model.api;

import com.vidayko.carrentals.solution.model.entry.*;

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