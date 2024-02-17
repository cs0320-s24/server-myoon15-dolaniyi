package edu.brown.cs.student.main.utils;

import edu.brown.cs.student.main.interfaces.DataSource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/** A datasource that actually connect the BroadbandHandler with an API call. */
public class APIBroadbandData implements DataSource {

  @Override
  public String requestData(String stateID, String countyID)
      throws URISyntaxException, IOException, InterruptedException {

    HttpRequest buildBoredApiRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                        + countyID
                        + "&in=state:"
                        + /*super.StateMap.get(*/ stateID.trim().toLowerCase()) /*)*/)
            .GET()
            .build();

    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> sentBoredApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());

    // if the response if empty, return error
    if (sentBoredApiResponse.body().equals("")) {
      return InvalidCallAPI;
    }
    return sentBoredApiResponse.body();
  }
}
