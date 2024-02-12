package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.utils.CountyDataUtilities;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class CSVHandler implements Route {

  @Override
  public Object handle(Request request, Response response) {
    Set<String> params = request.queryParams();
    String participants = request.queryParams("");

    for (int i = 0; i < params.size(); i++) {
      System.out.println("Parameter (" + i + "): " + params.toArray()[i].toString());
    }

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      String countyJson = this.sendRequest(participants);
      //      System.out.println("Result: " + activityJson);
      // Deserializes JSON into an Activity
      //          Activity activity = ActivityAPIUtilities.deserializeActivity(activityJson);
      String[] counties = CountyDataUtilities.deserializeCounty(countyJson);

      for (int i = 0; i < counties.length; i++) {
        System.out.println("County (" + i + "): " + counties[i]);
      }
      // Adds results to the responseMap
      responseMap.put("result", "success");
      //    responseMap.put("activity", activity);
      return responseMap;
    } catch (Exception e) {
      e.printStackTrace();

      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
    }
    return responseMap;
  }

  private String sendRequest(String participants)
      throws URISyntaxException, IOException, InterruptedException {
    // Build a request to this BoredAPI. Try out this link in your browser, what do you see?
    // TODO 1: Looking at the documentation, how can we add to the URI to query based
    // on participant number?
    HttpRequest buildBoredApiRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:*&in=state:06"))
            .GET()
            .build();

    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> sentBoredApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());

    // What's the difference between these two lines? Why do we return the body? What is useful from
    // the raw response (hint: how can we use the status of response)?
    //    System.out.println(sentBoredApiResponse);
    //    System.out.println(sentBoredApiResponse.body());

    return sentBoredApiResponse.body();
  }
}
