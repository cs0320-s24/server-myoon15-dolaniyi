package edu.brown.cs.student.main.utils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;

public class SerializeUtility {

  //  public static String deserializeCounty(String jsonCounty) {
  //
  //    Moshi moshi = new Moshi.Builder().build();
  //    Type type = Types.newParameterizedType(List.class, Types.newParameterizedType(List.class,
  // String.class));
  //    JsonAdapter<List<List<String>>> adapter = moshi.adapter(type);
  //
  //    try {
  //      List<List<String>> rawData = adapter.fromJson(jsonCounty);
  //      return rawData;
  //
  //    } catch (IOException e) {
  //      e.printStackTrace();
  //      return "";
  //    }
  //  }
  public static String[][] deserializeCounty(String jsonCounty) {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<String[][]> adapter = moshi.adapter(String[][].class);
      String[][] data = adapter.fromJson(jsonCounty);
      return data;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String serializeCounty(String[][] countyData) {
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<String[][]> adapter = moshi.adapter(String[][].class);
    String data = adapter.toJson(countyData);
    return data;
  }
}

/*
try {

  Moshi moshi = new Moshi.Builder().build();
  JsonAdapter<String> adapter = moshi.adapter(String.class);
  String row = adapter.fromJson(jsonCounty);
  return row;

} catch (IOException e) {
  e.printStackTrace();
  return "";
}*/

/**
 * Moshi moshi = new Moshi.Builder().build();
 *
 * <p>// Initializes an adapter to an Activity class then uses it to parse the JSON.
 * JsonAdapter<String> adapter = moshi.adapter(String.class);
 *
 * <p>String row = adapter.fromJson(jsonActivity);
 *
 * <p>return activity;
 */
