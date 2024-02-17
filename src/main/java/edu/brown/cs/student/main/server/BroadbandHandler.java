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

    String StateID = request.queryParams("state");
    String countyID = request.queryParams("county");
    if (countyID.equals("")) countyID = "*";
    StateID = StateID.replaceAll(" ", "").toLowerCase();

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();

    try {
      // Sends a request to the API and receives JSON back
      String countyJson = this.cache.search(StateID, countyID);

      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("time retrieved", LocalTime.now().toString());

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
      JsonData = JsonData.replaceAll("\"", "");
      System.out.println("MAP J: " +  new SuccessResponse(responseMap).serialize());
      responseMap.put("data", JsonData);
      return new SuccessResponse(responseMap).serialize();
    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("result", "Exception");
    }
    return responseMap;
  }

  @Override
  public String sendRequest(String stateID, String countyID)
      throws URISyntaxException, IOException, InterruptedException {

    return this.state.requestData(stateID, countyID);
  }
}
