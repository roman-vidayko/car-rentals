package com.vidayko.carrentals.solution.model.entry;

import java.util.*;
import java.util.concurrent.locks.*;

public abstract class Reservable implements Lockable, Hashable {

  private UUID id = UUID.randomUUID();

  private final Lock lock = new ReentrantLock();

  @Override
  public boolean lock() {
    return lock.tryLock();
  }

  @Override
  public void release() {
    lock.unlock();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Reservable that)) {
      return false;
    }
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return String.format("%s{id=%s}", getClass().getSimpleName(), id);
  }
}
