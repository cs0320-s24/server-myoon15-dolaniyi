package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.utils.CountyDataUtilities;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

  private HashMap<String, String> StateMap;

  @Override
  public Object handle(Request request, Response response) {

    InitializeStateMap();

    Set<String> params = request.queryParams();
    String StateID = request.queryParams("state");
    String countyID = request.queryParams("county");
    StateID = StateID.replaceAll(" ", "").toLowerCase();

    System.out.println(StateID);

    // variables ? City = SanFran
    for (int i = 0; i < params.size(); i++) {
      System.out.println("Parameter (" + i + "): " + params.toArray()[i].toString());
    }

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {

      // if key in Map:
      //    countyJason = map[key]
      // else
      //    Proxy.add((rhodeisland02, json))

      /*
      proxy: Hashmap main
      returns UnmodifyableMap
      from this handler: take unmodifymap and check if URI already accessed
       */

      /*
      class Proxy{
        private Hashmap data
        private UnmodifyableMap returnable

        data == returnable

        public void addToMap(key value){
          data.put(key, value)
          unmod = data
      }

       */


      // Sends a request to the API and receives JSON back
      String countyJson = this.sendRequest(StateID, countyID);
      // Map.put(parameters, countyJson);


      String[][] CountyData = CountyDataUtilities.deserializeCounty(countyJson);
      int x = CountyData.length;
      int y = CountyData[0].length;
      String[][] SerializedData = new String[x - 1][y - 2];

      // formats output data
      for (int i = 0; i < SerializedData.length; i++) {
        for (int j = 0; j < SerializedData[i].length; j++) {
          SerializedData[i][j] = CountyData[i + 1][j];
        }
      }

      String JsonData = CountyDataUtilities.serializeCounty(SerializedData);

      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("time retrieved", LocalTime.now());
      responseMap.put("data", JsonData);

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

  private String sendRequest(String stateID, String countyID)
      throws URISyntaxException, IOException, InterruptedException {
    // Build a request to this BoredAPI. Try out this link in your browser, what do you see?
    // TODO 1: Looking at the documentation, how can we add to the URI to query based
    // on participant number?

    HttpRequest buildBoredApiRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                        + countyID
                        + "&in=state:"
                        + this.StateMap.get(stateID.trim().toLowerCase())))
            .GET()
            .build();
    // link.gov/...? + variable +"/" + var2
    // localhost/loadcsv?NAME=xxx,COUNTY=xxx
    // convert xxx -> link

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

  private void InitializeStateMap() {

    this.StateMap = new HashMap<>();

    String[][] data = {
      {"Alabama", "01"},
      {"Alaska", "02"},
      {"Arizona", "04"},
      {"Arkansas", "05"},
      {"California", "06"},
      {"Louisiana", "22"},
      {"Kentucky", "21"},
      {"Colorado", "08"},
      {"Connecticut", "09"},
      {"Delaware", "10"},
      {"District of Columbia", "11"},
      {"Florida", "12"},
      {"Georgia", "13"},
      {"Hawaii", "15"},
      {"Idaho", "16"},
      {"Illinois", "17"},
      {"Indiana", "18"},
      {"Iowa", "19"},
      {"Kansas", "20"},
      {"Maine", "23"},
      {"Maryland", "24"},
      {"Massachusetts", "25"},
      {"Michigan", "26"},
      {"Minnesota", "27"},
      {"Mississippi", "28"},
      {"Missouri", "29"},
      {"Montana", "30"},
      {"Nebraska", "31"},
      {"Nevada", "32"},
      {"New Hampshire", "33"},
      {"New Jersey", "34"},
      {"New Mexico", "35"},
      {"New York", "36"},
      {"North Carolina", "37"},
      {"North Dakota", "38"},
      {"Ohio", "39"},
      {"Oklahoma", "40"},
      {"Oregon", "41"},
      {"Pennsylvania", "42"},
      {"Rhode Island", "44"},
      {"South Carolina", "45"},
      {"South Dakota", "46"},
      {"Tennessee", "47"},
      {"Texas", "48"},
      {"Utah", "49"},
      {"Vermont", "50"},
      {"Virginia", "51"},
      {"Washington", "53"},
      {"West Virginia", "54"},
      {"Wisconsin", "55"},
      {"Wyoming", "56"},
      {"Puerto Rico", "72"}
    };

    for (String[] entry : data) {
      if (entry.length >= 2) {
        entry[0] = entry[0].replaceAll(" ", "").toLowerCase();
        this.StateMap.put(entry[0], entry[1]);
      }
    }
  }
}
