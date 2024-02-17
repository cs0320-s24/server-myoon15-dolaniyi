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
    //    System.out.println("handle called");
    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();

    try {
      Set<String> params = request.queryParams();
      String filepath = request.queryParams("filepath");
      filepath = filepath.replaceAll(" ", "").toLowerCase();

      //      System.out.println(filepath);

      FileReader f = new FileReader(filepath);
      ArrayCreator arrCreator = new ArrayCreator();
      this.parser = new CSVParser(f, arrCreator);

      // protects accessible data
      if (!filepath.startsWith("data/") || !filepath.endsWith(".csv")) {
        responseMap.put("result", "error_datasource");
        return new SuccessResponse(responseMap).serialize();
      }
      // recognizes malformed CSV
      if (parser.isMalformed()){
        responseMap.put("result", "error_bad_json");
        System.out.println("malformed");
        return new SuccessResponse(responseMap).serialize();
      }

      responseMap.put("result", "success");

    } catch (FileNotFoundException e) {
      responseMap.put("result", "error_datasource");
      //      System.out.println("File not found: " + e.getMessage());
    }

    //    return responseMap;
    return new SuccessResponse(responseMap).serialize();
  }
}
