package com.vidayko.carrentals.solution.service.concurrent;

import com.vidayko.TestUtils;
import com.vidayko.carrentals.solution.model.*;
import com.vidayko.carrentals.solution.model.entry.*;
import java.io.*;
import java.util.*;
import lombok.*;
import org.junit.jupiter.api.*;

/**
 * Sample JUnit coverage test (all methods are covered).
 * Any additional test cases should be added to the *TestCases.json files,
 * rather than hard-coding new @Test methods.
 */
class ConcurrentTimesSlotTest {

  @Test
  void equals_test() throws IOException {
    final EqualsTestCase[] testcases = TestUtils.readValue(
        TestUtils.stringOf(this.getClass(),
            "testcases/com/vidayko/carrentals/solution/service/concurrent/ConcurrentTimesSlotTest/EqualsTestCases.json"),
        EqualsTestCase[].class);

    for (EqualsTestCase testCase : testcases) {
      Assertions.assertEquals(testCase.isEqual, testCase.o1.equals(testCase.o2));
    }
  }

  @Data
  static class EqualsTestCase {

    public ConcurrentTimesSlot o1;
    public ConcurrentTimesSlot o2;
    public boolean isEqual;
  }

  @Test
  void compareTo_test() throws IOException {
    final CompareToTestCase[] testcases = TestUtils.readValue(
        TestUtils.stringOf(this.getClass(),
            "testcases/com/vidayko/carrentals/solution/service/concurrent/ConcurrentTimesSlotTest/CompareToTestCases.json"),
        CompareToTestCase[].class);

    for (CompareToTestCase testCase : testcases) {
      Assertions.assertEquals(testCase.compare, testCase.o1.compareTo(testCase.o2));
    }
  }

  @Data
  static class CompareToTestCase {

    public ConcurrentTimesSlot o1;
    public ConcurrentTimesSlot o2;
    public int compare;
  }

  @Test
  void nextTimeSlot_test() throws IOException {
    final NextTimeSlotTestCase[] testcases = TestUtils.readValue(
        TestUtils.stringOf(this.getClass(),
            "testcases/com/vidayko/carrentals/solution/service/concurrent/ConcurrentTimesSlotTest/NextTimeSlotTestCases.json"),
        NextTimeSlotTestCase[].class);

    for (NextTimeSlotTestCase testCase : testcases) {
      Assertions.assertEquals(testCase.expected, testCase.original.nextTimeSlot());
    }
  }

  @Data
  static class NextTimeSlotTestCase {

    public ConcurrentTimesSlot original;
    public ConcurrentTimesSlot expected;
  }

  @Test
  void addReservation_test() throws IOException, InstantiationException, IllegalAccessException {
    final AddReservationTestCase[] testcases = TestUtils.readValue(
        TestUtils.stringOf(this.getClass(),
            "testcases/com/vidayko/carrentals/solution/service/concurrent/ConcurrentTimesSlotTest/AddReservationTestCases.json"),
        AddReservationTestCase[].class);

    for (AddReservationTestCase testCase : testcases) {

      for (Reservation reservation : testCase.reservations) {
        reservation.setReservable(testCase.reservableClass.newInstance());
        testCase.original.addReservation(testCase.reservableClass, reservation);
      }

      Assertions.assertEquals(testCase.reservations.size(),
          testCase.original.getReservations(testCase.reservableClass).size());
    }
  }

  @Data
  static class AddReservationTestCase {

    public ConcurrentTimesSlot original;
    List<Reservation> reservations;
    Class<Reservable> reservableClass;
  }


  @Test
  void getReservations_test() throws IOException, InstantiationException, IllegalAccessException {
    final GetReservationTestCase[] testcases = TestUtils.readValue(
        TestUtils.stringOf(this.getClass(),
            "testcases/com/vidayko/carrentals/solution/service/concurrent/ConcurrentTimesSlotTest/GetReservationTestCases.json"),
        GetReservationTestCase[].class);

    for (GetReservationTestCase testCase : testcases) {

      for (Reservation reservation : testCase.reservations) {
        reservation.setReservable(testCase.reservableClass.newInstance());
        testCase.original.addReservation(testCase.reservableClass, reservation);
      }

      Assertions.assertEquals(testCase.reservations.size(),
          testCase.original.getReservations(testCase.reservableClass).size());
    }
  }

  @Data
  static class GetReservationTestCase {

    public ConcurrentTimesSlot original;
    List<Reservation> reservations;
    Class<Reservable> reservableClass;
  }

  @Test
  void removeReservation_test() throws IOException, InstantiationException, IllegalAccessException {
    final RemoveReservationTestCase[] testcases = TestUtils.readValue(
        TestUtils.stringOf(this.getClass(),
            "testcases/com/vidayko/carrentals/solution/service/concurrent/ConcurrentTimesSlotTest/RemoveReservationTestCases.json"),
        RemoveReservationTestCase[].class);

    for (RemoveReservationTestCase testCase : testcases) {

      for (Reservation reservation : testCase.reservations) {
        reservation.setReservable(testCase.reservableClass.newInstance());
        testCase.original.addReservation(testCase.reservableClass, reservation);
      }

      Assertions.assertEquals(testCase.reservations.size(),
          testCase.original.getReservations(testCase.reservableClass).size());

      for (Reservation reservation : testCase.reservations) {
        testCase.original.removeReservation(reservation);
      }

      Assertions.assertTrue(testCase.original.getReservations(testCase.reservableClass).isEmpty());
    }
  }

  @Data
  static class RemoveReservationTestCase {

    public ConcurrentTimesSlot original;
    List<Reservation> reservations;
    Class<Reservable> reservableClass;
  }
}