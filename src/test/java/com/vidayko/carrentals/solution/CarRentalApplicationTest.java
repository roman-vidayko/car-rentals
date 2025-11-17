package com.vidayko.carrentals.solution;

import static com.vidayko.carrentals.solution.model.api.Status.*;
import static com.vidayko.carrentals.solution.model.api.ReservableEnum.*;

import com.vidayko.carrentals.solution.model.*;
import com.vidayko.carrentals.solution.model.api.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;

import static com.vidayko.carrentals.solution.model.time.ZonedDateTimeFormatter.*;

class CarRentalApplicationTest {

  @Test
  public void plain_test() {

    final CarRentalApplication carRentalApplication = new CarRentalApplication(Map.of(
        SEDAN, 1,
        SUV, 2,
        VAN, 1));

    final Response<Reservation> r01 = carRentalApplication.reserve("SEDAN",
        parse("2025-11-16T12:00:00[America/Toronto]"), 2);
    Assertions.assertEquals(SUCCESS, r01.getStatus());

    final Response<String> c01 = carRentalApplication.cancelReservation(
        String.valueOf(r01.getBody().getId()));
    Assertions.assertEquals(SUCCESS, c01.getStatus());
  }

  @Test
  public void concurrency_test() throws InterruptedException, ExecutionException {

    final CarRentalApplication carRentalApplication = new CarRentalApplication(Map.of(
        SEDAN, 4,
        SUV, 3,
        VAN, 2));

    //Trying to reserve all available cars, having 10 attempts per each car type
    final ZonedDateTime pickup = parse("2025-11-16T12:00:00[America/Toronto]");
    final int days = 3;
    final int attempts = 10;

    final ExecutorService executor = Executors.newCachedThreadPool();

    Map<ReservableEnum, List<Future<Response<Reservation>>>> results = initResults();

    for (int i = 0; i < attempts; i++) {
      results.get(SEDAN)
          .add(executor.submit(() -> carRentalApplication.reserve("SEDAN", pickup, days)));
      results.get(SUV)
          .add(executor.submit(() -> carRentalApplication.reserve("SUV", pickup, days)));
      results.get(VAN)
          .add(executor.submit(() -> carRentalApplication.reserve("VAN", pickup, days)));
    }

    final Map<ReservableEnum, Long> successes = new HashMap<>();

    for (ReservableEnum key : results.keySet()) {
      successes.put(key, results.get(key).stream().filter(f -> {
        try {
          return f.get().getStatus().equals(SUCCESS);
        } catch (Exception e) {
          return false;
        }
      }).count());
    }

    Assertions.assertEquals(4, successes.get(SEDAN));
    Assertions.assertEquals(3, successes.get(SUV));
    Assertions.assertEquals(2, successes.get(VAN));

    Response<Reservation> res_sedan = null;
    for (Future<Response<Reservation>> responseFuture : results.get(SEDAN)) {
      Response<Reservation> reservationResponse = responseFuture.get();
      if (reservationResponse.getStatus() == SUCCESS) {
        res_sedan = reservationResponse;
        break;
      }
    }
    Response<Reservation> res_suv = null;
    for (Future<Response<Reservation>> responseFuture : results.get(SUV)) {
      Response<Reservation> reservationResponse = responseFuture.get();
      if (reservationResponse.getStatus() == SUCCESS) {
        res_suv = reservationResponse;
        break;
      }
    }
    Response<Reservation> res_van = null;
    for (Future<Response<Reservation>> responseFuture : results.get(VAN)) {
      Response<Reservation> o = responseFuture.get();
      if (o.getStatus() == SUCCESS) {
        res_van = o;
        break;
      }
    }

    Assertions.assertNotNull(res_sedan);
    Assertions.assertNotNull(res_suv);
    Assertions.assertNotNull(res_van);

    carRentalApplication.cancelReservation(String.valueOf(res_sedan.getBody().getId()));
    carRentalApplication.cancelReservation(String.valueOf(res_suv.getBody().getId()));
    carRentalApplication.cancelReservation(String.valueOf(res_van.getBody().getId()));

    // Now cancelling one reservation per each car type, and trying to reserve them again
    results = initResults();
    for (int i = 0; i < attempts; i++) {
      results.get(SEDAN)
          .add(executor.submit(() -> carRentalApplication.reserve("SEDAN", pickup, days)));
      results.get(SUV)
          .add(executor.submit(() -> carRentalApplication.reserve("SUV", pickup, days)));
      results.get(VAN)
          .add(executor.submit(() -> carRentalApplication.reserve("VAN", pickup, days)));
    }
    successes.clear();

    for (ReservableEnum key : results.keySet()) {
      successes.put(key, results.get(key).stream().filter(f -> {
        try {
          return f.get().getStatus().equals(SUCCESS);
        } catch (Exception e) {
          return false;
        }
      }).count());
    }

    Assertions.assertEquals(1, successes.get(SEDAN));
    Assertions.assertEquals(1, successes.get(SUV));
    Assertions.assertEquals(1, successes.get(VAN));

    executor.shutdown();
  }

  private Map<ReservableEnum, List<Future<Response<Reservation>>>> initResults() {
    Map<ReservableEnum, List<Future<Response<Reservation>>>> results = new HashMap<>();
    for (ReservableEnum type : ReservableEnum.values()) {
      results.putIfAbsent(type, new ArrayList<>());
    }
    return results;
  }

}