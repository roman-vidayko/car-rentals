package com.vidayko.carrentals.solution.model.api;

import com.vidayko.carrentals.solution.model.entry.Hashable;
import java.util.*;
import lombok.Getter;

@Getter
public final class Response<T>  implements Hashable{

  private final Status status;
  private final T body;

  public Response(Status status, T body) {
    this.status = status;
    this.body = body;
  }

  public Response(Status status) {
    this.status = status;
    this.body = null;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Response<?> response)) {
      return false;
    }
    return status == response.status && Objects.equals(body, response.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, body);
  }

  @Override
  public String toString() {
    return String.format("%s{body=%s}", status, body);
  }
}
