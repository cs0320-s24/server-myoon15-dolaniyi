package edu.brown.cs.student.main.utils;

import edu.brown.cs.student.main.interfaces.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;

public class MockBroadbandData implements DataSource {

  private String StaticData = "[[\"NAME\",\"S2802_C03_022E\",\"state\",\"county\"],\n" +
          "[\"Kings County, California\",\"83.5\",\"06\",\"031\"]]";
  @Override
  public String requestData(String state, String county)
      throws URISyntaxException, IOException, InterruptedException {
    return StaticData;
  }
}
