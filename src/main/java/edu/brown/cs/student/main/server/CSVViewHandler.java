package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.search.CSVParser;
import edu.brown.cs.student.main.utils.SerializeUtility;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Displays the loaded parser elements to user. LoadCSV must be called first before this is toggled.
 */
public class CSVViewHandler implements Route {
  CSVLoadHandler loadHandler;

  public CSVViewHandler(CSVLoadHandler loader) {
    this.loadHandler = loader;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    Map<String, Object> responseMap = new HashMap<>();

    CSVParser parser = this.loadHandler.GetParser();

    if (parser == null) {
      responseMap.put("result", "CSV not loaded");
      return responseMap;
    }

    List<String[]> out = parser.getElts();

    String[][] arrayOut = new String[out.size()][out.get(0).length];
    for (int i = 0; i < out.size(); i += 1) {
      for (int j = 0; j < out.get(0).length; j += 1) {
        arrayOut[i][j] = out.get(i)[j];
      }
    }
    String JsonSerialized = SerializeUtility.ArrayToJson(arrayOut);

    responseMap.put("result", "success");
    responseMap.put("data", JsonSerialized);

    return responseMap;
  }
}
