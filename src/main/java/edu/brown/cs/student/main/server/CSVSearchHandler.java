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
import spark.Response;
import spark.Route;

public class CSVSearchHandler implements Route {
  CSVLoadHandler loadHandler;

  public CSVSearchHandler(CSVLoadHandler loader) {
    this.loadHandler = loader;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    Map<String, Object> responseMap = new HashMap<>();

    Set<String> params = request.queryParams();
    String word = request.queryParams("word");
    String col = request.queryParams("column");

    CSVParser parser = this.loadHandler.GetParser();

    if (parser == null) {
      responseMap.put("result", "CSV not loaded");
      return responseMap;
    }
    List<String[]> elements = parser.getElts();
    CSVSearcher searcher = parser.searchCSV(word, col);
    List<Coordinate> matches = searcher.returnList();
    String[][] arrayOut = new String[matches.size()][elements.get(0).length];

    for (int i = 0; i < matches.size(); i += 1) {
      int row = matches.get(i).getRow();
      arrayOut[i] = elements.get(row);

      /*  for (int j = 0; j < elements.size(); j++) {
          int row = matches.get(i).getRow();
          int col = matches.get(i).getCol();

          arrayOut[i][j] = elements.get(row)[col];
      }*/
    }

    //        for (int i = 0; i < out.size(); i += 1) {
    //            for (int j = 0; j < out.get(0).length; j += 1) {
    //                arrayOut[i][j] = out.get(i)[j];
    //            }
    //        }
    String JsonSerialized = SerializeUtility.ArrayToJson(arrayOut);
    //    System.out.println("JSON = " + JsonSerialized);
    if (parser.isMalformed()) {
      responseMap.put("result", "error_bad_json");
    } else {
      responseMap.put("result", "success");
    }
    responseMap.put("data", JsonSerialized);

    //  return responseMap;
    return new SuccessResponse(responseMap).serialize();
  }
}
