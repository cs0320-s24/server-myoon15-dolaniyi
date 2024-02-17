package edu.brown.cs.student.main.interfaces;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;


/**
 * Abstract super class for ock
 */
public abstract class DataSource {

  protected HashMap<String, String> StateMap;
  public final String InvalidStateID = "[INVALID STATE]";
  public final String InvalidCallAPI = "[INVALID CALL]";

  public abstract String requestData(String state, String county)
      throws URISyntaxException, IOException, InterruptedException;

  public boolean isValidState(String state) {

    return true;
    //  return this.StateMap.containsKey(state);
  }

  public DataSource() {
    // InitializeStateMap();
  }

  /*
    private void InitializeStateMap() {

      this.StateMap = new HashMap<>();

      String[][] data = {
        {"Alabama", "01"},
        {"Alaska", "02"},
        {"Arizona", "04"},
        {"Arkansas", "05"},
        {"California", "06"},
        {"Louisiana", "22"},
        {"Kentucky", "21"},
        {"Colorado", "08"},
        {"Connecticut", "09"},
        {"Delaware", "10"},
        {"District of Columbia", "11"},
        {"Florida", "12"},
        {"Georgia", "13"},
        {"Hawaii", "15"},
        {"Idaho", "16"},
        {"Illinois", "17"},
        {"Indiana", "18"},
        {"Iowa", "19"},
        {"Kansas", "20"},
        {"Maine", "23"},
        {"Maryland", "24"},
        {"Massachusetts", "25"},
        {"Michigan", "26"},
        {"Minnesota", "27"},
        {"Mississippi", "28"},
        {"Missouri", "29"},
        {"Montana", "30"},
        {"Nebraska", "31"},
        {"Nevada", "32"},
        {"New Hampshire", "33"},
        {"New Jersey", "34"},
        {"New Mexico", "35"},
        {"New York", "36"},
        {"North Carolina", "37"},
        {"North Dakota", "38"},
        {"Ohio", "39"},
        {"Oklahoma", "40"},
        {"Oregon", "41"},
        {"Pennsylvania", "42"},
        {"Rhode Island", "44"},
        {"South Carolina", "45"},
        {"South Dakota", "46"},
        {"Tennessee", "47"},
        {"Texas", "48"},
        {"Utah", "49"},
        {"Vermont", "50"},
        {"Virginia", "51"},
        {"Washington", "53"},
        {"West Virginia", "54"},
        {"Wisconsin", "55"},
        {"Wyoming", "56"},
        {"Puerto Rico", "72"}
      };

      for (String[] entry : data) {
        if (entry.length >= 2) {
          entry[0] = entry[0].replaceAll(" ", "").toLowerCase();
          this.StateMap.put(entry[0], entry[1]);
        }
      }
    }
  */
}
