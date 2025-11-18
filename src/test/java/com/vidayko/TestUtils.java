package com.vidayko;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;
import static com.fasterxml.jackson.core.JsonParser.Feature.*;
import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static com.fasterxml.jackson.databind.MapperFeature.*;
import static com.fasterxml.jackson.databind.SerializationFeature.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import org.apache.commons.io.*;

public class TestUtils {

  private final static ObjectMapper mapper = new ObjectMapper()
      .findAndRegisterModules()
      .configure(FAIL_ON_IGNORED_PROPERTIES, false)
      .configure(ALLOW_SINGLE_QUOTES, true)
      .configure(ORDER_MAP_ENTRIES_BY_KEYS, true)
      .configure(SORT_PROPERTIES_ALPHABETICALLY, true)
      .disable(FAIL_ON_EMPTY_BEANS)
      .disable(FAIL_ON_ORDER_MAP_BY_INCOMPARABLE_KEY)
      .setSerializationInclusion(NON_NULL);

  public static <T> String stringOf(Class<T> type, String path) throws IOException {
    return stringOf(type.getClassLoader(), path);
  }

  public static String stringOf(ClassLoader classLoader, String path) throws IOException {
    return IOUtils.toString(
        Objects.requireNonNull(classLoader.getResourceAsStream(path)),
        StandardCharsets.UTF_8);
  }

  public static <T> T[] readValue(String string, Class<T[]> clazz) throws JsonProcessingException {
    return mapper.readValue(string, clazz);
  }
}
