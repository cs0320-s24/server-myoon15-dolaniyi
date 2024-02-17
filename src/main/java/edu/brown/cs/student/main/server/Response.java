package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.Map;

/**
 * Record responsible for storing and serializing the responses provided by the handlers.
 *
 * @param response_type the state of the response
 * @param responseMap the data provided by the handlers
 */
public record Response(String response_type, Map<String, Object> responseMap) {
  public Response(Map<String, Object> responseMap) {
    this("sent", responseMap);
  }

  /**
   * @return the response map provided by the handler
   */
  public Map<String, Object> getMap() {
    return responseMap;
  }

  /**
   * @return Serialized string of this record instance
   */
  String serialize() {
    try {
      // Initialize Moshi which takes in this class and returns it as JSON!
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<Response> adapter = moshi.adapter(Response.class);
      return adapter.toJson(this);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
