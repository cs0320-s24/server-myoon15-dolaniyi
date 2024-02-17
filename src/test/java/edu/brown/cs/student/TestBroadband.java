package edu.brown.cs.student;

import static org.testng.AssertJUnit.assertEquals;
import static spark.Spark.*;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.*;
import edu.brown.cs.student.main.utils.APIBroadbandData;
import edu.brown.cs.student.main.utils.MockBroadbandData;
import edu.brown.cs.student.main.utils.SerializeUtility;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestBroadband {
  private CSVLoadHandler load = new CSVLoadHandler();
  private SerializeUtility serializeUtility;

  @BeforeAll
  public static void setupOnce() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  @BeforeEach
  public void setup() {
    // mock CSV parser that holds a filepath
    Spark.get("loadcsv", load);
    Spark.get("viewcsv", new CSVViewHandler(load));
    Spark.get("broadband", new BroadbandHandler(new MockBroadbandData()));
    Spark.init();
    Spark.awaitInitialization();
  }

  @AfterEach
  public void teardown() {
    Spark.unmap("loadcsv");
    Spark.unmap("viewcsv");
    Spark.unmap("broadband");
    Spark.awaitStop();
  }

  /** Helper method for URLConnection */
  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    // The default method is "GET", which is what we're using here.
    // If we were using "POST", we'd need to say so.
    clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  @Test
  public void testBroadbandWorking() throws IOException {
    HttpURLConnection loadConnection =
        tryRequest("loadcsv?filepath=data/dol_ri_earnings_disparity.csv");
    // tests if successful connection
    assertEquals(200, loadConnection.getResponseCode());
    // this calls handle(...) method inside load
    loadConnection.getInputStream();

    HttpURLConnection broadConnection = tryRequest("broadband?state=california&county=031");
   // System.out.println("broad connection: " + broadConnection.getInputStream());

    Moshi moshi = new Moshi.Builder().build();

    SuccessResponse response =
        moshi
            .adapter(SuccessResponse.class)
            .fromJson(new Buffer().readFrom(broadConnection.getInputStream()));

  //  System.out.println("Map Conversion: " + response.getMap().toString());

    //    Map<String, Object> ret = (Map<String, Object>)searchConnection.getInputStream();
    //
    //    System.out.println("size" +ret.size());

    //    String[][] response = SerializeUtility.JsonToArray(new
    // Buffer().readFrom(searchConnection.getInputStream()));

    //    Moshi moshi = new Moshi.Builder().build();
    //    String[][] response =
    //            moshi
    //                    .adapter(String[][].class)
    //                    .fromJson(new Buffer().readFrom(searchConnection.getInputStream()));
    //

    loadConnection.disconnect();
    broadConnection.disconnect();
  }
}
