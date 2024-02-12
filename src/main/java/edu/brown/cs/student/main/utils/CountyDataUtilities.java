package edu.brown.cs.student.main.utils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class CountyDataUtilities {
  public static String[] deserializeCounty(String jsonCounty) {
    try {
      Moshi moshi = new Moshi.Builder().build();

      Type type = Types.newParameterizedType(List.class, String.class);
      JsonAdapter<List<String>> adapter = moshi.adapter(type);
      System.out.println("r: " + adapter.fromJson(jsonCounty));

      return new String[0];

    } catch (IOException e) {
      e.printStackTrace();
      return new String[0];
    }
  }
}
