package com.vidayko.carrentals.solution.model.entry;

public interface Lockable {

  boolean lock();

  void release();
}
