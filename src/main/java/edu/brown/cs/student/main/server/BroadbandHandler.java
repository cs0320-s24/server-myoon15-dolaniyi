package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.Caching.CachedBroadband;
import edu.brown.cs.student.main.interfaces.DataSource;
import edu.brown.cs.student.main.utils.SerializeUtility;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Route;

public class BroadbandHandler implements Route {

  private CachedBroadband cache = new CachedBroadband(this);
  private DataSource state;
  public static final String InvalidStateError =
      "The state you requested either was misspelled or did not exist.  Please try again.";
  public static final String InvalidCallError =
      "An empty response was received.  This may be due to an invalid API call.";

  public BroadbandHandler(DataSource source) {
    this.state = source;
  }

  public void SetState(DataSource newState) {
    this.state = newState;
  }

  @Override
  public Object handle(Request request, spark.Response response) {
    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();

    Set<String> params = request.queryParams();
    if (!params.contains("state") || !params.contains("county")) {
      responseMap.put("result", "error_bad_request");
      return new Response(responseMap).serialize();
    }

    //    request.body().contains("state");
    String StateID = request.queryParams("state");
    String countyID = request.queryParams("county");

    //    if (countyID.equals("")) countyID = "*";
    StateID = StateID.replaceAll(" ", "").toLowerCase();

    // Sends a request to the API and receives JSON back
    String countyJson = this.cache.search(StateID, countyID);

    /*    if (countyJson == this.state.InvalidStateID) {
      responseMap.put("result", "Exception: " + InvalidStateError);
    } else */
    if (StateID == null || countyID == null) {
      //      System.out.println("null");
      responseMap.put("result", "error_bad_request");
    } else if (countyJson == this.state.InvalidCallAPI) {
      //      System.out.println("API FAILED");
      responseMap.put("result", "Exception: " + InvalidCallError);
    } else {
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

      responseMap.put("data", JsonData);
    }

    return new Response(responseMap).serialize();
    /*   }
    catch (Exception e) {
      e.printStackTrace();
      responseMap.put("result", "Exception");
    }
    return responseMap;*/
  }

  public String sendRequest(String stateID, String countyID)
      throws URISyntaxException, IOException, InterruptedException {

    return this.state.requestData(stateID, countyID);
  }
}
