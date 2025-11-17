package com.vidayko.carrentals.solution.model;

import com.vidayko.carrentals.solution.model.entry.*;
import com.vidayko.carrentals.solution.model.time.*;
import java.util.*;
import lombok.*;

public class Reservation implements Hashable {

  @Getter
  private UUID id = UUID.randomUUID();

  @Getter
  private final Reservable reservable;

  @Setter
  @Getter
  private ReservationPeriod period;


  public Reservation(Reservable reservable) {
    this.reservable = reservable;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Reservation that)) {
      return false;
    }
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {

    return String.format("%s{id=[%s], reservable=[%s], period=[]}",
        getId(),
        getReservable(),
        getPeriod());
  }
}