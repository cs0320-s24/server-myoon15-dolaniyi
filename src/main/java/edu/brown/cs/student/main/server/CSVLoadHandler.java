package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.search.CSVParser;
import edu.brown.cs.student.main.utils.ArrayCreator;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import spark.Request;
import spark.Route;

/**
 * Class reponsible for handling spark endpoints directed to "loadcsv" By extending Route, this
 * class's Handle() method directs the Parser with instructions according to parameters provided by
 * the user's query: a file path, primarily.
 */
public class CSVLoadHandler implements Route {

  private CSVParser parser;

  /**
   * @returns the stored parser within the loadhandler
   */
  public CSVParser GetParser() {
    return this.parser;
  }

  @Override
  public Object handle(Request request, spark.Response response) throws FileNotFoundException {

    Map<String, Object> responseMap = new HashMap<>();

    try {
      Set<String> params = request.queryParams();
      // checks if filepath is actually a param
      if (!params.contains("filepath")) {
        responseMap.put("result", "error_bad_request");
        return new Response(responseMap).serialize();
      }

      String filepath = request.queryParams("filepath");
      filepath = filepath.replaceAll(" ", "").toLowerCase();

      FileReader f = new FileReader(filepath);
      ArrayCreator arrCreator = new ArrayCreator();
      this.parser = new CSVParser(f, arrCreator);

      if (filepath == null) {
        responseMap.put("result", "error_bad_request");
        return new Response(responseMap).serialize();
      }
      // protects accessible data
      if (!filepath.startsWith("data/") || !filepath.endsWith(".csv")) {
        responseMap.put("result", "error_datasource");
        return new Response(responseMap).serialize();
      }
      // recognizes malformed CSV
      if (parser.isMalformed()) {
        responseMap.put("result", "error_bad_json");
//        System.out.println("malformed");
        return new Response(responseMap).serialize();
      }

      responseMap.put("result", "success");

    } catch (FileNotFoundException e) {
      responseMap.put("result", "error_datasource");
    }

    return new Response(responseMap).serialize();
  }
}
