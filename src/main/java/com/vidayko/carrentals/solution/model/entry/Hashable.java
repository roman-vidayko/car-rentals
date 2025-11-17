package com.vidayko.carrentals.solution.model.entry;

/**
 * Marker interface
 */
public interface Hashable {

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();
}
