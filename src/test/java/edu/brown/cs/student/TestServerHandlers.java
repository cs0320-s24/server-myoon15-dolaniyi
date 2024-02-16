// package edu.brown.cs.student;
//
// import com.squareup.moshi.Moshi;
// import edu.brown.cs.student.main.server.BroadbandHandler;
// import edu.brown.cs.student.main.server.CSVLoadHandler;
// import okio.Buffer;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.testng.Assert;
// import spark.Spark;
//
// import java.io.IOException;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.util.logging.Level;
// import java.util.logging.Logger;
//
// public class TestServerHandlers {
//
//    @BeforeAll
//    public static void setup_before_everything() {
//      Spark.port(0);
//      Logger.getLogger("").setLevel(Level.WARNING);
//    }
//
//    private String[][] referenceCSV;
//
//    @BeforeEach
//    public void setup() {
//      // Re-initialize state, etc. for _every_ test method run
//      this.referenceCSV = new String[0][0];
//
//      // In fact, restart the entire Spark server for every test!
//
//      CSVLoadHandler load = new CSVLoadHandler();
//      Spark.get("loadcsv", load);
//      Spark.init();
//      Spark.awaitInitialization(); // don't continue until the server is listening
//    }
//
//    @AfterEach
//    public void teardown() {
//      // Gracefully stop Spark listening on both endpoints after each test
//      Spark.unmap("loadcsv");
//      Spark.awaitStop(); // don't proceed until the server is stopped
//    }
//
//    @Test
//    // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the
////   type
//    // checker
//    public void testAPIOneRecipe() throws IOException {
//      /*      menu.add(
//                      Soup.buildNoExceptions(
//                              "Carrot",
//                              Arrays.asList("carrot", "onion", "celery", "garlic", "ginger",
//   "vegetable broth")));
//      */
//      HttpURLConnection clientConnection = tryRequest("broadband?state=ohio&county=*");
//      // Get an OK response (the *connection* worked, the *API* provides an error response)
//      Assert.assertEquals(200, clientConnection.getResponseCode());
//      // TEST CODE 404
//
//      // Now we need to see whether we've got the expected Json response.
//      // SoupAPIUtilities handles ingredient lists, but that's not what we've got here.
//      // NOTE:   (How could we reduce the code repetition?)
//      Moshi moshi = new Moshi.Builder().build();
//
//      // We'll use okio's Buffer class here
//      System.out.println(clientConnection.getInputStream());
//
//      BroadbandHandler.SuccessResponse response =
//          moshi
//              .adapter(BroadbandHandler.SuccessResponse.class)
//              .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
//
//      /* Soup carrot =
//      new Soup(
//              "Carrot",
//              Arrays.asList("carrot", "onion", "celery", "garlic", "ginger", "vegetable broth"),
//              false);*/
//
//      String expected = "";
//
//      String result = (String) response.responseMap().get("data");
//
//      //  System.out.println(result.get("ingredients"));
//      Assert.assertEquals(expected, result);
//      clientConnection.disconnect();
//    }
//
//    private static HttpURLConnection tryRequest(String apiCall) throws IOException {
//      // Configure the connection (but don't actually send the request yet)
//      URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
//      HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
//
//      // The default method is "GET", which is what we're using here.
//      // If we were using "POST", we'd need to say so.
//      clientConnection.setRequestMethod("GET");
//
//      clientConnection.connect();
//      return clientConnection;
//    }
//
//    //
//    //    @Test
//    //    public void testHandleMethod() {
//    //        // Mocking the Request and Response objects
//    //        Request request = mock(Request.class);
//    //        Response response = mock(Response.class);
//    //
//    //        // Setting up any necessary stubbing for the request object, if required
//    //        // For example:
//    //        // when(request.queryParams("param")).thenReturn("value");
//    //
//    //        // Call the handle method with the mocked objects
//    //        YourSparkApplication sparkApp = new YourSparkApplication(); // Instantiate your
//   Spark
//    // application class
//    //        Object result = sparkApp.handle(request, response);
//    //
//    //        // Assert the behavior or output of the handle method
//    //        // For example, you can check if the response is as expected
//    //        assertEquals("Expected result", result, expectedResult);
//    //        // Or you can assert the behavior of the response object, if any changes are made to
////   it
//    // in the handle method
//    //        // For example:
//    //        // verify(response).status(200); // Ensure status code is set correctly
//    //    }
//    //
//    //  /*  @Test
//    //    public void ExceptionTest() {
//    //        Assert.assertThrows(IllegalArgumentException.class,
//    //                () -> {
//    //                    SearchCoordinator.InitiateSearch("  ");
//    //                });
//    //    }*/
//    //
//    //
//
// }
