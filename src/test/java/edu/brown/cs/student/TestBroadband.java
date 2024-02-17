package edu.brown.cs.student;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.*;
import edu.brown.cs.student.main.utils.APIBroadbandData;
import edu.brown.cs.student.main.utils.MockBroadbandData;
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

public class TestBroadband {
  private CSVLoadHandler load = new CSVLoadHandler();
  private SerializeUtility serializeUtility;
  private BroadbandHandler broadbandHandler;

  @BeforeAll
  public static void setupOnce() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  @BeforeEach
  public void setup() {
    // mock CSV parser that holds a filepath
    this.broadbandHandler = new BroadbandHandler(new MockBroadbandData());
    Spark.get("loadcsv", load);
    Spark.get("viewcsv", new CSVViewHandler(load));
    Spark.get("broadband", this.broadbandHandler);
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
    HttpURLConnection broadConnection = tryRequest("broadband?state=california&county=031");

    Moshi moshi = new Moshi.Builder().build();

    Response response =
        moshi
            .adapter(Response.class)
            .fromJson(new Buffer().readFrom(broadConnection.getInputStream()));

    String jsonData = (String) response.getMap().get("data");

    Assert.assertEquals(jsonData, MockBroadbandData.ExpectedData);

    broadConnection.disconnect();
  }

  @Test
  public void testInvalidStateID() throws IOException {
    this.broadbandHandler.SetState(new APIBroadbandData());
    HttpURLConnection broadConnection = tryRequest("broadband?state=newhamptons&county=031");

    Moshi moshi = new Moshi.Builder().build();

    Response response =
        moshi
            .adapter(Response.class)
            .fromJson(new Buffer().readFrom(broadConnection.getInputStream()));

    String result = (String) response.getMap().get("result");

    Assert.assertEquals(result, "Exception: " + BroadbandHandler.InvalidCallError);

    broadConnection.disconnect();
  }

  @Test
  public void testInvalidCountyID() throws IOException {
    this.broadbandHandler.SetState(new APIBroadbandData());
    HttpURLConnection broadConnection = tryRequest("broadband?state=Iowa&county=11");

    Moshi moshi = new Moshi.Builder().build();

    Response response =
        moshi
            .adapter(Response.class)
            .fromJson(new Buffer().readFrom(broadConnection.getInputStream()));

    String result = (String) response.getMap().get("result");

    Assert.assertEquals(result, "Exception: " + BroadbandHandler.InvalidCallError);

    broadConnection.disconnect();
  }

  @Test
  public void illformedRequest() throws IOException {
    HttpURLConnection loadConnection = tryRequest("broadband?WRONG=xyz.csv");
    // tests if successful connection
    // this calls handle(...) method inside load
    loadConnection.getInputStream();

    Moshi moshi = new Moshi.Builder().build();
    Response response =
        moshi
            .adapter(Response.class)
            .fromJson(new Buffer().readFrom(loadConnection.getInputStream()));

    String resultString = (String) response.getMap().get("result");

    Assert.assertEquals(resultString, "error_bad_request");

    loadConnection.disconnect();
  }
}
