package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.search.CSVParser;
import edu.brown.cs.student.main.search.CSVSearcher;
import edu.brown.cs.student.main.search.Coordinate;
import edu.brown.cs.student.main.utils.SerializeUtility;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Route;

/**
 * SearchHandler for searching the parser's results after the loadhandler is called. Uses parameters
 * specified by user to identify which segments of data to return
 */
public class CSVSearchHandler implements Route {
  CSVLoadHandler loadHandler;

  public CSVSearchHandler(CSVLoadHandler loader) {
    this.loadHandler = loader;
  }

  @Override
  public Object handle(Request request, spark.Response response) throws Exception {

    Map<String, Object> responseMap = new HashMap<>();

    // checks if filepath is actually a param
    Set<String> params = request.queryParams();
    if (!params.contains("word") || !params.contains("column")) {
      responseMap.put("result", "error_bad_request");
      return new Response(responseMap).serialize();
    }

    String word = request.queryParams("word");
    String col = request.queryParams("column");

    CSVParser parser = this.loadHandler.GetParser();

    if (parser == null) {
      responseMap.put("result", "CSV not loaded");
      return responseMap;
    }
    if (word == null || col == null) {
      responseMap.put("result", "error_bad_request");
      return new Response(responseMap).serialize();
    }
    List<String[]> elements = parser.getElts();
    CSVSearcher searcher = parser.searchCSV(word, col);
    List<Coordinate> matches = searcher.returnList();
    String[][] arrayOut = new String[matches.size()][elements.get(0).length];

    // Cycles through match list
    for (int i = 0; i < matches.size(); i += 1) {
      int row = matches.get(i).getRow();
      arrayOut[i] = elements.get(row);
    }

    String JsonSerialized = SerializeUtility.ArrayToJson(arrayOut);
    JsonSerialized = JsonSerialized.replaceAll("\"", "");
    if (parser.isMalformed()) {
      responseMap.put("result", "error_bad_json");
    } else {
      responseMap.put("result", "success");
    }
    responseMap.put("data", JsonSerialized);

    return new Response(responseMap).serialize();
  }
}
