package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.Caching.CachedBroadband;
import edu.brown.cs.student.main.interfaces.Broadband;
import edu.brown.cs.student.main.interfaces.DataSource;
import edu.brown.cs.student.main.utils.SerializeUtility;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route, Broadband {

  private CachedBroadband cache = new CachedBroadband(this);
  private DataSource state;

  public BroadbandHandler(DataSource source) {
    this.state = source;
  }

  @Override
  public Object handle(Request request, Response response) {

    Set<String> params = request.queryParams();
    String StateID = request.queryParams("state");
    String countyID = request.queryParams("county");
    StateID = StateID.replaceAll(" ", "").toLowerCase();

    //    System.out.println(StateID);

    // variables ? City = SanFran
    //    for (int i = 0; i < params.size(); i++) {
    //      System.out.println("Parameter (" + i + "): " + params.toArray()[i].toString());
    //    }

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();

    try {
      // Sends a request to the API and receives JSON back
      String countyJson = this.cache.search(StateID, countyID);

      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("time retrieved", LocalTime.now());

      String[][] CountyData = SerializeUtility.JsonToArray(countyJson);

      int x = CountyData.length;
      int y = CountyData[0].length;
      String[][] SerializedData = new String[x - 1][y - 2];

      // formats output data
      for (int i = 0; i < SerializedData.length; i++) {
        for (int j = 0; j < SerializedData[i].length; j++) {
          SerializedData[i][j] = CountyData[i + 1][j];
        }
      }

      String JsonData = SerializeUtility.ArrayToJson(SerializedData);

      responseMap.put("data", JsonData);

      return new SuccessResponse(responseMap).serialize();
      // return new SuccessResponse(responseMap).serialize(countyJson);
    } catch (Exception e) {
      e.printStackTrace();

      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
    }

    return responseMap;
  }

  @Override
  public String sendRequest(String stateID, String countyID)
      throws URISyntaxException, IOException, InterruptedException {
    // Build a request to this BoredAPI. Try out this link in your browser, what do you see?
    // TODO 1: Looking at the documentation, how can we add to the URI to query based
    // on participant number?

    //    HttpRequest buildBoredApiRequest =
    //        HttpRequest.newBuilder()
    //            .uri(
    //                new URI(
    //
    // "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
    //                        + countyID
    //                        + "&in=state:"
    //                        + this.StateMap.get(stateID.trim().toLowerCase())))
    //            .GET()
    //            .build();
    //    // link.gov/...? + variable +"/" + var2
    //    // localhost/loadcsv?NAME=xxx,COUNTY=xxx
    //    // convert xxx -> link
    //
    //    // Send that API request then store the response in this variable. Note the generic type.
    //    HttpResponse<String> sentBoredApiResponse =
    //        HttpClient.newBuilder()
    //            .build()
    //            .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());

    return this.state.requestData(stateID, countyID);
  }



}
