package edu.brown.cs.student;

import static org.testng.AssertJUnit.assertEquals;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.CSVLoadHandler;
import edu.brown.cs.student.main.server.CSVSearchHandler;
import edu.brown.cs.student.main.server.CSVViewHandler;
import edu.brown.cs.student.main.server.SuccessResponse;
import edu.brown.cs.student.main.utils.SerializeUtility;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import spark.Spark;

public class TestSearchHandler {
  private CSVLoadHandler load = new CSVLoadHandler();
  private SerializeUtility serializeUtility;

  @BeforeAll
  public static void setupOnce() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  //  private JsonAdapter<String[][]> adapter;

  @BeforeEach
  public void setup() {
    // mock CSV parser that holds a filepath
    Spark.get("loadcsv", load);
    Spark.get("viewcsv", new CSVViewHandler(load));
    Spark.get("searchcsv", new CSVSearchHandler(load));
    Spark.init();
    Spark.awaitInitialization();
  }

  @AfterEach
  public void teardown() {
    Spark.unmap("loadcsv");
    Spark.unmap("viewcsv");
    Spark.unmap("searchcsv");
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
  public void ExistsAnyColumn() throws IOException {
    HttpURLConnection loadConnection =
        tryRequest("loadcsv?filepath=data/dol_ri_earnings_disparity.csv");
    // tests if successful connection
    assertEquals(200, loadConnection.getResponseCode());
    // this calls handle(...) method inside load
    loadConnection.getInputStream();

    HttpURLConnection searchConnection = tryRequest("searchcsv?word=ri&column=");

    Moshi moshi = new Moshi.Builder().build();
    SuccessResponse response =
        moshi
            .adapter(SuccessResponse.class)
            .fromJson(new Buffer().readFrom(searchConnection.getInputStream()));

    String dataString = (String) response.getMap().get("data");
    String resultString = (String) response.getMap().get("result");

    // testing length of serialized Json
    Assert.assertEquals(dataString.length(), 406);
    // testing result
    Assert.assertEquals(resultString, "success");
    // testing

    loadConnection.disconnect();
    searchConnection.disconnect();
  }

  @Test
  public void existsSpecificColumn() throws IOException {
    HttpURLConnection loadConnection =
        tryRequest("loadcsv?filepath=data/dol_ri_earnings_disparity.csv");
    // tests if successful connection
    assertEquals(200, loadConnection.getResponseCode());
    // this calls handle(...) method inside load
    loadConnection.getInputStream();

    HttpURLConnection searchConnection = tryRequest("searchcsv?word=ri&column=");

    Moshi moshi = new Moshi.Builder().build();
    SuccessResponse response =
        moshi
            .adapter(SuccessResponse.class)
            .fromJson(new Buffer().readFrom(searchConnection.getInputStream()));

    String dataString = (String) response.getMap().get("data");
    String resultString = (String) response.getMap().get("result");

    // testing length of serialized Json
    Assert.assertEquals(dataString.length(), 406);
    // testing result
    Assert.assertEquals(resultString, "success");
    // testing

    loadConnection.disconnect();
    searchConnection.disconnect();
  }

  @Test
  public void detectMalformed() throws IOException {
    HttpURLConnection loadConnection = tryRequest("loadcsv?filepath=data/malformed_signs.csv");
    // tests if successful connection
    assertEquals(200, loadConnection.getResponseCode());
    // this calls handle(...) method inside load
    loadConnection.getInputStream();

    HttpURLConnection searchConnection = tryRequest("searchcsv?word=ri&column=");

    Moshi moshi = new Moshi.Builder().build();
    SuccessResponse response =
        moshi
            .adapter(SuccessResponse.class)
            .fromJson(new Buffer().readFrom(searchConnection.getInputStream()));

    String dataString = (String) response.getMap().get("data");
    String resultString = (String) response.getMap().get("result");

    // testing length of serialized Json
    //    Assert.assertEquals(dataString.length(), 406);
    // testing result
    System.out.println(dataString);
    Assert.assertEquals(resultString, "ill-formed");
    // testing

    loadConnection.disconnect();
    searchConnection.disconnect();
  }

  @Test
  public void detectNormalWithCommas() throws IOException {
    HttpURLConnection loadConnection =
        tryRequest("loadcsv?filepath=data/dol_ri_earnings_disparity.csv");
    // tests if successful connection
    assertEquals(200, loadConnection.getResponseCode());
    // this calls handle(...) method inside load
    loadConnection.getInputStream();

    HttpURLConnection searchConnection = tryRequest("searchcsv?word=ri&column=");

    Moshi moshi = new Moshi.Builder().build();
    SuccessResponse response =
        moshi
            .adapter(SuccessResponse.class)
            .fromJson(new Buffer().readFrom(searchConnection.getInputStream()));

    String dataString = (String) response.getMap().get("data");
    String resultString = (String) response.getMap().get("result");

    // testing length of serialized Json
    //    Assert.assertEquals(dataString.length(), 406);
    // testing result
    //    System.out.println(dataString);
    Assert.assertEquals(resultString, "success");
    // testing

    loadConnection.disconnect();
    searchConnection.disconnect();
  }
}
