package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.Map;

public record SearchSuccess(String response_type, Map<String, Object> responseMap) {
  public SearchSuccess(Map<String, Object> responseMap) {
    this("success", responseMap);


  }

  String serialize() {
    try {
      // Initialize Moshi which takes in this class and returns it as JSON!
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<SearchSuccess> adapter = moshi.adapter(SearchSuccess.class);
      return adapter.toJson(this);
    } catch (Exception e) {
      // For debugging purposes, show in the console _why_ this fails
      // Otherwise we'll just get an error 500 from the API in integration
      // testing.
      e.printStackTrace();
      throw e;
    }
  }

}
