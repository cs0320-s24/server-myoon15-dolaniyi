package edu.brown.cs.student;

import static org.testng.AssertJUnit.assertEquals;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.*;
import edu.brown.cs.student.main.utils.SerializeUtility;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import spark.Spark;

public class TestSearchHandler {
  private CSVLoadHandler load = new CSVLoadHandler();
  private SerializeUtility serializeUtility;

  @BeforeClass
  public static void setupOnce() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

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

  /**
   * Searching for an elt that exists, without specifying a column
   *
   * @throws IOException
   */
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
    Response response =
        moshi
            .adapter(Response.class)
            .fromJson(new Buffer().readFrom(searchConnection.getInputStream()));

    String dataString = (String) response.getMap().get("data");
    String resultString = (String) response.getMap().get("result");
    String reference =
        "[[RI,White, $1058.47 ,395773.6521, $1.00 ,75%]," +
                "[RI,Black, $770.26 ,30424.80376, $0.73 ,6%]," +
                "[RI,Native American/American Indian, $471.07 ,2315.505646, $0.45 ,0%]," +
                "[RI,Asian-Pacific Islander, $1080.09 ,18956.71657, $1.02 ,4%]," +
                "[RI,Hispanic/Latino, $673.14 ,74596.18851, $0.64 ,14%]," +
                "[RI,Multiracial, $971.89 ,8883.049171, $0.92 ,2%]]";

    // testing length of serialized Json
    Assert.assertEquals(dataString, reference);
    // testing result
    Assert.assertEquals(resultString, "success");

    loadConnection.disconnect();
    searchConnection.disconnect();
  }

  /**
   * Tests searching for an element with a specified column, where the elt exists
   *
   * @throws IOException
   */
  @Test
  public void existsSpecificColumn() throws IOException {
    HttpURLConnection loadConnection =
        tryRequest("loadcsv?filepath=data/dol_ri_earnings_disparity.csv");
    // tests if successful connection
    assertEquals(200, loadConnection.getResponseCode());
    // this calls handle(...) method inside load
    loadConnection.getInputStream();

    HttpURLConnection searchConnection = tryRequest("searchcsv?word=ri&column=0");

    Moshi moshi = new Moshi.Builder().build();
    Response response =
        moshi
            .adapter(Response.class)
            .fromJson(new Buffer().readFrom(searchConnection.getInputStream()));

    String dataString = (String) response.getMap().get("data");
    String resultString = (String) response.getMap().get("result");
    //    System.out.println(dataString);

    String reference =
            "[[RI,White, $1058.47 ,395773.6521, $1.00 ,75%]," +
                    "[RI,Black, $770.26 ,30424.80376, $0.73 ,6%]," +
                    "[RI,Native American/American Indian, $471.07 ,2315.505646, $0.45 ,0%]," +
                    "[RI,Asian-Pacific Islander, $1080.09 ,18956.71657, $1.02 ,4%]," +
                    "[RI,Hispanic/Latino, $673.14 ,74596.18851, $0.64 ,14%]," +
                    "[RI,Multiracial, $971.89 ,8883.049171, $0.92 ,2%]]";

    // testing length of serialized Json
    Assert.assertEquals(dataString, reference);
    // testing result
    Assert.assertEquals(resultString, "success");

    loadConnection.disconnect();
    searchConnection.disconnect();
  }

  /**
   * Tests searching for an element that doesn't exist, both with a specified column, and searching
   * all columns
   *
   * @throws IOException
   */
  @Test
  public void doesNotExist() throws IOException {
    HttpURLConnection loadConnection =
        tryRequest("loadcsv?filepath=data/dol_ri_earnings_disparity.csv");
    // tests if successful connection
    assertEquals(200, loadConnection.getResponseCode());
    // this calls handle(...) method inside load
    loadConnection.getInputStream();

    HttpURLConnection allColSearchConnection = tryRequest("searchcsv?word=DOESNOTEXIST&column=");
    HttpURLConnection specificColSearchConnection =
        tryRequest("searchcsv?word=DOESNOTEXIST&column=6");

    Moshi moshi = new Moshi.Builder().build();
    Response response =
        moshi
            .adapter(Response.class)
            .fromJson(new Buffer().readFrom(allColSearchConnection.getInputStream()));

    String dataString = (String) response.getMap().get("data");
    String resultString = (String) response.getMap().get("result");
    //    System.out.println(dataString);

    // testing length of serialized Json
    Assert.assertEquals(dataString, "[]");
    // testing result
    Assert.assertEquals(resultString, "success");

    loadConnection.disconnect();
    allColSearchConnection.disconnect();
  }

  /**
   * Tests whether parser is able to detect a malformed CSV
   *
   * @throws IOException
   */
  @Test
  public void detectMalformed() throws IOException {
    HttpURLConnection loadConnection = tryRequest("loadcsv?filepath=data/malformed_signs.csv");
    // tests if successful connection
    assertEquals(200, loadConnection.getResponseCode());
    // this calls handle(...) method inside load
    loadConnection.getInputStream();

    HttpURLConnection searchConnection = tryRequest("searchcsv?word=ri&column=");

    Moshi moshi = new Moshi.Builder().build();
    Response response =
        moshi
            .adapter(Response.class)
            .fromJson(new Buffer().readFrom(searchConnection.getInputStream()));

    String resultString = (String) response.getMap().get("result");

    Assert.assertEquals(resultString, "error_bad_json");

    loadConnection.disconnect();
    searchConnection.disconnect();
  }

  /**
   * Tests regex expression by checking how commas inside elts affect malformed behavior
   *
   * @throws IOException
   */
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
    Response response =
        moshi
            .adapter(Response.class)
            .fromJson(new Buffer().readFrom(searchConnection.getInputStream()));

    String dataString = (String) response.getMap().get("data");
    String resultString = (String) response.getMap().get("result");

    Assert.assertEquals(resultString, "success");
    // testing

    loadConnection.disconnect();
    searchConnection.disconnect();
  }

  /**
   * Tests loading a file that isn't a CSV
   *
   * @throws IOException
   */
  @Test
  public void protectedData() throws IOException {
    HttpURLConnection loadConnection = tryRequest("loadcsv?filepath=home/private.csv");
    // tests if successful connection
    assertEquals(200, loadConnection.getResponseCode());
    // this calls handle(...) method inside load
    loadConnection.getInputStream();

    Moshi moshi = new Moshi.Builder().build();
    Response response =
        moshi
            .adapter(Response.class)
            .fromJson(new Buffer().readFrom(loadConnection.getInputStream()));

    String resultString = (String) response.getMap().get("result");
    Assert.assertEquals(resultString, "error_datasource");

    loadConnection.disconnect();
  }

  /**
   * Tests loading a file that isn't a CSV filetype
   *
   * @throws IOException
   */
  @Test
  public void notCSV() throws IOException {
    HttpURLConnection loadConnection = tryRequest("loadcsv?filepath=data/notCSV.jar");
    // tests if successful connection
    assertEquals(200, loadConnection.getResponseCode());
    // this calls handle(...) method inside load
    loadConnection.getInputStream();

    Moshi moshi = new Moshi.Builder().build();
    Response response =
        moshi
            .adapter(Response.class)
            .fromJson(new Buffer().readFrom(loadConnection.getInputStream()));

    String resultString = (String) response.getMap().get("result");
    Assert.assertEquals(resultString, "error_datasource");

    loadConnection.disconnect();
  }

  /**
   * Tests loading a file that isn't a CSV filetype
   *
   * @throws IOException
   */
  @Test
  public void illformedRequest() throws IOException {
    HttpURLConnection loadConnection = tryRequest("loadcsv?WRONG=xyz.csv");
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
