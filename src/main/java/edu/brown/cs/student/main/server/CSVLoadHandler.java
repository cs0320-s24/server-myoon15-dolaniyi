package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.search.CSVParser;
import edu.brown.cs.student.main.utils.ArrayCreator;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import spark.Request;
import spark.Response;
import spark.Route;

public class CSVLoadHandler implements Route {

  private CSVParser parser;

  public CSVParser GetParser() {
    if (this.parser == null) System.out.println("Parser Null. Call loadcsv first!");
    return this.parser;
  }

  @Override
  public Object handle(Request request, Response response) throws FileNotFoundException {

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();

    try {
      Set<String> params = request.queryParams();
      String filepath = request.queryParams("filepath");
      filepath = filepath.replaceAll(" ", "").toLowerCase();

      System.out.println(filepath);

      FileReader f = new FileReader(filepath);
      ArrayCreator arrCreator = new ArrayCreator();
      this.parser = new CSVParser(f, arrCreator);
      List<String[]> out = parser.getElts();
      //      for (String[] array : out) {
      //        for (String element : array) {
      //          System.out.print(element);
      //        }
      //        System.out.println();
      //      }

      responseMap.put("result", "success");

    } catch (FileNotFoundException e) {
      responseMap.put("result", "File not found: " + e.getMessage());
      System.out.println("File not found: " + e.getMessage());
    }

    return responseMap;
  }

  //    private String sendRequest(String stateID, String countyID)
  //            throws URISyntaxException, IOException, InterruptedException {
  //        // Build a request to this BoredAPI. Try out this link in your browser, what do you see?
  //        // TODO 1: Looking at the documentation, how can we add to the URI to query based
  //        // on participant number?
  //
  //        HttpRequest buildBoredApiRequest =
  //                HttpRequest.newBuilder()
  //                        .uri(
  //                                new URI(
  //
  // "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
  //                                                + countyID
  //                                                + "&in=state:"
  //                                                +
  // this.StateMap.get(stateID.trim().toLowerCase())))
  //                        .GET()
  //                        .build();
  //        // link.gov/...? + variable +"/" + var2
  //        // localhost/loadcsv?NAME=xxx,COUNTY=xxx
  //        // convert xxx -> link
  //
  //        // Send that API request then store the response in this variable. Note the generic
  // type.
  //        HttpResponse<String> sentBoredApiResponse =
  //                HttpClient.newBuilder()
  //                        .build()
  //                        .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());
  //
  //        // What's the difference between these two lines? Why do we return the body? What is
  // useful from
  //        // the raw response (hint: how can we use the status of response)?
  //        //    System.out.println(sentBoredApiResponse);
  //        //    System.out.println(sentBoredApiResponse.body());
  //
  //        return sentBoredApiResponse.body();
  //    }
}
