package edu.brown.cs.student.main.server;

import com.squareup.moshi.Moshi;

public record FailResponse(String response_type) {
  public FailResponse() {
    this("error");
  }
  /**
   * @return this response, serialized as Json
   */
  String serialize() {
    Moshi moshi = new Moshi.Builder().build();
    return moshi.adapter(FailResponse.class).toJson(this);
  }
}

